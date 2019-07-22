package extensions

open class SuparnaturalPluginExtension {
    var description = ""
    var docsUrl = ""
    var vcsUrl = ""
    var supportsCocoapods = false
    var supportsAndroid = false
    var supportsIos = false
    var versionLabel = ""
    var buildNumber = 1
    var license = "MIT"
    var bintray = BintrayExtension()
        private set
    var androidMain = SourceSetExtension(AndroidMainDependenciesExtension::class)
    var androidTest = SourceSetExtension(AndroidTestDependenciesExtension::class)

    var commonMain = SourceSetExtension(CommonMainDependenciesExtension::class)
    var commonTest = SourceSetExtension(CommonTestDependenciesExtension::class)

//    fun bintray(callback: BintrayExtension.() -> Unit) {
//        bintray.versionLabel = versionLabel
//        bintray.description = description
//        bintray.vcsUrl = vcsUrl
//        bintray.licenses = arrayOf(license)
//        bintray.apply(callback)
//    }
}
