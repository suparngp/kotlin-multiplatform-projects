import constants.DefaultDependencies
import constants.Dependencies
import constants.Plugins
import constants.ProjectConfig
import extensions.SuparnaturalExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance



class SuparnaturalProjectPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("__suparnatural", SuparnaturalExtension::class.java)
        target.configureCommon()
    }
}

fun Project.suparnatural(callback: (SuparnaturalExtension.() -> Unit)) {
    val config = extensions.getByType(SuparnaturalExtension::class.java).apply(callback)
    val isRelease = hasReleaseTask()

    configureMultiplatform(config)
    configureDocs()


    if (config.supportsAndroid) {
        configureAndroid(config)
    }

    if (config.supportsIos) {
        configureIos(isRelease)
    }

    if (config.supportsCocoapods) {
        configureCocoapods(CocoapodsConfig(config.description, config.docsUrl, config.license, "suparnatural"))
    }

    if (config.bintray.publish) {
        configureBintray(config.bintray)
    }

    configureReleaseTask()
}