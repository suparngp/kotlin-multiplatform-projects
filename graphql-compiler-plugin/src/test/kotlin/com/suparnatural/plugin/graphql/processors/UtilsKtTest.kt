package com.suparnatural.plugin.graphql.processors

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UtilsKtTest {
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
            "String, kotlin.String?",
            "String!, kotlin.String",
            "[String], kotlin.collections.List<kotlin.String?>?",
            "[String]!, kotlin.collections.List<kotlin.String?>",
            "[String!]!, kotlin.collections.List<kotlin.String>",
            "[String!], kotlin.collections.List<kotlin.String>?"
    )
    fun propertySpecType(input: String, expected: String) {
        Assertions.assertEquals(expected, propertyTypeName(input, KnownTypes).toString())
    }
}