package com.suparnatural.plugins.graphql

import org.gradle.api.Plugin
import org.gradle.api.Project

class SuparnaturalGraphQlPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.task("hello") {
            it.doLast {
                println("Executing task hello>>>>>>>")
            }
        }
    }

}