package pt.iselearning.services.domain.questionnaires

import pt.iselearning.services.util.SCHEMA
import javax.persistence.*

/**
 * Data class that represents questionnaire instance entity
 */
@Entity
@Table(name = "questionnaire_instance", schema = SCHEMA)
data class QuestionnaireInstance(

        @Column(name = "questionnaire_id")
        var questionnaireId: Int?,

        @Id
        @Column(name = "questionnaire_instance_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var questionnaireInstanceId: Int?,

        @Column(name = "questionnaire_instance_uuid")
        var questionnaireInstanceUuid: String?,

        @Column(name = "description")
        var description: String?,

        @Column(name = "timer")
        var timer: Long?,

        @Column(name = "start_timestamp")
        var startTimestamp: Long?,

        @Column(name = "end_timestamp")
        var endTimestamp: Long?,

        @Column(name = "is_finish")
        var isFinish: Boolean

) { constructor() : this(null, null, null, null, null, null, null, false) }