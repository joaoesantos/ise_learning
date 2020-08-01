package pt.iselearning.services.domain.questionnaires

import pt.iselearning.services.util.Constants
import java.sql.Timestamp
import javax.persistence.*

/**
 * Data class that represents questionnaire instance entity
 */
@Entity
@Table(name = "questionnaire_instance", schema = Constants.SCHEMA)
data class QuestionnaireInstance (

        @Id
        @Column(name = "questionnaire_instance_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var questionnaireInstanceId: Int?,

        @Column(name = "questionnaire_id")
        var questionnaireId: Int?,

        @Column(name = "questionnaire_instance_uuid")
        var questionnaireInstanceUuid: String?,

        @Column(name = "description")
        var description: String?,

        @Column(name = "timer")
        var timer: Int?,

        @Column(name = "start_timestamp")
        var startTimestamp: Timestamp?,

        @Column(name = "end_timestamp")
        var endTimestamp: Timestamp?

) { constructor() : this(null, null, null, null, null, null, null) }