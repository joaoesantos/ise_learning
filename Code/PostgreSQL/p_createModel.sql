------------------------------------
--          CREATE MODEL          --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_createmodel()
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	SET search_path TO ise_learning,public;
	
	CREATE TABLE app_user (
		user_id INT GENERATED ALWAYS AS IDENTITY,
		username VARCHAR(50) UNIQUE NOT NULL,
		password VARCHAR(255) NOT NULL,
		email VARCHAR(50) UNIQUE NOT NULL,
		name VARCHAR(50) NOT NULL,
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
		code_language VARCHAR(20),
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
		challenge_title VARCHAR(50) UNIQUE NOT NULL,
		challenge_text TEXT NOT NULL,
		is_private BOOLEAN NOT NULL DEFAULT FALSE,
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
			REFERENCES app_user (user_id) ON DELETE CASCADE
	);

	CREATE TABLE questionnaire (
		questionnaire_id INT GENERATED ALWAYS AS IDENTITY,
		description VARCHAR(50) NOT NULL,
		timer DOUBLE PRECISION,
		creator_id INT NOT NULL,
		creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
		--
		PRIMARY KEY (questionnaire_id),
		FOREIGN KEY (creator_id) 
			REFERENCES app_user (user_id) ON DELETE CASCADE
	);
	
	CREATE TABLE questionnaire_instance (
		questionnaire_instance_id INT GENERATED ALWAYS AS IDENTITY,
		questionnaire_id INT NOT NULL,
		questionnaire_instance_uuid TEXT NOT NULL,
		description VARCHAR(50) NOT NULL,
		timer DOUBLE PRECISION,
		start_timestamp DOUBLE PRECISION,
		end_timestamp DOUBLE PRECISION,
		is_finish BOOLEAN NOT NULL DEFAULT FALSE,
		--
		PRIMARY KEY (questionnaire_instance_id),
		FOREIGN KEY (questionnaire_id) 
			REFERENCES questionnaire (questionnaire_id) ON DELETE CASCADE
	);
	
	-- MANY TO MANY (QUESTIONNAIRE-CHALLENGE)
	CREATE TABLE qc(
		id INT GENERATED ALWAYS AS IDENTITY,
		questionnaire_id INT NOT NULL,
		challenge_id INT NOT NULL,
		language_filter TEXT,
		--
		PRIMARY KEY(id),
		FOREIGN KEY (questionnaire_id) 
			REFERENCES questionnaire (questionnaire_id) ON DELETE CASCADE,
		FOREIGN KEY (challenge_id) 
			REFERENCES challenge (challenge_id) ON DELETE CASCADE,
		--
		UNIQUE(questionnaire_id,challenge_id)
	);
	
	CREATE TABLE questionnaire_answer (
		questionnaire_answer_id INT GENERATED ALWAYS AS IDENTITY,
		answer_id INT UNIQUE NOT NULL,
		questionnaire_instance_id INT NOT NULL,
		qc_id INT NOT NULL,
		--
		PRIMARY KEY (questionnaire_answer_id),
		FOREIGN KEY (answer_id) 
			REFERENCES answer (answer_id) ON DELETE CASCADE,
		FOREIGN KEY (questionnaire_instance_id) 
			REFERENCES questionnaire_instance (questionnaire_instance_id) ON DELETE CASCADE,
		FOREIGN KEY (qc_id) 
			REFERENCES qc (id) ON DELETE CASCADE
		UNIQUE(qc_id, questionnaire_instance_id)
	);
	
	CREATE OR REPLACE VIEW questionnaire_instances_questionnaire AS
    SELECT
    qi.questionnaire_instance_id, 
    q.description AS QuestionnaireDescription,
    qi.description AS QuestionnaireInstanceDescription,
    q.creator_id AS Creator_Id
    FROM questionnaire q
    INNER JOIN questionnaire_instance qi ON qi.questionnaire_id = q.questionnaire_id;

    COMMIT;
END;
$BODY$;
