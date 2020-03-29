------------------------------------
--    INSERT CHALLENGE ANSWER     --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_insertchallengeanswer(
	userId INT,
	challengeId INT,
	codeLanguage VARCHAR(20),
	answercode TEXT,
	unitTests TEXT)
LANGUAGE 'plpgsql'

AS $BODY$
DECLARE id INT;
BEGIN
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	SET search_path TO ise_learning,public;
	INSERT INTO answer(codeLanguage,answerCode,unitTests) VALUES(codeLanguage,answerCode,unitTests) RETURNING answerID INTO id;
	INSERT INTO challenge_answer(answerId,challengeId,userId) VALUES(id,challengeId,userId);
END;
$BODY$;
