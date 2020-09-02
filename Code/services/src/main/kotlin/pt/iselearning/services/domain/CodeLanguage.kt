package pt.iselearning.services.domain

import pt.iselearning.services.util.SCHEMA
import javax.persistence.*

/**
 * data class that represents the code language
 */
@Entity
@Table(name="code_language", schema = SCHEMA)
data class CodeLanguage (
    @Id
    @Column(name="code_language_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var languageId : Int? =  null,

    @Column(name="code_language")
    var codeLanguage : String?
) {
    constructor() : this(null, null)
}
