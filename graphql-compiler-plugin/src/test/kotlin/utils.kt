package com.suparnatural.plugin.graphql.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.suparnatural.plugin.graphql.models.Container

val Json = ObjectMapper().registerModule(KotlinModule())!!
fun container(fileName: String): Container {
    val json = ClassLoader.getSystemClassLoader().getResource(fileName).readText()
    return Json.readValue<Container>(json)
}