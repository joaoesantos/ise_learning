package pt.iselearning.services.domain.questionnaires

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import pt.iselearning.services.util.SCHEMA
import java.util.*
import javax.persistence.*

/**
 * Data class that represents questionnaire entity
 */
@Entity
@Table(name = "questionnaire", schema = SCHEMA)
data class Questionnaire(

        @Id
        @Column(name = "questionnaire_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        var questionnaireId: Int?,

        @Column(name = "description")
        var description: String?,

        @Column(name = "timer", updatable = true)
        var timer: Long?,

        @Column(name = "creator_id")
        var creatorId: Int?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Generated(GenerationTime.INSERT)
        @Column(name = "creation_date", insertable = false, updatable = false)
        var creationDate: Date?

) { constructor() : this(null, null, null, null, null) }