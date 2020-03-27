------------------------------------
--    INSERT CHALLENGE ANSWER     --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_insertchallengeanswer(
	userid INT,
	challengeid INT,
	codeLanguage VARCHAR(20),
	answercode TEXT,
	unittests TEXT)
LANGUAGE 'plpgsql'

AS $BODY$
DECLARE id INT;
BEGIN
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	SET search_path TO ise_learning,public;
	INSERT INTO ANSWER(codeLanguage,answerCode,unitTests) VALUES(codeLanguage,answerCode,unitTests) RETURNING answerID INTO id;
	INSERT INTO CHALLENGE_ANSWER(answerID,challengeID,userID) VALUES(id,challengeID,userID);
END;
$BODY$;
