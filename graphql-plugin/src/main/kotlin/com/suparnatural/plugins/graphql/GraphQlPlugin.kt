package com.suparnatural.plugins.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.suparnatural.plugins.graphql.models.Container
import com.suparnatural.plugins.graphql.processors.processFragments
import com.suparnatural.plugins.graphql.processors.processOperations
import com.suparnatural.plugins.graphql.processors.processTypes
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileWriter
import java.nio.charset.Charset
import java.nio.file.Paths

open class GraphQlPluginExtension {
    var packageName: String = ""
    var outputDirectoryPath: String = ""
    var localSchemaFilePath: String? = null
    var endpointUrl: String? = null
    var documentsPath: String? = null
    var headers: List<String>? = null
}

val Json = ObjectMapper().registerKotlinModule()
const val APOLLO_TOOLING_SCRIPT_NAME = "apollo-tooling.sh"
const val APOLLO_CODEGEN_OUTPUT_FILE = "types.json"

class GraphQlPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("suparnaturalGraphQl", GraphQlPluginExtension::class.java)

        target.task("graphQlCodeGen") {
            group = "suparnatural"
            description = "Generate type safe classes from GraphQL documents"
            doLast {

                val localSchemaFilePath = extension.localSchemaFilePath
                val endpointUrl = extension.endpointUrl
                val documentsPath = extension.documentsPath ?: throw Exception("documentsPath cannot be null.")

                if (localSchemaFilePath == null && endpointUrl == null) throw Exception("one of localSchemaFilePath or endpointUrl must be defined")

                if (!localSchemaFilePath.isNullOrEmpty() && !endpointUrl.isNullOrEmpty()) {
                    logger.warn("both localSchemaFilePath and endpointUrl are set, localSchemaFilePath will be used")
                }

                // prepare apollo command line tooling
                val stream = GraphQlPlugin::class.java.classLoader.getResourceAsStream(APOLLO_TOOLING_SCRIPT_NAME)
                    ?: throw Exception("Unable to load code gen script")

                val scriptPath = Paths.get(target.buildDir.absolutePath, APOLLO_TOOLING_SCRIPT_NAME)
                val scriptFile = scriptPath.toFile()
                stream.reader(Charset.forName("UTF-8")).use {
                    val script = it.readText()
                    logger.info("loaded code gen script")

                    // ensure build directory exists
                    if (!target.buildDir.exists()) {
                        logger.info("build directory not found, creating now")
                        target.buildDir.mkdir()
                    }

                    val writer = FileWriter(scriptFile)
                    writer.use {
                        // write script to build directory
                        writer.write(script)
                        logger.info("copied code gen script to build directory")
                    }
                }

                logger.info("executing code gen script")
                val arguments = mutableListOf("sh", scriptFile.absolutePath, "client:codegen", "--target=json")
                extension.headers?.forEach {
                    arguments.add("--header")
                    arguments.add(it)
                }
                if (!localSchemaFilePath.isNullOrEmpty()) {
                    val localSchemaFile = target.file(extension.localSchemaFilePath!!).absolutePath
                    arguments.add("--localSchemaFile=$localSchemaFile")
                }
                if (localSchemaFilePath.isNullOrEmpty() && !endpointUrl.isNullOrEmpty()) {
                    arguments.add("--endpoint=$endpointUrl")
                }

                arguments.add("--includes=${target.file(documentsPath).absolutePath}")

                val typesJsonFile = Paths.get(target.buildDir.absolutePath, APOLLO_CODEGEN_OUTPUT_FILE).toFile()
                arguments.add(typesJsonFile.absolutePath)

                logger.info("executing ${arguments.joinToString(" ")}")
                target.exec {
                    commandLine(arguments)
                }

                if (!typesJsonFile.exists()) throw Exception("code gen script failed to generate types file")

                logger.info("generating classes")
                val json = Json.readValue(typesJsonFile, Container::class.java)

                val outputDirectory = target.file(extension.outputDirectoryPath)
                processTypes(json.typesUsed, extension).writeTo(outputDirectory)
                processOperations(json.operations, extension).writeTo(outputDirectory)
                processFragments(json.fragments, extension).writeTo(outputDirectory)

                typesJsonFile.delete()
                logger.info("deleted intermediate json file")

                logger.info("done")
            }
        }
    }
}

fun Project.suparnaturalGraphQl(closure: GraphQlPluginExtension.() -> Unit) {
    this.configure(closure)
}