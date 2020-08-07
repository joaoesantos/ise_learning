------------------------------------
--           DROP MODEL           --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_dropmodel()
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	SET search_path TO ise_learning,public;
	DROP TABLE challenge_solution, challenge_answer, questionnaire_answer, answer;
	DROP TABLE qc, ct; -- MANY TO MANY
	DROP TABLE challenge, questionnaire_instance, questionnaire;
	DROP TABLE app_user;
	DROP TABLE tag;
	DROP TABLE code_language;
    COMMIT;
END;
$BODY$;

