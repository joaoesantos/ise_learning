------------------------------------
--    CREATE DB FOR CLOUD SQL     --
------------------------------------
CREATE DATABASE db_name
	WITH
	OWNER =  owner_name		
	ENCODING = 'UTF8'		
	CONNECTION LIMIT = -1;

------------------------------------
--          CREATE MODEL          --
------------------------------------
CREATE TABLE app_user (
	user_id INT GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(50) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(50) UNIQUE NOT NULL,
	name VARCHAR(50) NOT NULL,
	image BYTEA,
	--
	PRIMARY KEY (user_id)
);
	
CREATE TABLE code_language (
	code_language_id INT GENERATED ALWAYS AS IDENTITY,
	code_language VARCHAR(20) UNIQUE NOT NULL,
	--
	PRIMARY KEY (code_language_id)
);

CREATE TABLE tag (
	tag_id INT GENERATED ALWAYS AS IDENTITY,
	tag VARCHAR(20) UNIQUE NOT NULL,
	--
	PRIMARY KEY (tag_id)
);
	
CREATE TABLE answer (
	answer_id INT GENERATED ALWAYS AS IDENTITY,
	code_language VARCHAR(20) NOT NULL,
	answer_code TEXT,
	unit_tests TEXT,
	is_correct BOOLEAN DEFAULT FALSE,
	--
	PRIMARY KEY (answer_id),
	FOREIGN KEY (code_language) 
		REFERENCES code_language (code_language) ON DELETE CASCADE
);

CREATE TABLE challenge (
	challenge_id INT GENERATED ALWAYS AS IDENTITY,
	creator_id INT NOT NULL,
	challenge_text TEXT NOT NULL,
	is_private BOOLEAN NOT NULL,
	--
	PRIMARY KEY (challenge_id),
	FOREIGN KEY (creator_id) 
		REFERENCES app_user (user_id) ON DELETE CASCADE
);
	
-- MANY TO MANY (CHALLENGE-TAG)
CREATE TABLE ct(
	id INT GENERATED ALWAYS AS IDENTITY,
	challenge_id INT,
	tag_id INT,
	--
	PRIMARY KEY(id),
	FOREIGN KEY (challenge_id) 
		REFERENCES CHALLENGE (challenge_id) ON DELETE CASCADE,
	FOREIGN KEY (tag_id) 
		REFERENCES tag (tag_id) ON DELETE CASCADE,
	UNIQUE(challenge_id, tag_id)
);
	
CREATE TABLE challenge_solution (
	challenge_solution_id INT GENERATED ALWAYS AS IDENTITY,
	challenge_id INT NOT NULL,
	code_language VARCHAR(20) NOT NULL,
	challenge_code TEXT NOT NULL,
	solution_code TEXT NOT NULL,
	unit_tests TEXT NOT NULL,
	--
	PRIMARY KEY (challenge_solution_id),
	FOREIGN KEY (challenge_id) 
		REFERENCES challenge (challenge_id) ON DELETE CASCADE,
	FOREIGN KEY (code_language) 
		REFERENCES code_language (code_language) ON DELETE CASCADE,
	--
	UNIQUE(challenge_id,code_language)
);

CREATE TABLE challenge_answer (
	challenge_answer_id INT GENERATED ALWAYS AS IDENTITY,
	answer_id INT UNIQUE NOT NULL,
	challenge_id INT NOT NULL,
	user_id INT NOT NULL,
	--
	PRIMARY KEY (challenge_answer_id),
	FOREIGN KEY (challenge_id) 
		REFERENCES challenge (challenge_id) ON DELETE CASCADE,
	FOREIGN KEY (answer_id) 
		REFERENCES answer (answer_id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) 
		REFERENCES app_user (user_id) ON DELETE CASCADE,
	--
	UNIQUE(challenge_id,user_id)
);

CREATE TABLE questionnaire (
	questionnaire_id INT GENERATED ALWAYS AS IDENTITY,
	creator_id INT NOT NULL,
	timer INT DEFAULT 0,
	--
	PRIMARY KEY (questionnaire_id),
	FOREIGN KEY (creator_id) 
		REFERENCES app_user (user_id) ON DELETE CASCADE,
	CHECK(timer >= 0)
);
	
-- MANY TO MANY (QUESTIONNAIRE-CHALLENGE)
CREATE TABLE qc(
	id INT GENERATED ALWAYS AS IDENTITY,
	questionnaire_id INT NOT NULL,
	challenge_id INT NOT NULL,
	lang_filter VARCHAR(20) NOT NULL,
	--
	PRIMARY KEY(id),
	FOREIGN KEY (questionnaire_id) 
		REFERENCES questionnaire (questionnaire_id) ON DELETE CASCADE,
	FOREIGN KEY (challenge_id) 
		REFERENCES challenge (challenge_id) ON DELETE CASCADE,
	--
	UNIQUE(questionnaire_id,challenge_id,lang_filter)
);

CREATE TABLE questionnaire_answer (
	questionnaire_answer_id INT GENERATED ALWAYS AS IDENTITY,
	answer_id INT UNIQUE NOT NULL,
	questionnaire_id INT NOT NULL,
	qc_id INT UNIQUE NOT NULL,
	label VARCHAR(20) NOT NULL,
	start_date TIMESTAMP,
	end_date TIMESTAMP,
	--
	PRIMARY KEY (questionnaire_answer_id),
	FOREIGN KEY (answer_id) 
		REFERENCES answer (answer_id) ON DELETE CASCADE,
	FOREIGN KEY (questionnaire_id) 
		REFERENCES questionnaire (questionnaire_id) ON DELETE CASCADE,
	FOREIGN KEY (qc_id) 
		REFERENCES qc (id) ON DELETE CASCADE
);

------------------------------------
--   INSERT CHALLENGE ANSWER  SP  --
------------------------------------
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

------------------------------------
--    INSERT CHALLENGE ANSWER     --
------------------------------------
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


------------------------------------
--            TRIGGERS            --
------------------------------------
CREATE OR REPLACE FUNCTION before_insert_challenge_answer()
RETURNS TRIGGER
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	IF EXISTS(SELECT * FROM questionnaire_answer QA WHERE QA.answer_id = NEW.answer_id )
	THEN
		RAISE EXCEPTION 'Already exists an questionnaire answer with this answer id';
	ELSE
		RETURN NEW;
	END IF;
END;
$BODY$;

CREATE TRIGGER trig_on_challenge_answer 
BEFORE INSERT ON challenge_answer
FOR EACH ROW 
EXECUTE PROCEDURE before_insert_challenge_answer();

--

CREATE OR REPLACE FUNCTION before_insert_questionnaire_answer()
RETURNS TRIGGER
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	IF EXISTS(SELECT * FROM challenge_answer CA WHERE CA.answer_id = NEW.answer_id)
	THEN 
		RAISE EXCEPTION 'Already exists an challenge answer with this answer id';
	ELSE
		RETURN NEW;
	END IF;
END;
$BODY$;

CREATE TRIGGER trig_on_questionaire_answer 
BEFORE INSERT ON questionnaire_answer
FOR EACH ROW 
EXECUTE PROCEDURE before_insert_questionnaire_answer();