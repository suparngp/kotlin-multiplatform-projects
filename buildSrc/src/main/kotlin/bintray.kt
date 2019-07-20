import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import constants.Plugins
import constants.TaskNames
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.delegateClosureOf
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

open class SuparnaturalBintrayExtension {
    var publish = false
    var repository = ""
    var username = ""
    var apiKey = ""
    var vcsUrl = ""
    var versionLabel = ""
    var description = ""
    var licenses = arrayOf("MIT")
    var publishDate:  ZonedDateTime =  ZonedDateTime.now()

    override fun toString(): String {
        return "SuparnaturalBintrayExtension(publish=$publish, repository='$repository', username='$username', apiKey='$apiKey', vcsUrl='$vcsUrl', versionLabel='$versionLabel', description='$description', licenses=${Arrays.toString(licenses)}, publishDate=$publishDate)"
    }


}

fun Project.configureBintray(config: SuparnaturalBintrayExtension) {
    plugins.apply(Plugins.mavenPublish.id)
    plugins.apply(Plugins.bintray.id)
    val datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"
    val publishing = extensions.getByType(PublishingExtension::class.java)

    tasks.withType(BintrayUploadTask::class.java).configureEach {
        dependsOn(tasks.named(TaskNames.publishToMavenLocal))

        doFirst {
            setPublications(publishing.publications.map { it.name })
        }
    }

    val bintray = extensions.getByType(BintrayExtension::class.java)
    val projectName = name

    bintray.apply {
        user = config.username
        key = config.apiKey
        isOverride = true
        isPublish = true
        pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
            repo = config.repository
            name = projectName
            vcsUrl = config.vcsUrl
            setLicenses(*config.licenses)
            version(delegateClosureOf<BintrayExtension.VersionConfig> {
                name = config.versionLabel
                desc = config.description
                vcsTag = config.versionLabel
                released = config.publishDate.format(DateTimeFormatter.ofPattern(datePattern))
            })
        })
    }
}

