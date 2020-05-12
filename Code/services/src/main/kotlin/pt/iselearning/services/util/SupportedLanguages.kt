package pt.iselearning.services.util

/**
 * Enum representing the supported languages
 */
enum class SupportedLanguages(val stringRepresentation : String) {
    JAVA("java"),
    KOTLIN("kotlin"),
    C_SHARP("cs"),
    JAVASCRIPT("javascript"),
    PYTHON("python");

    companion object {
        const val REGEX_STRING_SUPPORTED_LANGUAGES = "\\b(java)\\b|\\b(kotlin)\\b|\\b(cs)\\b|\\b(javascript)\\b|\\b(python)\\b"
        fun getRegexForSupportedLanguages() : String {
            return values().joinToString("|") { e -> "\\b(${e.stringRepresentation})\\b" }
        }
    }
}