package pt.iselearning.services.domain.questionnaires

import pt.iselearning.services.domain.Answer
import pt.iselearning.services.util.SCHEMA
import javax.persistence.*
import javax.validation.constraints.Positive

@Entity
@Table(name = "questionnaire_instances_questionnaire", schema = SCHEMA)
data class QuestionnaireInstanceQuestionnaireView(
    @Id
    @Column(name = "questionnaire_instance_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var questionnaireInstanceId: Int?,

    @Column(name = "questionnaire_instance_description")
    var questionnaireInstanceDescription: String?,

    @Column(name = "questionnaire_description")
    var questionnaireDescription: String?,

    @Column(name = "creator_Id")
    var creatorId: Int?
) { constructor() : this(null, null,null,null) }