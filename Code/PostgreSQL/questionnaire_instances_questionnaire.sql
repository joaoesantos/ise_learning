SET search_path TO ise_learning,public;
CREATE OR REPLACE VIEW questionnaire_instances_questionnaire AS
    select
    qi.questionnaire_instance_id, 
    q.description as QuestionnaireDescription,
    qi.description as QuestionnaireInstanceDescription,
    q.creator_id as Creator_Id
    from questionnaire q
    inner join questionnaire_instance qi on qi.questionnaire_id = q.questionnaire_id
