package com.suparnatural.plugins.graphql.processors

import com.squareup.kotlinpoet.*
import com.suparnatural.plugins.graphql.config.SuparnaturalGraphqlExtension
import com.suparnatural.plugins.graphql.models.Fragment


fun processFragments(fragments: List<Fragment>, config: SuparnaturalGraphqlExtension): FileSpec {
    val fileSpec = FileSpec.builder(config.packageName, FragmentsContainer)

    val containerInterface = TypeSpec.interfaceBuilder(ClassName(config.packageName,
        FragmentsContainer
    ))

    fragments.forEach { fragment ->
        processFieldGroup(fragment, containerInterface, config, true)
    }
    return fileSpec
            .addType(containerInterface.build())
            .build()
}