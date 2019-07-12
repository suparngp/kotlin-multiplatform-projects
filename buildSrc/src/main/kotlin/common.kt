import constants.SourceSetNames
import constants.TargetDependencies
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.gradle.kotlin.dsl.getting

fun Project.configureCommon(kotlin: KotlinMultiplatformExtension) {
    kotlin.apply {

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