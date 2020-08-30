package pt.iselearning.services.models.codeLanguage

/**
 * Model returned by the code loanguage service
 */
class CodeLanguageModel (
        var languageId : Int?,
        var codeLanguage : String?
) {
    constructor() : this(null, null)
}
