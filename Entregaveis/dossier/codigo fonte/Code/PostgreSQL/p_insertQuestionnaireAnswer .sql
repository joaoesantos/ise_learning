------------------------------------
--    INSERT CHALLENGE ANSWER     --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_insertquestionnaireanswer(
	questionnaire_id INT,
	qc_id INT,
	code_language VARCHAR(20),
	answer_code TEXT,
	unit_tests TEXT,
	label VARCHAR(20))
LANGUAGE 'plpgsql'

AS $BODY$
DECLARE id INT;
BEGIN
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	SET search_path TO ise_learning,public;
	INSERT INTO answer(code_language,answer_code,unit_tests) VALUES(code_language,answer_code,unit_tests) RETURNING answer_id INTO id;
	INSERT INTO questionnaire_answer(answer_id,questionnaire_id,qc_id,label) VALUES(id,questionnaire_id,qc_id,label);
END;
$BODY$;
