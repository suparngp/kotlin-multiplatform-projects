package extensions

import java.time.ZonedDateTime
import java.util.*

open class BintrayExtension {
    var publish = false
    var repository = ""
    var username = ""
    var apiKey = ""
    var vcsUrl = ""
    var versionLabel = ""
    var description = ""
    var licenses = emptyArray<String>()
    var publishDate: ZonedDateTime =  ZonedDateTime.now()

    override fun toString(): String {
        return "SuparnaturalBintrayExtension(publish=$publish, repository='$repository', username='$username', apiKey='$apiKey', vcsUrl='$vcsUrl', versionLabel='$versionLabel', description='$description', licenses=${Arrays.toString(licenses)}, publishDate=$publishDate)"
    }


}