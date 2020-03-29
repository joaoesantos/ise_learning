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
		userId INT GENERATED ALWAYS AS IDENTITY,
		username VARCHAR(50) UNIQUE NOT NULL,
		password VARCHAR(255) NOT NULL,
		email VARCHAR(50) UNIQUE NOT NULL,
		name VARCHAR(50) NOT NULL,
		image BYTEA,
		--
		PRIMARY KEY (userID)
	);
	
	CREATE TABLE code_language (
		codeLanguageId INT GENERATED ALWAYS AS IDENTITY,
		codeLanguage VARCHAR(20) UNIQUE NOT NULL,
		--
		PRIMARY KEY (codeLanguageId)
	);

	CREATE TABLE runcode (
		runCodeId INT GENERATED ALWAYS AS IDENTITY,
		codeLanguage VARCHAR(20),
		code TEXT,
		output TEXT,
		--
		PRIMARY KEY (runCodeId),
		FOREIGN KEY (codeLanguage) 
			REFERENCES code_language (codeLanguage)
	);

	CREATE TABLE tag (
		tagId INT GENERATED ALWAYS AS IDENTITY,
		tag VARCHAR(20) UNIQUE NOT NULL,
		--
		PRIMARY KEY (tagId)
	);
	
	CREATE TABLE answer (
		answerId INT GENERATED ALWAYS AS IDENTITY,
		codeLanguage VARCHAR(20) NOT NULL,
		answerCode TEXT,
		unitTests TEXT,
		isCorrect BOOLEAN DEFAULT FALSE,
		--
		PRIMARY KEY (answerId),
		FOREIGN KEY (codeLanguage) 
			REFERENCES code_language (codeLanguage) ON DELETE CASCADE
	);

	CREATE TABLE challenge (
		challengeId INT GENERATED ALWAYS AS IDENTITY,
		creatorId INT NOT NULL,
		challengeText TEXT NOT NULL,
		isPrivate BOOLEAN NOT NULL,
		--
		PRIMARY KEY (challengeId),
		FOREIGN KEY (creatorId) 
			REFERENCES app_user (userId) ON DELETE CASCADE
	);
	
	-- MANY TO MANY (CHALLENGE-TAG)
	CREATE TABLE ct(
		id INT GENERATED ALWAYS AS IDENTITY,
		challengeId INT,
		tag VARCHAR(20),
		--
		PRIMARY KEY(id,challengeId),
		FOREIGN KEY (challengeId) 
			REFERENCES CHALLENGE (challengeId) ON DELETE CASCADE,
		FOREIGN KEY (tag) 
			REFERENCES tag (tag) ON DELETE CASCADE
	);
	
	CREATE TABLE challenge_solution (
		challengeSolutionId INT GENERATED ALWAYS AS IDENTITY,
		challengeId INT NOT NULL,
		codeLanguage VARCHAR(20) NOT NULL,
		challengeCode TEXT NOT NULL,
		solutionCode TEXT NOT NULL,
		unitTests TEXT NOT NULL,
		--
		PRIMARY KEY (challengeSolutionId),
		FOREIGN KEY (challengeId) 
			REFERENCES challenge (challengeId) ON DELETE CASCADE,
		FOREIGN KEY (codeLanguage) 
			REFERENCES code_language (codeLanguage) ON DELETE CASCADE,
		--
		UNIQUE(challengeId,codeLanguage)
	);

	CREATE TABLE challenge_answer (
		challengeAnswerId INT GENERATED ALWAYS AS IDENTITY,
		answerId INT UNIQUE NOT NULL,
		challengeId INT NOT NULL,
		userId INT NOT NULL,
		--
		PRIMARY KEY (challengeAnswerId),
		FOREIGN KEY (challengeId) 
			REFERENCES challenge (challengeId) ON DELETE CASCADE,
		FOREIGN KEY (answerId) 
			REFERENCES answer (answerId) ON DELETE CASCADE,
		FOREIGN KEY (userId) 
			REFERENCES app_user (userId) ON DELETE CASCADE,
		--
		UNIQUE(challengeId,userId)
	);

	CREATE TABLE questionnaire (
		questionnaireId INT GENERATED ALWAYS AS IDENTITY,
		creatorId INT NOT NULL,
		timer INT DEFAULT 0,
		--
		PRIMARY KEY (questionnaireId),
		FOREIGN KEY (creatorId) 
			REFERENCES app_user (userId) ON DELETE CASCADE,
		CHECK(timer >= 0)
	);
	
	-- MANY TO MANY (QUESTIONNAIRE-CHALLENGE)
	CREATE TABLE qc(
		id INT GENERATED ALWAYS AS IDENTITY,
		questionnaireId INT NOT NULL,
		challengeId INT NOT NULL,
		langFilter VARCHAR(20) NOT NULL,
		--
		PRIMARY KEY(id),
		FOREIGN KEY (questionnaireId) 
			REFERENCES questionnaire (questionnaireId) ON DELETE CASCADE,
		FOREIGN KEY (challengeId) 
			REFERENCES challenge (challengeId) ON DELETE CASCADE,
		--
		UNIQUE(questionnaireId,challengeId)
	);

	CREATE TABLE questionnaire_answer (
		questionnaireAnswerId INT GENERATED ALWAYS AS IDENTITY,
		answerId INT UNIQUE NOT NULL,
		questionnaireId INT NOT NULL,
		qcId INT UNIQUE NOT NULL,
		label VARCHAR(20) NOT NULL,
		--
		PRIMARY KEY (questionnaireAnswerId),
		FOREIGN KEY (answerId) 
			REFERENCES answer (answerId) ON DELETE CASCADE,
		FOREIGN KEY (questionnaireId) 
			REFERENCES questionnaire (questionnaireId) ON DELETE CASCADE,
		FOREIGN KEY (qcId) 
			REFERENCES qc (id) ON DELETE CASCADE
	);

    COMMIT;
END;
$BODY$;
