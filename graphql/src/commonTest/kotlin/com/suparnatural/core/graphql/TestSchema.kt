package com.suparnatural.core.graphql

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.collections.set
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

interface Fragments {
    @Serializable
    interface ContinentDetails {
        val __typename: String

        val name: String?

        val countries: List<Country.CountryFragmentsAdapter?>?

        @Serializable
        data class ContinentDetailsImpl(
                override val __typename: String,
                override val name: String?,
                override val countries: List<Country.CountryFragmentsAdapter?>?
        ) : ContinentDetails

        @Serializer(forClass = ContinentDetails::class)
        companion object {
            override val descriptor: SerialDescriptor = ContinentDetailsImpl.serializer().descriptor

            override fun serialize(encoder: Encoder, obj: ContinentDetails) =
                    ContinentDetailsImpl.serializer().serialize(encoder, obj as ContinentDetailsImpl)

            override fun deserialize(decoder: Decoder): ContinentDetails =
                    ContinentDetailsImpl.serializer().deserialize(decoder)
        }

        @Serializable
        interface Country {
            val __typename: String

            @Serializable
            data class CountryImpl(
                    override val __typename: String
            ) : Country

            @Serializable(with = CountryFragmentsAdapter.Companion::class)
            data class CountryFragmentsAdapter(
                    val delegate: Country,
                    val fragments: FragmentsGroup = FragmentsGroup()
            ) : Country by delegate {
                companion object : KSerializer<CountryFragmentsAdapter> {
                    @InternalSerializationApi
                    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Country", PrimitiveKind.STRING)


                    override fun serialize(encoder: Encoder, obj: CountryFragmentsAdapter) {
                        val jsonObjects =
                                mutableListOf<JsonElement>(Json { ignoreUnknownKeys = true }.encodeToJsonElement(CountryImpl.serializer(),
                                        obj.delegate as CountryImpl).jsonObject)
                        if (obj.fragments.countryDetails != null) {
                            jsonObjects.add(Json { ignoreUnknownKeys = true }.encodeToJsonElement(CountryDetails.Companion,
                                    obj.fragments.countryDetails).jsonObject)
                        }
                        val jsonMap = mutableMapOf<String, JsonElement>()
                        jsonObjects.map { it.jsonObject }.forEach { jsonObject ->
                            jsonObject.keys.forEach { key ->
                                jsonMap[key] = jsonObject[key]!!
                            }
                        }
                        (encoder as JsonEncoder).encodeJsonElement(JsonObject(jsonMap))
                    }

                    override fun deserialize(decoder: Decoder): CountryFragmentsAdapter {
                        val json = (decoder as JsonDecoder).decodeJsonElement()
                        val delegate = Json { ignoreUnknownKeys = true }.decodeFromJsonElement(CountryImpl.serializer(), json)
                        val countryDetails = Json { ignoreUnknownKeys = true }.decodeFromJsonElement(CountryDetails.Companion, json)
                        return CountryFragmentsAdapter(delegate, FragmentsGroup(countryDetails))
                    }
                }
            }

            @Serializable
            data class FragmentsGroup(
                    val countryDetails: CountryDetails? = null
            )

            @Serializer(forClass = Country::class)
            companion object {
                override val descriptor: SerialDescriptor = CountryFragmentsAdapter.serializer().descriptor

                override fun serialize(encoder: Encoder, obj: Country) =
                        CountryFragmentsAdapter.serializer().serialize(encoder, obj as CountryFragmentsAdapter)

                override fun deserialize(decoder: Decoder): Country =
                        CountryFragmentsAdapter.serializer().deserialize(decoder)
            }
        }
    }

    @Serializable
    interface CountryDetails {
        val __typename: String

        val code: String?

        val name: String?

        @Serializable
        data class CountryDetailsImpl(
                override val __typename: String,
                override val code: String?,
                override val name: String?
        ) : CountryDetails

        @Serializer(forClass = CountryDetails::class)
        companion object {
            override val descriptor: SerialDescriptor = CountryDetailsImpl.serializer().descriptor

            override fun serialize(encoder: Encoder, obj: CountryDetails) =
                    CountryDetailsImpl.serializer().serialize(encoder, obj as CountryDetailsImpl)

            override fun deserialize(decoder: Decoder): CountryDetails =
                    CountryDetailsImpl.serializer().deserialize(decoder)
        }
    }
}


class Operations {
    class Countries() : GraphQlOperation<Operations.Countries.CountriesResponse>() {
        override val name: String = "Countries"

        override val responseSerializer: KSerializer<Operations.Countries.CountriesResponse> =
                Operations.Countries.CountriesResponse.serializer()

        override val source: String = """query Countries {
          countries {
            __typename
            code
          }
        }"""

        @Serializable override val variables: MutableMap<String, JsonElement?> = mutableMapOf()

        @Serializable
        data class CountriesResponse(
                val countries: List<Country?>?
        ) {
            @Serializable
            data class Country(
                    val __typename: String,
                    val code: String?
            )
        }
    }

    class CountryCodeQuery(
            val code: String
    ) : GraphQlOperation<Operations.CountryCodeQuery.CountryCodeQueryResponse>() {
        override val name: String = "CountryCodeQuery"

        override val source: String = """query CountryCodeQuery(${"$"}code: String!) {
          country(code: ${"$"}code) {
            __typename
            ...CountryDetails
            continent {
              __typename
              ...ContinentDetails
            }
          }
        }
        fragment CountryDetails on Country {
          __typename
          code
          name
        }
        fragment ContinentDetails on Continent {
          __typename
          name
          countries {
            __typename
            ...CountryDetails
          }
        }"""

        override val variables: MutableMap<String, JsonElement?> = mutableMapOf("code" to Json.decodeFromString(Json.encodeToString(code)))

        override val responseSerializer:
                KSerializer<Operations.CountryCodeQuery.CountryCodeQueryResponse> =
                Operations.CountryCodeQuery.CountryCodeQueryResponse.serializer()

        @Serializable
        data class CountryCodeQueryResponse(
                val country: Country.CountryFragmentsAdapter?
        ) {
            @Serializable
            interface Country {
                val __typename: String

                val continent: Continent.ContinentFragmentsAdapter?

                @Serializable
                data class CountryImpl(
                        override val __typename: String,
                        override val continent: Continent.ContinentFragmentsAdapter?
                ) : Country

                @Serializable(with = CountryFragmentsAdapter.Companion::class)
                data class CountryFragmentsAdapter(
                        val delegate: Country,
                        val fragments: FragmentsGroup = FragmentsGroup()
                ) : Country by delegate {
                    companion object : KSerializer<CountryFragmentsAdapter> {
                        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Country", PrimitiveKind.STRING)


                        override fun serialize(encoder: Encoder, obj: CountryFragmentsAdapter) {
                            val jsonObjects =
                                    mutableListOf<JsonElement>(Json { ignoreUnknownKeys = true }.encodeToJsonElement(CountryImpl.serializer(),
                                            obj.delegate as CountryImpl).jsonObject)
                            if (obj.fragments.countryDetails != null) {
                                jsonObjects.add(Json{ignoreUnknownKeys = true}.encodeToJsonElement(Fragments.CountryDetails.Companion,
                                        obj.fragments.countryDetails).jsonObject)
                            }
                            val jsonMap = mutableMapOf<String, JsonElement>()
                            jsonObjects.map { it.jsonObject }.forEach { jsonObject ->
                                jsonObject.keys.forEach { key ->
                                    jsonMap[key] = jsonObject[key]!!
                                }
                            }
                            (encoder as JsonEncoder).encodeJsonElement(JsonObject(jsonMap))
                        }

                        override fun deserialize(decoder: Decoder): CountryFragmentsAdapter {
                            val json = (decoder as JsonDecoder).decodeJsonElement()
                            val delegate = Json { ignoreUnknownKeys = true }.decodeFromJsonElement(CountryImpl.serializer(), json)
                            val countryDetails = Json { ignoreUnknownKeys = true }.decodeFromJsonElement(Fragments.CountryDetails.Companion, json)
                            return CountryFragmentsAdapter(delegate, FragmentsGroup(countryDetails))
                        }
                    }
                }

                @Serializable
                data class FragmentsGroup(
                        val countryDetails: Fragments.CountryDetails? = null
                )

                @Serializer(forClass = Country::class)
                companion object {
                    override val descriptor: SerialDescriptor =
                            CountryFragmentsAdapter.serializer().descriptor

                    override fun serialize(encoder: Encoder, obj: Country) =
                            CountryFragmentsAdapter.serializer().serialize(encoder, obj as
                                    CountryFragmentsAdapter)

                    override fun deserialize(decoder: Decoder): Country =
                            CountryFragmentsAdapter.serializer().deserialize(decoder)
                }

                @Serializable
                interface Continent {
                    val __typename: String

                    @Serializable
                    data class ContinentImpl(
                            override val __typename: String
                    ) : Continent

                    @Serializable(with = ContinentFragmentsAdapter.Companion::class)
                    data class ContinentFragmentsAdapter(
                            val delegate: Continent,
                            val fragments: FragmentsGroup = FragmentsGroup()
                    ) : Continent by delegate {
                        companion object : KSerializer<ContinentFragmentsAdapter> {
                            override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Continent", PrimitiveKind.STRING)


                            override fun serialize(encoder: Encoder, obj: ContinentFragmentsAdapter) {
                                val jsonObjects =
                                        mutableListOf<JsonElement>(Json { ignoreUnknownKeys = true }.encodeToJsonElement(ContinentImpl.serializer(),
                                                obj.delegate as ContinentImpl).jsonObject)
                                if (obj.fragments.continentDetails != null) {
                                    jsonObjects.add(Json { ignoreUnknownKeys = true }.encodeToJsonElement(Fragments.ContinentDetails.Companion,
                                            obj.fragments.continentDetails).jsonObject)
                                }
                                val jsonMap = mutableMapOf<String, JsonElement>()
                                jsonObjects.map { it.jsonObject }.forEach { jsonObject ->
                                    jsonObject.keys.forEach { key ->
                                        jsonMap[key] = jsonObject[key]!!
                                    }
                                }
                                (encoder as JsonEncoder).encodeJsonElement(JsonObject(jsonMap))
                            }

                            override fun deserialize(decoder: Decoder): ContinentFragmentsAdapter {
                                val json = (decoder as JsonDecoder).decodeJsonElement()
                                val delegate = Json { ignoreUnknownKeys = true }.decodeFromJsonElement(ContinentImpl.serializer(), json)
                                val continentDetails = Json { ignoreUnknownKeys = true }.decodeFromJsonElement(Fragments.ContinentDetails.Companion,
                                        json)
                                return ContinentFragmentsAdapter(delegate, FragmentsGroup(continentDetails))
                            }
                        }
                    }

                    @Serializable
                    data class FragmentsGroup(
                            val continentDetails: Fragments.ContinentDetails? = null
                    )

                    @Serializer(forClass = Continent::class)
                    companion object {
                        override val descriptor: SerialDescriptor =
                                ContinentFragmentsAdapter.serializer().descriptor

                        override fun serialize(encoder: Encoder, obj: Continent) =
                                ContinentFragmentsAdapter.serializer().serialize(encoder, obj as
                                        ContinentFragmentsAdapter)

                        override fun deserialize(decoder: Decoder): Continent =
                                ContinentFragmentsAdapter.serializer().deserialize(decoder)
                    }
                }
            }
        }
    }
}

