import constants.Plugins
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

data class CocoapodsConfig(val summary: String, val homepage: String)

fun Project.configureCocoapods(config: CocoapodsConfig) {
    plugins.apply(Plugins.cocoapods.id)
    val cocoapods = (kmpKotlin as ExtensionAware).extensions.getByType(CocoapodsExtension::class.java)
    cocoapods.apply {
        summary = config.summary
        homepage = config.homepage
    }
}