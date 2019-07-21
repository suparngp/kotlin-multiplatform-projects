import constants.ProjectConfig
import constants.TaskNames
import org.gradle.api.Project

fun Project.configureReleaseTask() {

    afterEvaluate {
        tasks.create(TaskNames.release) {
            findProperty(ProjectConfig.Properties.environment)?.toString() == ProjectConfig.PropertyValue.environmentRelease ?: return@create
            println("disabling")
            gradle.taskGraph.whenReady {
                val test = tasks.getByName(TaskNames.test)
                if (hasTask(test)) {
                    test.enabled = false
                }
            }

            doFirst {

            }
            group = "Build"
            description = "Builds, tests and releases the artifact to bintray."
            dependsOn(tasks.getByName(TaskNames.clean))
            dependsOn(tasks.getByName(TaskNames.build))
            dependsOn(tasks.getByName(TaskNames.androidTest))
            dependsOn(tasks.getByName(TaskNames.iosTest))
            dependsOn(tasks.getByName(TaskNames.publishToMavenLocal))
        }
    }


}