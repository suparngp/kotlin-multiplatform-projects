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
import java.lang.Exception

open class GraphQlPluginExtension {
    var packageName: String = ""
    var schemaJsonFilePath: String = ""
    var outputDirectoryPath: String = ""
}

val Json = ObjectMapper().registerKotlinModule()

class GraphQlPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("suparnaturalGraphQl", GraphQlPluginExtension::class.java)

        target.task("hello") {
            doLast {
                val schemaFile = target.file(extension.schemaJsonFilePath)
                if (!schemaFile.exists()) {
                    throw Exception("SuparnaturalGraphQlPlugin: Schema file not found at path ${schemaFile.absolutePath}")
                }
                val json = Json.readValue(schemaFile, Container::class.java)

                val outputDirectory = target.file(extension.outputDirectoryPath)
                processTypes(json.typesUsed, extension).writeTo(outputDirectory)
                processOperations(json.operations, extension).writeTo(outputDirectory)
                processFragments(json.fragments, extension).writeTo(outputDirectory)
                println("Executing task hello>>>>>>> ${extension.packageName} ${outputDirectory.absolutePath}")
            }
        }
    }
}

fun Project.suparnaturalGraphQl(closure: GraphQlPluginExtension.() -> Unit) {
    this.configure(closure)
}