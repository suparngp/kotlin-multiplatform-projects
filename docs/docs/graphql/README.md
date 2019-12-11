# Introduction
`suparnatural-graphql` is a strongly-typed GraphQL client for `Android`, `iOS` and `JVM` applications written in [Kotlin Multiplatform](). It generates
type safe model classes from operations so that you don't have to deal with parsing JSON or raw data structures. It provides extensibility via Links inspired 
by [Apollo-Link]() project and makes no assumptions about the network transport.

For example, the following query

```graphql{5,13,20}
# Write your query or mutation here
query CountryCodeQuery($code: String!) {
  country(code: $code) {
    __typename
    code
    ...CountryDetails
    continent {
      ...ContinentDetails
    }
  }
}

fragment ContinentDetails on Continent {
  name
  countries {
    ...CountryDetails
  }
}

fragment CountryDetails on Country {
  name
}
```
can be used as 

```kotlin{5-7}
val operation = Operations.CountryCodeQuery("AD")
client.execute(operation).subscribe {
    when(it) {
        is Result.Success -> {
            println(it.value.data?.country?.code)
            println(it.value.data?.country?.fragments.continentDetails)
            println(it.value.data?.country?.fragments.continentDetails?.countries?.fragments.countryDetails)
        }
    }
}
```



## Motivation
`GraphQL` provides a powerful and flexible way of fetching data from your server. The exact structure of a query response is known ahead of time as the expected
fields are a part of the query and the field types are defined in the `schema`. However, rich API data models can be complex and are hard to manage on a client
without type safety. For example, without strict types, you may end up parsing a JSON into a map and then access the values without any type checking. This is 
both very risky and not developer friendly. This is not a new problem as far as APIs are concerned and there are many solutions which solve such a problem elegantly
by generating a strictly typed data model and then mapping a raw `JSON` response over to that model. 

Next, a common pattern found across many API clients is to bundle a network transport in the library. It is a good way to get started. However, it becomes
very limiting if you are building complex applications which require features like `Certificate Pinning` where you must control the transport layer of API call stack.
Another downside of such an approach is that your application may even end up with two clients for making network requests where one takes care of `GraphQL` and another
client like `Alamofire` or `OkHttp` which deal with other `REST` endpoints (`OAuth` for example). And, they both may not even have the same level of customization.
Of course it is a personal opinion whether you consider it as a good or a bad thing.

`suparnatural-graphql` aims to solve these problems. It generates strictly type safe `Kotlin` classes by deriving information from the `GraphQL` schema and
the operations used in the client application. It uses [kotlinx.serialization]() to parse `JSON`. However, the implementation of various parts of this library
can be customized to support other protocols like `ProtoBuf` if needed. You are also free to implement your own network transport however way you see fit.

## Features

1. Generates strictly typed classes for all your `GraphQL` operations.
2. Build reusable steps to process an operation before it goes over the wire by using a `chain` of `Link` instances.
3. Use your own transport to make network requests. For example, use multiplatform client like [ktor-client]() or a native client like [Alamofire]() and [OkHttp]().
4. Customize / Extend default implementations of various parts of the library.

## Architecture

`suparnatural-graphql` is comprised of two parts and **cannot** be used without each other.

1. A `Gradle plugin` which generates strictly typed model classes from `GraphQL` operations.
2. A library which provides API and implementations for `GraphQL` client, `Link` and other things.

The plugin is inspired by the build script part of integrating [apollo-ios](https://github.com/apollographql/apollo-ios) 
and also uses the same [apollo-tooling](https://github.com/apollographql/apollo-tooling) to pull `schema` and generate an intermediate
`JSON` structure which can then be translated to type safe classes.

