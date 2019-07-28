package com.suparnatural.plugin.graphql.processors

import com.squareup.kotlinpoet.TypeSpec
import com.suparnatural.plugin.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugin.graphql.test.container
import org.junit.jupiter.api.Test
import java.io.File

internal class TypeProcessorKtTest {
    @Test
    fun processType() {
        val c = container("input.json")
        val types = processTypes(c.typesUsed, SuparnaturalGraphqlExtension)
        types.writeTo(File("output.kt"))
//        assertNotNull(types)
//

//        val x = TypeSpec.classBuilder("Hello")
//                .addProperty("One", String::class)
//        val a = x.build()
//        x.addProperty("Two", String::class)
//        val b = x.build()
//        println(a)
//        println(b)
    }
}