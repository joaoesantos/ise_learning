------------------------------------
--    INSERT CHALLENGE ANSWER     --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_insertquestionnaireanswer(
	questionnaire_instance_id integer,
	qc_id integer,
	code_language character varying,
	answer_code text,
	unit_tests boolean)
LANGUAGE 'plpgsql'

AS $BODY$
DECLARE id INT;
BEGIN
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	SET search_path TO ise_learning,public;
	INSERT INTO answer(code_language,answer_code,unit_tests) VALUES(code_language,answer_code,unit_tests) RETURNING answer_id INTO id;
	INSERT INTO questionnaire_answer(answer_id,questionnaire_instance_id,qc_id) VALUES(id,questionnaire_instance_id,qc_id);
END;
$BODY$;

