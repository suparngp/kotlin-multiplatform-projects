const superFragment = (className, possibleTypes, properties) => `
@UnstableDefault
@Serializable(with = ${className}.Companion::class)
interface ${className}{

    companion object: KSerializer<${className}> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("SearchableSerializer")

        override fun deserialize(decoder: Decoder): ${className} {
            val json = (decoder as JsonInput).decodeJson()

            return when ((json.jsonObject["__typename"] as JsonLiteral).content) {
                ${ possibleTypes.map(t => `"${t.replace('Fragments.', '')}" -> (JsonStrategy.fromJson(${t}.serializer(), json))`).join('\n')}

                else -> {
                    throw Error("Unknown Possible Type for ${className}")
                }
            }
        }

        override fun serialize(encoder: Encoder, obj: ${className}) {
            val jsonEncoder = (encoder as JsonOutput)

            when (obj) {
                ${possibleTypes.map(t => `is ${t} -> jsonEncoder.encodeJson(JsonStrategy.toJson(${t}.serializer(), obj))`).join('\n')}
                
                else -> {
                  throw Error("Unknown Possible Type for ${className}")
                }
            }
        }
    }

    ${properties.join('\n')}
  }
`;

const classTemplate = (className, properties, parentClass) => `
    ${parentClass ? '@UnstableDefault' : ''}
    @Serializable
    data class ${className}(${properties.join(', ')})${parentClass ? `: ${parentClass}` : ''}
`;

export { superFragment, classTemplate };