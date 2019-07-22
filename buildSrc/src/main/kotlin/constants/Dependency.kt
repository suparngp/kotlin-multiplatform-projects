package constants


class Dependency(group: String, name: String, version: String, val type: Type): Artifact(group, name, version) {
    enum class Type(val value: String) {
        Api("api"),
        Implementation("implementation")
    }
}