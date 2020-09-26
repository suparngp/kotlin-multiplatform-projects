import constants.TaskNames
import org.gradle.api.Project

fun Project.configureReleaseTask() {
    val isRelease = hasReleaseTask()

    afterEvaluate {
        tasks.create(TaskNames.release) {
            onlyIf { isRelease }
            gradle.taskGraph.whenReady {
                tasks.all {
                    if (name.startsWith(TaskNames.test)) {
                        enabled = false
                    }
                }
            }

            group = "Build"
            description = "Builds, tests and releases the artifact to bintray."
            val clean = tasks.getByName(TaskNames.clean)
            val build = tasks.getByName(TaskNames.build)
            val androidTest = tasks.getByName(TaskNames.androidTest)
            val iosTest = tasks.getByName(if (isRelease) TaskNames.iosX64Test else TaskNames.iosTest)
            val publishToMavenLocal = tasks.getByName(TaskNames.publishToMavenLocal)
            val bintrayUpload = tasks.getByName(TaskNames.bintrayUpload)

            val tasks = arrayOf(clean, build, androidTest, iosTest, publishToMavenLocal, bintrayUpload)
            for (x in 0 until tasks.size - 1) {
                tasks[x + 1].shouldRunAfter(tasks[x])
            }
            dependsOn(*tasks)
        }
    }


}