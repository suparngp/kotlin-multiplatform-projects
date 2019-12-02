import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.observable.subscribe
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.suparnatural.core.graphql.*
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.models.Container
import com.suparnatural.plugin.graphql.processors.processFragments
import com.suparnatural.plugin.graphql.processors.processOperations
import com.suparnatural.plugin.graphql.processors.processTypes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File
import java.lang.annotation.Native

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
//////    println(all)
////
////    println(processTypes(all.typesUsed, SuparnaturalGraphqlExtension))
//    processOperations(all.operations, SuparnaturalGraphqlExtension).writeTo(File("src/main/kotlin"))
//    processFragments(all.fragments, SuparnaturalGraphqlExtension).writeTo(File("src/main/kotlin"))
//    val x = kotlinx.serialization.json.Json.nonstrict.parse(Co.serializer(), "{\"test\": \"one\"}")

//    println(x)
//    val variables = mapOf<String, Any>("one" to 1, "two" to "2", "bool" to false )
//    val operationName = "name"
//    val source = "source"
////    val o = MyOp(source, variables, operationName)
//
//
//    val r = GraphQlOperation.Builder(source, variables, operationName, mapOf("inital" to "context"))
//            .build()
//
//    val link1 = Link(false) {request, forward ->
//        println("Link 1")
//        val newRequest = GraphQlRequest.Builder(request).setContextValue("First", "value").build()
//        forward?.invoke(newRequest) ?: observableOf(null)
//    }
//
//    val link2 = Link(false) {request, forward ->
//        println("link 2")
//        println(request.getContext())
//        forward?.invoke(request) ?: observableOf(null)
//    }
//
//    val link = concatLinks(arrayOf(link1, link2))
//    link.execute(r) {
//        println("original forward")
//        observableOf(null)
//    }

//    val firstLink = StringGeneratorLink()
//    val secondLink = StringToIntLink()
//    val thirdLink = EvenOddLink();
//    val finalLink = thirdLink.concat(secondLink.concat(firstLink))
//    finalLink.execute(Unit, null).subscribe {
//        println(it)
//    }


    val link = GraphQlHttpLink(NativeFetcher(), GraphQlHttpLink.Options(""))
    val client = GraphQlClient(link)
}