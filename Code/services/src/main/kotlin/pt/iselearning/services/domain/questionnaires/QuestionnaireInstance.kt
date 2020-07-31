package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.Constants
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

/**
 * Data class that represents questionnaire instance entity
 */
@Validated
@Entity
@Table(name = "questionnaire_instance", schema = Constants.SCHEMA)
data class QuestionnaireInstance (

        @Id
        @Column(name = "questionnaire_instance_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire Instance id must be positive")
        var questionnaireInstanceId: Int?,

        @Column(name = "questionnaire_id")
        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId : Int?,

        @Column(name = "questionnaire_instance_uuid")
        var questionnaireInstanceUuid: String?,

        @Column(name = "description")
        @field:Size(min = 1, max = 50, message = "Questionnaire Instance description must be between 1 and 50 characters")
        var description: String?,

        @Column(name = "timer")
        @field:Positive(message = "Timer value must be positive")
        var timer : Int?,

        @Column(name = "start_timestamp")
        var startTimestamp : Timestamp?,

        @Column(name = "end_timestamp")
        var endTimestamp : Timestamp?

) { constructor() : this(null, null, null, null, null, null, null) }