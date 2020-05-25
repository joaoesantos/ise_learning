package pt.iselearning.services.util

/**
 * Custom Javax validators
 */
class CustomValidators {
    companion object {
        const val PRIVACY_REGEX_STRING = "\\b(private)\\b|\\b(public)\\b"
        const val TAGS_REGEX_STRING = "\\b([^,]+)([,][^,]+)*\\b"
    }
}