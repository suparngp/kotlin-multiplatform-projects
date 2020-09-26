import constants.SourceSetNames
import org.gradle.api.Project
import constants.Plugins
import extensions.SuparnaturalPluginExtension

fun Project.configureMultiplatform(config: SuparnaturalPluginExtension) {
    plugins.apply(Plugins.multiplatform.id)
    configureKmpSourceSet(SourceSetNames.commonMain, config.commonMain)
    configureKmpSourceSet(SourceSetNames.commonTest, config.commonTest)
}