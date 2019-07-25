import constants.SourceSetNames
import constants.TargetNames
import extensions.SuparnaturalPluginExtension
import org.gradle.api.Project

fun Project.configureJvm(config: SuparnaturalPluginExtension) {
    kmpKotlin.apply {
        jvm(TargetNames.jvm)
    }

    configureKmpSourceSet(SourceSetNames.jvmMain, config.jvmMain)
    configureKmpSourceSet(SourceSetNames.jvmTest, config.jvmTest)
}