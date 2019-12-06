package com.suparnatural.plugins.graphql.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = false)
data class Container(val operations: List<Operation>, val fragments: List<Fragment>, val typesUsed: List<TypeUsed>)
