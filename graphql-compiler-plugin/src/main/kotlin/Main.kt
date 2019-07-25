import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.suparnatural.plugin.graphql.models.Fragment
import com.suparnatural.plugin.graphql.models.Operation
import com.suparnatural.plugin.graphql.models.TypeUsed
import java.io.File

val Json = ObjectMapper().registerKotlinModule()

@JsonIgnoreProperties(ignoreUnknown = false)
data class Container(val operations: List<Operation>, val fragments: List<Fragment>, val typesUsed: List<TypeUsed>)

fun main(args: Array<String>) {
    val json = ClassLoader.getSystemClassLoader().getResource("input.json").readText()
    val all = Json.readValue<Container>(json)
    println(all.operations)
}