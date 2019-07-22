import constants.SourceSetNames
import constants.DefaultDependencies
import org.gradle.api.Project
import constants.Plugins
import extensions.SuparnaturalExtension

fun Project.configureMultiplatform(config: SuparnaturalExtension) {
    plugins.apply(Plugins.multiplatform.id)
    configureKmpSourceSet(SourceSetNames.commonMain, config.commonMain)
    configureKmpSourceSet(SourceSetNames.commonTest, config.commonTest)
}