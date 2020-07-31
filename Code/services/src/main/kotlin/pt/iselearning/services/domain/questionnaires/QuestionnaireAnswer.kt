package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Answer
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * data class that represents questionnaire answer entity
 */
@Validated
@Entity
@Table(name="questionnaire_answer")
data class QuestionnaireAnswer (

        @Id
        @Column(name="questionnaire_answer_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire Answer id must be positive")
        var questionnaireAnswerId : Int? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name="answer_id")
        var answer : Answer? = null

)