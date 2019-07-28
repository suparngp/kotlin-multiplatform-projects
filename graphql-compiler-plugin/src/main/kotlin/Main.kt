import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val Json = ObjectMapper().registerKotlinModule()


enum class Test {
    @SerialName("one")
    One,
    Two
}

@Serializable
data class Co(val test: Test)
fun main(args: Array<String>) {
//    val json = ClassLoader.getSystemClassLoader().getResource("input.json").readText()
//    val all = Json.readValue<Container>(json)
//    println(all.operations)

    val x = kotlinx.serialization.json.Json.nonstrict.parse(Co.serializer(), "{\"test\": \"one\"}")
    println(x)
}