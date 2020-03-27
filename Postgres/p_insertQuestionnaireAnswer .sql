------------------------------------
--    INSERT CHALLENGE ANSWER     --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_insertquestionnaireanswer(
	questionnaireid INT,
	qcID INT,
	codeLanguage VARCHAR(20),
	answercode TEXT,
	unittests TEXT,
	label VARCHAR(20))
LANGUAGE 'plpgsql'

AS $BODY$
DECLARE id INT;
BEGIN
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	SET search_path TO ise_learning,public;
	INSERT INTO ANSWER(codeLanguage,answerCode,unitTests) VALUES(codeLanguage,answerCode,unitTests) RETURNING answerID INTO id;
	INSERT INTO QUESTIONNAIRE_ANSWER(answerID,questionnaireID,qcID,label) VALUES(id,questionnaireID,qcID,label);
END;
$BODY$;
