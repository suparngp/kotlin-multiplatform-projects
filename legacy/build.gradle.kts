apply("credentials.gradle.kts")
plugins {
    base
    java
}

group = "com.suparnatural"
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}