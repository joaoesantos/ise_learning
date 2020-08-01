package pt.iselearning.services.util

import org.modelmapper.AbstractConverter
import org.modelmapper.Converter
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.domain.questionnaires.Questionnaire

/**
 * Converter to be used by a ModelMapper instance to convert questionnaire to an Id representation
 */
fun getQuestionnaireToIdConverter(): Converter<Questionnaire, Int> {
    return object : AbstractConverter<Questionnaire, Int>() {
        override fun convert(source: Questionnaire?): Int {
            return source?.questionnaireId!!
        }
    }
}

/**
 * Converter to be used by a ModelMapper instance to convert challenge to an Id representation
 */
fun getChallengeToIdConverter(): Converter<Challenge, Int> {
    return object : AbstractConverter<Challenge, Int>() {
        override fun convert(source: Challenge?): Int {
            return source?.challengeId!!
        }
    }
}