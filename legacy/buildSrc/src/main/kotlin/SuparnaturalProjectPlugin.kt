import extensions.SuparnaturalPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


class SuparnaturalProjectPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("__suparnatural", SuparnaturalPluginExtension::class.java)
        target.configureCommon()
    }
}

fun Project.suparnatural(callback: (SuparnaturalPluginExtension.() -> Unit)) {
    val config = extensions.getByType(SuparnaturalPluginExtension::class.java).apply(callback)
    val isRelease = hasReleaseTask()

    configureMultiplatform(config)
    configureDocs(config.name)

    if (config.supportsJvm) {
        configureJvm(config)
    }

    if (config.supportsAndroid) {
        configureAndroid(config)
    }

    if (config.supportsIos) {
        configureIos(config, isRelease)
    }

    if (config.supportsCocoapods) {
        configureCocoapods(CocoapodsConfig(config.description, config.docsUrl, config.license, "suparnatural"))
    }

    if (config.bintray.publish) {
        configureBintray(config.bintray)
    }

    configureReleaseTask()
}