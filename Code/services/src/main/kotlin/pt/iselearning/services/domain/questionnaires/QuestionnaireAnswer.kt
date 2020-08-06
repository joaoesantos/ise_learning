package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Answer
import pt.iselearning.services.util.SCHEMA
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * Data class that represents questionnaire answer entity.
 */
@Validated
@Entity
@Table(name = "questionnaire_answer", schema = SCHEMA)
data class QuestionnaireAnswer (

        @Id
        @Column(name = "questionnaire_answer_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire Answer id must be positive")
        var questionnaireAnswerId: Int?,

        @Column(name = "questionnaire_instance_id")
        @field:Positive(message = "Questionnaire Instance id must be positive")
        var questionnaireInstanceId: Int?,

        @Column(name = "qc_id")
        @field:Positive(message = "Questionnaire-Challenge id must be positive")
        var qcId: Int?,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name="answer_id", nullable=false, updatable=true, insertable=true)
        var answer: Answer?

) { constructor() : this(null, null,null, null) }