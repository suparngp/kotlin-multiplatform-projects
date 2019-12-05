package com.suparnatural.plugins.graphql

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

open class GraphQlPluginExtension {
    var name = "String"
}

class GraphQlPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("suparnaturalGraphQl", GraphQlPluginExtension::class.java)
        target.task("hello") {
            doLast {
                println("Executing task hello>>>>>>> ${extension.name}")
            }
        }
    }
}

fun Project.suparnaturalGraphQl(closure: GraphQlPluginExtension.() -> Unit) {
    this.configure(closure)
}