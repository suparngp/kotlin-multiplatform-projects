package com.suparnatural

import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

interface Fragments {
  interface CommentFieldData {
    val __typename: String

    val fieldId: String?

    val textsWithLanguage: List<ResponseTextWithLanguage?>?

    val originalLanguage: ResponseLanguage?

    @Serializable
    data class ResponseTextWithLanguage(
      val __typename: String,
      val text: String?,
      val language: ResponseLanguage?
    )

    @Serializable
    data class ResponseTextWithLanguage(
      val __typename: String,
      val text: String?,
      val language: ResponseLanguage?
    )

    @Serializable
    data class CommentFieldDataImpl(
      val __typename: String,
      val fieldId: String?,
      val textsWithLanguage: List<ResponseTextWithLanguage?>?,
      val originalLanguage: ResponseLanguage?
    ) : com.suparnatural.CommentFieldData
  }

  interface FeedbackFieldOptionData {
    val __typename: String

    val id: String

    val name: String

    val numericValue: Int?

    val boxColor: String?

    val fontColor: String?

    val isNoAnswer: Boolean?

    @Serializable
    data class FeedbackFieldOptionDataImpl(
      val __typename: String,
      val id: String,
      val name: String,
      val numericValue: Int?,
      val boxColor: String?,
      val fontColor: String?,
      val isNoAnswer: Boolean?
    ) : com.suparnatural.FeedbackFieldOptionData
  }

  interface FeedbackFieldDataTypeNumberData {
    val __typename: String

    val min: Int?

    val max: Int?

    val type: FeedbackFieldDataTypeNumberType

    val multiplier: Int?

    val precision: Float?

    @Serializable
    data class FeedbackFieldDataTypeNumberDataImpl(
      val __typename: String,
      val min: Int?,
      val max: Int?,
      val type: FeedbackFieldDataTypeNumberType,
      val multiplier: Int?,
      val precision: Float?
    ) : com.suparnatural.FeedbackFieldDataTypeNumberData
  }

  interface FeedbackFieldDataTypeTextData {
    val __typename: String

    val format: FeedbackFieldDataTypeTextFormat?

    @Serializable
    data class FeedbackFieldDataTypeTextDataImpl(
      val __typename: String,
      val format: FeedbackFieldDataTypeTextFormat?
    ) : com.suparnatural.FeedbackFieldDataTypeTextData
  }

  interface FeedbackFieldData {
    val __typename: String

    val id: String

    val name: String

    val multiValued: Boolean

    val description: String?

    val isRatingScale: Boolean

    val layout: FeedbackFieldLayout?

    val queryable: Boolean

    val subRecord: SubRecordConfig?

    val dataType: FeedbackFieldDataTypeInterface

    val options: FeedbackFieldOptions

    @Serializable
    data class SubRecordConfig(
      val __typename: String,
      val fieldIds: List<String?>
    )

    @Serializable
    data class SubRecordConfig(
      val __typename: String,
      val fieldIds: List<String?>
    )

    interface FeedbackFieldDataTypeInterface {
      val __typename: String

      val isNumeric: Boolean

      @Serializable
      data class FeedbackFieldDataTypeInterfaceImpl(
        val __typename: String,
        val isNumeric: Boolean
      ) : FeedbackFieldDataTypeInterface
    }

    @Serializable
    data class FeedbackFieldOptions(
      val __typename: String,
      val total: Int,
      val items: List<FeedbackFieldOption?>
    ) {
      interface FeedbackFieldOption {
        val __typename: String

        @Serializable
        data class FeedbackFieldOptionImpl(
          val __typename: String
        ) : FeedbackFieldOption
      }
    }

    @Serializable
    data class FeedbackFieldOptions(
      val __typename: String,
      val total: Int,
      val items: List<FeedbackFieldOption?>
    ) {
      interface FeedbackFieldOption {
        val __typename: String

        @Serializable
        data class FeedbackFieldOptionImpl(
          val __typename: String
        ) : FeedbackFieldOption
      }
    }

    @Serializable
    data class FeedbackFieldDataImpl(
      val __typename: String,
      val id: String,
      val name: String,
      val multiValued: Boolean,
      val description: String?,
      val isRatingScale: Boolean,
      val layout: FeedbackFieldLayout?,
      val queryable: Boolean,
      val subRecord: SubRecordConfig?,
      val dataType: FeedbackFieldDataTypeInterface,
      val options: FeedbackFieldOptions
    ) : com.suparnatural.FeedbackFieldData
  }

  interface ModuleConfig {
    val __typename: String

    val name: String?

    val configOptions: List<ModuleConfigOption?>

    @Serializable
    data class ModuleConfigOption(
      val __typename: String,
      val key: String,
      val type: String?,
      val value: JSON?
    )

    @Serializable
    data class ModuleConfigOption(
      val __typename: String,
      val key: String,
      val type: String?,
      val value: JSON?
    )

    @Serializable
    data class ModuleConfigImpl(
      val __typename: String,
      val name: String?,
      val configOptions: List<ModuleConfigOption?>
    ) : com.suparnatural.ModuleConfig
  }

  interface ControlOptionData {
    val __typename: String

    val id: String?

    val value: String?

    val label: String?

    val type: String?

    val filters: JSON?

    val slices: List<Slice?>?

    val classicSelectionState: List<ClassicSelectionState?>?

    val ancestors: List<String?>?

    val ancestorLabels: List<String?>?

    val hasDescendants: Boolean?

    val timeperiodType: String?

    val reportingDateField: String?

    val hideEmptyColumns: Boolean?

    val fieldIds: List<String?>?

    val calculation: Calculation?

    val benchmark: String?

    val segmentRankerField: String?

    val commentField: String?

    @Serializable
    data class Slice(
      val __typename: String,
      val label: String?,
      val filters: JSON?
    )

    @Serializable
    data class Slice(
      val __typename: String,
      val label: String?,
      val filters: JSON?
    )

    @Serializable
    data class ClassicSelectionState(
      val __typename: String,
      val urlKey: String?,
      val urlValue: String?
    )

    @Serializable
    data class ClassicSelectionState(
      val __typename: String,
      val urlKey: String?,
      val urlValue: String?
    )

    @Serializable
    data class Calculation(
      val __typename: String,
      val id: String?,
      val label: String?,
      val queryKey: String?
    )

    @Serializable
    data class Calculation(
      val __typename: String,
      val id: String?,
      val label: String?,
      val queryKey: String?
    )

    @Serializable
    data class ControlOptionDataImpl(
      val __typename: String,
      val id: String?,
      val value: String?,
      val label: String?,
      val type: String?,
      val filters: JSON?,
      val slices: List<Slice?>?,
      val classicSelectionState: List<ClassicSelectionState?>?,
      val ancestors: List<String?>?,
      val ancestorLabels: List<String?>?,
      val hasDescendants: Boolean?,
      val timeperiodType: String?,
      val reportingDateField: String?,
      val hideEmptyColumns: Boolean?,
      val fieldIds: List<String?>?,
      val calculation: Calculation?,
      val benchmark: String?,
      val segmentRankerField: String?,
      val commentField: String?
    ) : com.suparnatural.ControlOptionData
  }

  interface RecordFieldLabeledValueData {
    val __typename: String

    val label: String?

    val value: String?

    @Serializable
    data class RecordFieldLabeledValueDataImpl(
      val __typename: String,
      val label: String?,
      val value: String?
    ) : com.suparnatural.RecordFieldLabeledValueData
  }

  interface RecordFieldDatumData {
    val __typename: String

    val values: List<RecordFieldLabeledValue?>?

    val field: FeedbackField

    interface RecordFieldLabeledValue {
      val __typename: String

      @Serializable
      data class RecordFieldLabeledValueImpl(
        val __typename: String
      ) : RecordFieldLabeledValue
    }

    @Serializable
    data class FeedbackField(
      val __typename: String,
      val id: String
    )

    @Serializable
    data class FeedbackField(
      val __typename: String,
      val id: String
    )

    @Serializable
    data class RecordFieldDatumDataImpl(
      val __typename: String,
      val values: List<RecordFieldLabeledValue?>?,
      val field: FeedbackField
    ) : com.suparnatural.RecordFieldDatumData
  }

  interface ExclusionData {
    val __typename: String

    val excluded: Boolean

    @Serializable
    data class ExclusionDataImpl(
      val __typename: String,
      val excluded: Boolean
    ) : com.suparnatural.ExclusionData
  }

  interface RecordFieldData {
    val __typename: String

    val items: List<RecordFieldDatum?>

    interface RecordFieldDatum {
      val __typename: String

      @Serializable
      data class RecordFieldDatumImpl(
        val __typename: String
      ) : RecordFieldDatum
    }

    @Serializable
    data class RecordFieldDataImpl(
      val __typename: String,
      val items: List<RecordFieldDatum?>
    ) : com.suparnatural.RecordFieldData
  }

  interface ResponseSocialSentimentData {
    val __typename: String

    val id: String?

    val sentimentId: SocialSentimentId?

    val value: Int?

    val label: String?

    @Serializable
    data class ResponseSocialSentimentDataImpl(
      val __typename: String,
      val id: String?,
      val sentimentId: SocialSentimentId?,
      val value: Int?,
      val label: String?
    ) : com.suparnatural.ResponseSocialSentimentData
  }

  interface ResponseSocialFeedbackSentimentData {
    val __typename: String

    val socialSentiment: ResponseSocialSentiment?

    interface ResponseSocialSentiment {
      val __typename: String

      @Serializable
      data class ResponseSocialSentimentImpl(
        val __typename: String
      ) : ResponseSocialSentiment
    }

    @Serializable
    data class ResponseSocialFeedbackSentimentDataImpl(
      val __typename: String,
      val socialSentiment: ResponseSocialSentiment?
    ) : com.suparnatural.ResponseSocialFeedbackSentimentData
  }

  interface SocialFeedbackSourceColorData {
    val __typename: String

    val start: Int

    val end: Int

    val background: String

    val border: String

    @Serializable
    data class SocialFeedbackSourceColorDataImpl(
      val __typename: String,
      val start: Int,
      val end: Int,
      val background: String,
      val border: String
    ) : com.suparnatural.SocialFeedbackSourceColorData
  }

  interface SocialFeedbackSourceData {
    val __typename: String

    val id: String

    val name: String

    val kind: String

    val subMetrics: SourceSubMetrics

    val overallScore: SourceScore

    val scales: ScoreScales

    @Serializable
    data class SourceSubMetrics(
      val __typename: String,
      val items: List<SourceScore?>
    ) {
      @Serializable
      data class SourceScore(
        val __typename: String,
        val percentageField: SocialFeedbackField?
      ) {
        @Serializable
        data class SocialFeedbackField(
          val __typename: String,
          val id: String
        )

        @Serializable
        data class SocialFeedbackField(
          val __typename: String,
          val id: String
        )
      }

      @Serializable
      data class SourceScore(
        val __typename: String,
        val percentageField: SocialFeedbackField?
      ) {
        @Serializable
        data class SocialFeedbackField(
          val __typename: String,
          val id: String
        )

        @Serializable
        data class SocialFeedbackField(
          val __typename: String,
          val id: String
        )
      }
    }

    @Serializable
    data class SourceSubMetrics(
      val __typename: String,
      val items: List<SourceScore?>
    ) {
      @Serializable
      data class SourceScore(
        val __typename: String,
        val percentageField: SocialFeedbackField?
      ) {
        @Serializable
        data class SocialFeedbackField(
          val __typename: String,
          val id: String
        )

        @Serializable
        data class SocialFeedbackField(
          val __typename: String,
          val id: String
        )
      }

      @Serializable
      data class SourceScore(
        val __typename: String,
        val percentageField: SocialFeedbackField?
      ) {
        @Serializable
        data class SocialFeedbackField(
          val __typename: String,
          val id: String
        )

        @Serializable
        data class SocialFeedbackField(
          val __typename: String,
          val id: String
        )
      }
    }

    @Serializable
    data class SourceScore(
      val __typename: String,
      val nativeField: SocialFeedbackField?
    ) {
      @Serializable
      data class SocialFeedbackField(
        val __typename: String,
        val id: String
      )

      @Serializable
      data class SocialFeedbackField(
        val __typename: String,
        val id: String
      )
    }

    @Serializable
    data class SourceScore(
      val __typename: String,
      val nativeField: SocialFeedbackField?
    ) {
      @Serializable
      data class SocialFeedbackField(
        val __typename: String,
        val id: String
      )

      @Serializable
      data class SocialFeedbackField(
        val __typename: String,
        val id: String
      )
    }

    @Serializable
    data class ScoreScales(
      val __typename: String,
      val items: List<ScoreScale?>
    ) {
      @Serializable
      data class ScoreScale(
        val __typename: String,
        val min: Int,
        val minDecimalPlaces: Int,
        val max: Int,
        val maxDecimalPlaces: Int,
        val colorScheme: List<ColorScheme?>
      ) {
        interface ColorScheme {
          val __typename: String

          @Serializable
          data class ColorSchemeImpl(
            val __typename: String
          ) : ColorScheme
        }
      }

      @Serializable
      data class ScoreScale(
        val __typename: String,
        val min: Int,
        val minDecimalPlaces: Int,
        val max: Int,
        val maxDecimalPlaces: Int,
        val colorScheme: List<ColorScheme?>
      ) {
        interface ColorScheme {
          val __typename: String

          @Serializable
          data class ColorSchemeImpl(
            val __typename: String
          ) : ColorScheme
        }
      }
    }

    @Serializable
    data class ScoreScales(
      val __typename: String,
      val items: List<ScoreScale?>
    ) {
      @Serializable
      data class ScoreScale(
        val __typename: String,
        val min: Int,
        val minDecimalPlaces: Int,
        val max: Int,
        val maxDecimalPlaces: Int,
        val colorScheme: List<ColorScheme?>
      ) {
        interface ColorScheme {
          val __typename: String

          @Serializable
          data class ColorSchemeImpl(
            val __typename: String
          ) : ColorScheme
        }
      }

      @Serializable
      data class ScoreScale(
        val __typename: String,
        val min: Int,
        val minDecimalPlaces: Int,
        val max: Int,
        val maxDecimalPlaces: Int,
        val colorScheme: List<ColorScheme?>
      ) {
        interface ColorScheme {
          val __typename: String

          @Serializable
          data class ColorSchemeImpl(
            val __typename: String
          ) : ColorScheme
        }
      }
    }

    @Serializable
    data class SocialFeedbackSourceDataImpl(
      val __typename: String,
      val id: String,
      val name: String,
      val kind: String,
      val subMetrics: SourceSubMetrics,
      val overallScore: SourceScore,
      val scales: ScoreScales
    ) : com.suparnatural.SocialFeedbackSourceData
  }
}
