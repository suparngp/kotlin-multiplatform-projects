import constants.PluginNames
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

open class SuparnaturalExtension {
    var summary: String = ""
    var homepage: String = ""
    var supportsCocoapods: Boolean = false
    var supportsAndroid: Boolean = false
    var supportsIos: Boolean = false
    override fun toString(): String {
        return "SuparnaturalExtension(summary='$summary', homepage='$homepage', supportsCocoapods=$supportsCocoapods, supportsAndroid=$supportsAndroid, supportsIos=$supportsIos)"
    }


}

class SuparnaturalProjectPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("__suparnatural", SuparnaturalExtension::class.java)
        target.plugins.apply(PluginNames.multiplatform)
        val kotlin = target.extensions.getByType(KotlinMultiplatformExtension::class.java)
        target.configureCommon(kotlin)
    }


}

fun Project.suparnatural(callback: (SuparnaturalExtension.() -> Unit)) {
    val config = extensions.getByType(SuparnaturalExtension::class.java).apply(callback)
    val kotlin = extensions.getByType(KotlinMultiplatformExtension::class.java)
    println(config)
    if (config.supportsCocoapods) {
        configureCocoapods(kotlin, CocoapodsConfig(config.summary, config.homepage))
    }

    if (config.supportsAndroid) {
        configureAndroid(kotlin)
    }

    if (config.supportsIos) {
        configureIos(kotlin)
    }

}