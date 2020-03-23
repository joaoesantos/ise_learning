------------------------------------
--            TRIGGERS            --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE FUNCTION before_insert_challenge_answer()
RETURNS TRIGGER
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	IF EXISTS(SELECT * FROM QUESTIONNAIRE_ANSWER QA WHERE QA.answerID = NEW.answerID )
	THEN
		RAISE EXCEPTION 'Already exists an questionnaire answer with this answer id';
	ELSE
		RETURN NEW;
	END IF;
END;
$BODY$;

CREATE TRIGGER trig_on_challenge_answer 
BEFORE INSERT ON CHALLENGE_ANSWER
FOR EACH ROW 
EXECUTE PROCEDURE before_insert_challenge_answer();

--

CREATE OR REPLACE FUNCTION before_insert_questionnaire_answer()
RETURNS TRIGGER
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	IF EXISTS(SELECT * FROM CHALLENGE_ANSWER CA WHERE CA.answerID = NEW.answerID )
	THEN 
		RAISE EXCEPTION 'Already exists an challenge answer with this answer id';
	ELSE
		RETURN NEW;
	END IF;
END;
$BODY$;

CREATE TRIGGER trig_on_questionaire_answer 
BEFORE INSERT ON QUESTIONNAIRE_ANSWER
FOR EACH ROW 
EXECUTE PROCEDURE before_insert_questionnaire_answer();