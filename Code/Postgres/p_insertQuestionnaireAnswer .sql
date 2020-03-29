------------------------------------
--    INSERT CHALLENGE ANSWER     --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_insertquestionnaireanswer(
	questionnaireId INT,
	qcId INT,
	codeLanguage VARCHAR(20),
	answercode TEXT,
	unitTests TEXT,
	label VARCHAR(20))
LANGUAGE 'plpgsql'

AS $BODY$
DECLARE id INT;
BEGIN
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	SET search_path TO ise_learning,public;
	INSERT INTO answer(codeLanguage,answerCode,unitTests) VALUES(codeLanguage,answerCode,unitTests) RETURNING answerID INTO id;
	INSERT INTO questionnaire_answer(answerId,questionnaireID,qcId,label) VALUES(id,questionnaireId,qcId,label);
END;
$BODY$;
