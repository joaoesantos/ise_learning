package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * data class that represents questionnaire instance entity
 */
@Validated
@Entity
@Table(name="questionnaire_instance")
data class QuestionnaireInstance (

        @Id
        @Column(name="questionnaire_instance_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire Instance id must be positive")
        var questionnaireInstanceId: Int,

        @Column(name="description")
        var description: String? = null,

        @Column(name = "timer")
        @field:Positive(message = "timer must be positive")
        var timer : Timestamp? = null,

        @Column(name = "start_timestamp")
        var startTimestamp : Timestamp? = null,

        @Column(name = "end_timestamp")
        var endTimestamp : Timestamp? = null,

        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinColumn(name = "questionnaire_id")
        var questionnaire : Questionnaire

)