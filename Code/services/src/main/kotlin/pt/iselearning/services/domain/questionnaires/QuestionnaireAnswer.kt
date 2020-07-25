package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Answer
import pt.iselearning.services.util.Constants
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * Data class that represents questionnaire answer entity.
 */
@Validated
@Entity
@Table(name = "questionnaire_answer", schema = Constants.SCHEMA)
data class QuestionnaireAnswer (

        @Id
        @Column(name = "questionnaire_answer_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire Answer id must be positive")
        var questionnaireAnswerId : Int?,

        @Column(name = "questionnaire_id")
        @field:Positive(message = "Questionnaire Instance id must be positive")
        var questionnaireInstanceId : Int?,

        @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinColumn(name = "answer_id")
        var answer : MutableList<Answer>?

) { constructor() : this(null, null, null) }