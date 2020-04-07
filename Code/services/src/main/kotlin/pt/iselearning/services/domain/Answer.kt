package pt.iselearning.services.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType

/**
 * data class that represents answer entity
 */

@Table("answer")
data class Answer (
    @Id
    @Column("answerId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id : Int? =  null,

    @Column("codeLanguage")
    var codeLanguage : String?,

    @Column("answerCode")
    var answerCode : String?,

    @Column("unitTests")
    var unitTests : String?,

    @Column("isCorrect")
    var creatorId : Boolean?
)
