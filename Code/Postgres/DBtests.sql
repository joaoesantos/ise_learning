SET search_path TO ise_learning,public;

SELECT * FROM USERS;
--
SELECT * FROM USERS U
INNER JOIN CHALLENGE C ON U.user_id = C.creator_id
WHERE U.user_id = 1;
--
SELECT * FROM USERS U
INNER JOIN QUESTIONNAIRE Q ON U.user_id = Q.creator_id
INNER JOIN QUESTIONNAIRE_ANSWER QA ON Q.questionnaire_id = QA.questionnaire_id
INNER JOIN QC QC ON QA.questionnaire_id = QC.questionnaire_id 
WHERE U.user_id = 1;
-- TEST IF REMOVING USER 1 REMOVES ALSO RESPECTIVES ROWS IN:
-- CHALLENGE (OK)
-- CHALLENGE ANSWER (OK)
-- QUESTIONNAIRE (OK)
-- QUESTIONNAIRE ANSWER (OK)
-- QC (OK)
DELETE FROM USERS U WHERE U.user_id = 1; 

--
SELECT * FROM CHALLENGE;
SELECT * FROM CHALLENGE_ANSWER;
SELECT * FROM CHALLENGE C
LEFT JOIN CHALLENGE_ANSWER CA ON C.challenge_id = CA.challenge_answer_id;

SELECT * FROM ANSWER A
INNER JOIN  CHALLENGE_ANSWER CA ON A.answer_id = CA.answer_id
-- TEST IF REMOVING CHALLENGE 1 REMOVES ALSO RESPECTIVES ROWS IN:
-- CHALLENGE ANSWER (OK)
-- QUESTIONNAIRE ANSWER (OK)
-- QC (OK)
DELETE FROM CHALLENGE C WHERE C.challenge_id = 1;

--
SELECT * FROM QUESTIONNAIRE;
SELECT * FROM QUESTIONNAIRE_ANSWER;
SELECT * FROM QUESTIONNAIRE Q
LEFT JOIN QUESTIONNAIRE_ANSWER QA ON Q.questionnaire_id = QA.questionnaire_id
LEFT JOIN QC QC ON QA.questionnaire_id = QC.questionnaire_id
LEFT JOIN CHALLENGE_ANSWER CA ON QA_CA.challenge_answer_id = CA.challenge_answer_id;
DELETE FROM QUESTIONNAIRE Q WHERE Q.questionnaire_id = 1;
-- TEST IF REMOVING QUESTIONNAIRE 2 REMOVES ALSO RESPECTIVES ROWS IN:
-- QUESTIONNAIRE ANSWER (OK)
-- QC (OK)

-- TESTING TRIGGERS
-- INSERTING CHALLENGE_ANSWER WITHOUT ANSWER ID ON QUESTIONNAIRE_ANSWER (OK)
-- INSERTING QUESTIONNAIRE_ANSWER HAVING ANSWER ID ON CHALLENGE_ANSWER (OK)

DO SELECT * FROM CHALLENGE_ANSWER
$$
BEGIN
IF EXISTS(SELECT * FROM QUESTIONNAIRE_ANSWER QA WHERE QA.answer_id = 13 )
THEN
	RAISE EXCEPTION 'Already exists an QUESTIONNAIRE_ANSWER with this ANSWER id';
END IF;
END
$$









