------------------------------------
--           DROP MODEL           --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_dropmodel()
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	SET search_path TO ise_learning,public;
	DROP TABLE CHALLENGE_SOLUTION, CHALLENGE_ANSWER, QUESTIONNAIRE_ANSWER, ANSWER;
	DROP TABLE QC, CT; -- MANY TO MANY
	DROP TABLE CHALLENGE, QUESTIONNAIRE;
	DROP TABLE USERS;
	DROP TABLE TAG;
	DROP TABLE RUNCODE;
	DROP TABLE CODELANGUAGE;
    COMMIT;
END;
$BODY$;

