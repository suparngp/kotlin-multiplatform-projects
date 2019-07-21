import constants.Plugins
import org.gradle.api.Plugin
import org.gradle.api.Project


open class SuparnaturalExtension {
    var description = ""
    var docsUrl = ""
    var supportsCocoapods = false
    var supportsAndroid = false
    var supportsIos = false
    var bintray = SuparnaturalBintrayExtension()
        private set

    fun bintray(callback: SuparnaturalBintrayExtension.() -> Unit) {
        bintray.apply(callback)
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

    configureMultiplatform()
    configureDocs()


    if (config.supportsAndroid) {
        configureAndroid()
    }

    if (config.supportsIos) {
        configureIos()
    }

    if (config.supportsCocoapods) {
        configureCocoapods(CocoapodsConfig(config.description, config.docsUrl))
    }

    if (config.bintray.publish) {
        configureBintray(config.bintray)
    }
}