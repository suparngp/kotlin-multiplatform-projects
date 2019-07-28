package com.suparnatural

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
data class DataView(
  val id: String,
  val minimumSampleSizeEnabled: Boolean?
)

@Serializable
data class OptionFilteringDatumInput(
  val filters: JSON?,
  val classicSelectionState: List<ClassicSelectionStateInput?>?
)

@Serializable(with = JSONSerializer::class)
class JSON

@Serializable
data class ClassicSelectionStateInput(
  val urlKey: String?,
  val urlValue: String?
)

@Serializable
data class ControlSelectionInput(
  val optionId: String?,
  val controlId: String?,
  val bundleId: String?,
  val instance: Int?,
  val linkQuery: String?
)

enum class RoundingMode {
  HALF_EVEN,

  HALF_UP
}

enum class ResponseLanguage {
  AFRIKAANS,

  ALBANIAN,

  ARABIC,

  ARMENIAN,

  AZERBAIJANI,

  BASQUE,

  BELARUSIAN,

  BENGALI,

  BOSNIAN,

  BULGARIAN,

  CATALAN,

  CHICHEWA,

  CHINESE,

  CROATIAN,

  CZECH,

  DANISH,

  DUTCH,

  ENGLISH,

  ESTONIAN,

  FINNISH,

  FRENCH,

  GALICIAN,

  GEORGIAN,

  GERMAN,

  GREEK,

  GUJARATI,

  HAITIAN_CREOLE,

  HAUSA,

  HEBREW,

  HINDI,

  HUNGARIAN,

  ICELANDIC,

  IGBO,

  INDONESIAN,

  IRISH,

  ITALIAN,

  JAPANESE,

  JAVANESE,

  KANNADA,

  KAZAKH,

  KHMER,

  KOREAN,

  LAO,

  LATVIAN,

  LITHUANIAN,

  MACEDONIAN,

  MALAGASY,

  MALAY,

  MALAYALAM,

  MALTESE,

  MAORI,

  MARATHI,

  MONGOLIAN,

  MYANMAR,

  NEPALI,

  NORWEGIAN,

  PANJABI,

  PERSIAN,

  POLISH,

  PORTUGUESE,

  PUNJABI,

  ROMANIAN,

  RUSSIAN,

  SERBIAN,

  SESOTHO,

  SINHALA,

  SLOVAK,

  SLOVENIAN,

  SOMALI,

  SPANISH,

  SUDANESE,

  SWAHILI,

  SWEDISH,

  TAGALOG,

  TAJIK,

  TAMIL,

  TELUGU,

  THAI,

  TURKISH,

  UKRAINIAN,

  URDU,

  UZBEK,

  VIETNAMESE,

  WELSH,

  YIDDISH,

  YORUBA,

  ZULU
}

enum class FeedbackFieldDataTypeNumberType {
  INTEGER,

  FRACTIONAL,

  REAL_NUMBER
}

enum class FeedbackFieldDataTypeTextFormat {
  TEXT,

  COMMENT,

  IP_ADDRESS,

  HYPERLINK,

  EMAIL,

  PHONE
}

enum class FeedbackFieldLayout {
  SHORT_TEXT,

  MED_TEXT,

  LONG_TEXT,

  FOUR_LINES,

  TEN_LINES,

  DROP_DOWN,

  CHECK_BOX,

  RADIO_BUTTON
}

enum class SocialSentimentId {
  NO_SENTIMENT,

  NEGATIVE,

  MILD_NEGATIVE,

  NEUTRAL,

  MILD_POSITIVE,

  POSITIVE
}
