import constants.SourceSetNames
import constants.TargetDependencies
import org.gradle.api.Project
import constants.Plugins

fun Project.configureMultiplatform() {
    plugins.apply(Plugins.multiplatform.id)
    kmpKotlin.apply {

        sourceSets.apply {
            this.getByName(SourceSetNames.commonMain).apply {
                dependencies {
                    add(TargetDependencies.Common.main)
                }
            }
            getByName(SourceSetNames.commonTest).apply {
                dependencies {
                    add(TargetDependencies.Common.test)
                }
            }
        }
    }
}