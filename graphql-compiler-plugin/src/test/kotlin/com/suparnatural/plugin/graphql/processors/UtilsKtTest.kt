package com.suparnatural.plugin.graphql.processors

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UtilsKtTest {

    @ParameterizedTest
    @CsvSource(
            "String, String?",
            "String!, String",
            "[String], List<String?>?",
            "[String]!, List<String?>",
            "[String!], List<String>?",
            "[String!]!, List<String>"
    )
    fun adjustType(input: String, expected: String) {
        Assertions.assertEquals(expected, com.suparnatural.plugin.graphql.processors.propertyType(input))
    }

    @ParameterizedTest
    @CsvSource(
            "A_B, AB",
            "Ab_cd, AbCd",
            "ab_cd, AbCd",
            "ab, Ab",
            "aC_cD, AcCd"

    )
    fun snakeToPascal(input: String, expected: String) {
        Assertions.assertEquals(expected, com.suparnatural.plugin.graphql.processors.snakeToPascal(input))
    }

    @ParameterizedTest
    @CsvSource(
            "String, String?",
            "String!, String",
            "[String], kotlin.collections.List<String?>?",
            "[String]!, kotlin.collections.List<String?>",
            "[String!]!, kotlin.collections.List<String>",
            "[String!], kotlin.collections.List<String>?"
    )
    fun propertySpecType(input: String, expected: String) {
        Assertions.assertEquals(expected, propertySpecType(input).toString())
    }
}