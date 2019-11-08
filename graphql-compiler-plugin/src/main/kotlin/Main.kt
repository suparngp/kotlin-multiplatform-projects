import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.suparnatural.core.graphql.GraphQlOperation
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.Container
import com.suparnatural.plugin.graphql.processors.processFragments
import com.suparnatural.plugin.graphql.processors.processOperations
import com.suparnatural.plugin.graphql.processors.processTypes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File

val Json = ObjectMapper().registerKotlinModule()


enum class Test {
    @SerialName("one")
    One,
    Two
}

class MyOp(override val query: String, override val variables: Map<String, Any>, override val operationName: String) : GraphQlOperation() {

}

@Serializable
data class Co(val test: Test)
fun main(args: Array<String>) {
//    val json = ClassLoader.getSystemClassLoader().getResource("input.json").readText()
//    val all = Json.readValue<Container>(json)
////    println(all)
//
//    println(processTypes(all.typesUsed, SuparnaturalGraphqlExtension))
//    processOperations(all.operations, SuparnaturalGraphqlExtension).writeTo(File("src/main/kotlin"))
//    processFragments(all.fragments, SuparnaturalGraphqlExtension).writeTo(File("src/main/kotlin"))
//    val x = kotlinx.serialization.json.Json.nonstrict.parse(Co.serializer(), "{\"test\": \"one\"}")

//    println(x)
    val variables = mapOf<String, Any>("one" to 1, "two" to "2", "bool" to false )
    val operationName = "name"
    val source = "source"
    val o = MyOp(source, variables, operationName)
    println(o.serialize())
}