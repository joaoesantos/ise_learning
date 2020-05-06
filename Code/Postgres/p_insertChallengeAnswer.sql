------------------------------------
--    INSERT CHALLENGE ANSWER     --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_insertchallengeanswer(
	user_id INT,
	challenge_id INT,
	code_language VARCHAR(20),
	answer_code TEXT,
	unit_tests TEXT)
LANGUAGE 'plpgsql'

AS $BODY$
DECLARE id INT;
BEGIN
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	SET search_path TO ise_learning,public;
	INSERT INTO answer(code_language,answer_code,unit_tests) VALUES(code_language,answer_code,unit_tests) RETURNING answer_id INTO id;
	INSERT INTO challenge_answer(answer_id,challenge_id,user_id) VALUES(id,challenge_id,user_id);
END;
$BODY$;
