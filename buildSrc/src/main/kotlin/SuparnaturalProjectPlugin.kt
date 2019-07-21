import constants.Plugins
import constants.ProjectConfig
import org.gradle.api.Plugin
import org.gradle.api.Project


open class SuparnaturalExtension {
    var description = ""
    var docsUrl = ""
    var vcsUrl = ""
    var supportsCocoapods = false
    var supportsAndroid = false
    var supportsIos = false
    var versionLabel = ""
    var bintray = SuparnaturalBintrayExtension()
        private set

    fun bintray(callback: SuparnaturalBintrayExtension.() -> Unit) {
        bintray.versionLabel = versionLabel
        bintray.description = description
        bintray.vcsUrl = vcsUrl
        bintray.apply(callback)
        println(this.bintray)
    }

}

class SuparnaturalProjectPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("__suparnatural", SuparnaturalExtension::class.java)
        target.configureCommon()
    }
}

fun Project.suparnatural(callback: (SuparnaturalExtension.() -> Unit)) {
    val config = extensions.getByType(SuparnaturalExtension::class.java).apply(callback)

    val environment = findProperty(ProjectConfig.Properties.environment)?.toString()

    configureMultiplatform()
    configureDocs()


    if (config.supportsAndroid) {
        configureAndroid()
    }

    if (config.supportsIos) {
        configureIos(environment == ProjectConfig.PropertyValue.environmentRelease)
    }

    if (config.supportsCocoapods) {
        configureCocoapods(CocoapodsConfig(config.description, config.docsUrl))
    }

    if (config.bintray.publish) {
        configureBintray(config.bintray)
    }

    configureReleaseTask()
}