package pt.iselearning.services.domain

import javax.persistence.*

/**
 * data class that represents answer entity
 */
@Entity
@Table(name="answer")
data class Answer (
    @Id
    @Column(name="answer_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id : Int? =  null,

    @Column(name="code_language")
    var codeLanguage : String?,

    @Column(name="answer_code")
    var answerCode : String?,

    @Column(name="unit_tests")
    var unitTests : String?,

    @Column(name="is_correct")
    var creatorId : Boolean?
) {
    constructor() : this(null, null, null, null, null)
}
