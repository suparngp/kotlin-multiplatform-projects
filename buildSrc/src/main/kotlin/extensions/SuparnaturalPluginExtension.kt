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
    val androidMain = SourceSetExtension(AndroidMainDefaultDependencies::class)
    val androidTest = SourceSetExtension(AndroidTestDefaultDependencies::class)

    val commonMain = SourceSetExtension(CommonMainDefaultDependencies::class)
    val commonTest = SourceSetExtension(CommonTestDefaultDependencies::class)

    val iosX64Main = SourceSetExtension(IosX64MainDefaultDependencies::class)
    val iosX64Test = SourceSetExtension(IosX64TestDefaultDependencies::class)

    val iosArm64Main = SourceSetExtension(IosArm64MainDefaultDependencies::class)
    val iosArm64Test = SourceSetExtension(IosArm64TestDefaultDependencies::class)

    fun bintray(closure: BintrayExtension.() -> Unit) {
        bintray.licenses = arrayOf(license)
        bintray.description = description
        bintray.vcsUrl = vcsUrl
        bintray.versionLabel = versionLabel
        bintray.apply(closure)
    }
}
