subprojects {
    this.extra["bintrayUsername"] = System.getenv("BINTRAY_USERNAME") ?: ""
    this.extra["bintrayApiKey"] = System.getenv("BINTRAY_API_KEY") ?: ""
    this.extra["bintrayRepository"] = "kotlin-multiplatform"
}