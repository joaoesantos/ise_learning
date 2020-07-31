------------------------------------
--          FILL MODEL            --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_fillmodel()
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	SET search_path TO ise_learning,public;

	INSERT INTO app_user(username,password,name,email) VALUES
	--pass:user1
	('user1','$2a$10$BgowVRiHeJaoV5kOoBgsZebOUgvdgbF97/9etCKlMqZqIEhg1GgZu','User One','user1@example.com'),	-- created challenge 1,2 & questionnaire 1
	--pass:user2
	('user2','$2a$10$B72054TXKodEi.QdGF9HTOoWxO9f//tzz6QU.xplQMx4l.SXSDaSK','User Two','user2@example.com'),	-- created challenge 3,4 & answered challenge 1,2
	--pass:user3
	('user3','$2a$10$UDUU1KqrjclkUGF9MegNbOGbNeyIezZWw93cTrwcZSWCKR5UkZRka','User Three','user3@example.com'),	-- created challenge 5 & questionnaire 2,3
	--pass:user4
	('user4','$2a$10$FnkxyonQk/iGxAb/alhcauKvgkAT9d6tmhfKg105a3N1z8FxbGq.e','User Four','user4@example.com'),	-- answered challenge 1,2 & answered questionnaire 1,2
	--pass:user5
	('user5','$2a$10$ZaGvwc4pJM9Me2csCcCVZ.BwndB4vuJbrUNzqkywyP8klV9gxAk0O','User Five','user5@example.com');	-- answered challenge 1,2,3,4,5 & answered questionnaire 1,2,3
	
	-- USER ACTIONS
	-- ------------------------------------------------------------------------------
	-- | XX | C1 | C2 | C3 | C4 | C5 || CA1 | CA2 | CA3 | CA4 | CA5 || Q1 | Q2 | Q3 |
	-- ------------------------------------------------------------------------------
	-- | U1	| C  | C  |	   |	|    ||     |     |     |     |     || C  |    |    |  -- CREATOR
	-- | U2	|	 |	  |	C  | C  |    || A   | A   |     |     |     ||    |    |    |  -- CREATOR
	-- | U3	|	 |	  |	   |	| C  ||     |     |     |     |     ||    | C  | C  |  -- CREATOR
	-- | U4	| 	 |    |	   |	|    || A   | A   |     |     |     || A  |    |    |  -- MEMBER
	-- | U5	| A	 | A  |	A  |	|    || A   | A   | A   | A   | A   || A  | A  | A  |  -- MEMBER
	-- ------------------------------------------------------------------------------ 
	
	-- CHALLENGES ON QUESIONAIRES ANSWERS
	-- ---------------------
	-- | XX | Q1 | Q2 | Q3 |
	-- ---------------------	
	-- | C1 | X  |    |	   |
	-- | C2 | X  | X  |	   |
	-- | C3 | X  | X  |	   |
	-- | C4 |    | X  |	   |
	-- | C5 |    | X  |	   |

	INSERT INTO code_language (code_language) VALUES
	('java'),('kotlin'),('javascript'),('csharp'),('python');

	INSERT INTO tag (tag) VALUES
	('Array'),('Divide and Conquer'),('Hash Table'),('Recursion');

	INSERT INTO challenge(creator_id,challenge_text,is_private) VALUES
	-- CHALLENGE 1
	(
		1,
		'Type on the console the sentence <Hello World_1!>',
		FALSE
	),
	-- CHALLENGE 2
	(
		1,
		'Type on the console the sentence <Hello World_2!>',
		FALSE
	),
	-- CHALLENGE 3
	(
		2,
		'Type on the console the sentence <Hello World_3!>',
		TRUE
	),
	-- CHALLENGE 4
	(
		2,
		'Type on the console the sentence <Hello World_4!>',
		TRUE
	),
	-- CHALLENGE 5
	(
		3,
		'Type on the console the sentence <Hello World_5!>',
		FALSE
	);

	INSERT INTO ct(challenge_id,tag_id) VALUES
	(1,1),(1,2),(1,4),
	(2,2),
	(3,1),
	(4,2),
	(5,NULL);
	
	INSERT INTO challenge_solution(challenge_id,code_language,challenge_code,solution_code,unit_tests) VALUES
	-- CHALLENGE 1 EM JAVA
	(
		1,
		'java',
		'public class Main {
			public static void main(String[] args) {
				// WRITE YOUR CODE HERE
			}
		}',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World_1!");
			}
		}',
		TRUE
	),
	-- CHALLENGE 1 EM KOTLIN
	(
		1,
		'kotlin',
		'fun main(args: Array<String>) {
			// WRITE YOUR CODE HERE
		}',
		'fun main(args: Array<String>) {
			println("Hello, World_1!")
		}',
		TRUE
	),
	-- CHALLENGE 1 EM JAVASCRIPT
	(
		1,
		'javascript',
		'function main() {
			// WRITE YOUR CODE HERE
		}());',
		'function main() {
			console.log("Hello World_1!");
		}());',
		TRUE
	),
	-- CHALLENGE 1 EM C#
	(
		1,
		'csharp',
		'class HelloWorld {
			static void Main() {
				// WRITE YOUR CODE HERE;
			}
		}',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World_1!");
			}
		}',
		TRUE
	),
	-- CHALLENGE 1 EM PYTHON
	(
		1,
		'python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_1!")',
		TRUE
	),
	-- CHALLENGE 2 EM JAVA
	(
		2,
		'java',
		'public class Main {
			public static void main(String[] args) {
				// WRITE YOUR CODE HERE
			}
		}',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World_2!");
			}
		}',
		TRUE
	),
	-- CHALLENGE 2 EM KOTLIN
	(
		2,
		'kotlin',
		'fun main(args: Array<String>) {
			// WRITE YOUR CODE HERE
		}',
		'fun main(args: Array<String>) {
			println("Hello, World_2!")
		}',
		TRUE
	),
	-- CHALLENGE 2 EM JAVASCRIPT
	(
		2,
		'javascript',
		'function main() {
			// WRITE YOUR CODE HERE
		}());',
		'function main() {
			console.log("Hello World_2!");
		}());',
		TRUE
	),
	-- CHALLENGE 2 EM C#
	(
		2,
		'csharp',
		'class HelloWorld {
			static void Main() {
				// WRITE YOUR CODE HERE;
			}
		}',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World_2!");
			}
		}',
		TRUE
	),
	-- CHALLENGE 2 EM PYTHON
	(
		2,
		'python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_2!")',
		TRUE
	),
	-- CHALLENGE 3 EM JAVA
	(
		3,
		'java',
		'public class Main {
			public static void main(String[] args) {
				// WRITE YOUR CODE HERE
			}
		}',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World_3!");
			}
		}',
		TRUE
	),
	-- CHALLENGE 3 EM KOTLIN
	(
		3,
		'kotlin',
		'fun main(args: Array<String>) {
			// WRITE YOUR CODE HERE
		}',
		'fun main(args: Array<String>) {
			println("Hello, World_3!")
		}',
		TRUE
	),
	-- CHALLENGE 3 EM JAVASCRIPT
	(
		3,
		'javascript',
		'function main() {
			// WRITE YOUR CODE HERE
		}());',
		'function main() {
			console.log("Hello World_3!");
		}());',
		TRUE
	),
	-- CHALLENGE 3 EM C#
	(
		3,
		'csharp',
		'class HelloWorld {
			static void Main() {
				// WRITE YOUR CODE HERE;
			}
		}',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World_3!");
			}
		}',
		TRUE
	),
	-- CHALLENGE 3 EM PYTHON
	(
		3,
		'python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_3!")',
		TRUE
	),
	-- CHALLENGE 4 EM JAVA
	(
		4,
		'java',
		'public class Main {
			public static void main(String[] args) {
				// WRITE YOUR CODE HERE
			}
		}',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World_4!");
			}
		}',
		TRUE
	),
	-- CHALLENGE 4 EM KOTLIN
	(
		4,
		'kotlin',
		'fun main(args: Array<String>) {
			// WRITE YOUR CODE HERE
		}',
		'fun main(args: Array<String>) {
			println("Hello, World_4!")
		}',
		TRUE
	),
	-- CHALLENGE 5 EM JAVASCRIPT
	(
		5,
		'javascript',
		'function main() {
			// WRITE YOUR CODE HERE
		}());',
		'function main() {
			console.log("Hello World_5!");
		}());',
		TRUE
	),
	-- CHALLENGE 1 EM C#
	(
		5,
		'csharp',
		'class HelloWorld {
			static void Main() {
				// WRITE YOUR CODE HERE;
			}
		}',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World_5!");
			}
		}',
		TRUE
	),
	-- CHALLENGE 5 EM PYTHON
	(
		5,
		'python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_5!")',
		TRUE
	);

	-- INSERT INTO CHALLENGE_ANSWER
	CALL p_insertchallengeanswer
	(
		2, -- user
		1, -- challenge
		'python',
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		2, -- user
		2, -- challenge
		'java',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		4, -- user
		1, -- challenge
		'python',
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		4, -- user
		2, -- challenge
		'java',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		4, -- user
		3, -- challenge
		'kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		1, -- challenge
		'python',
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		2, -- challenge
		'java',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		3, -- challenge
		'kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		4, -- challenge
		'javascript',
		'function main() {
			console.log("Hello World!");
		}());',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		5, -- challenge
		'csharp',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World!");
			}
		}',
		'TRUE'
	);


	INSERT INTO questionnaire(description,timer,creator_id) VALUES
	('questionnaire 1',3600000,1),
	('questionnaire 2',3600000,3),
	('questionnaire 3',0,3);
	
	INSERT INTO questionnaire_instance(questionnaire_id,questionnaire_instance_uuid,description,timer,start_timestamp,end_timestamp) VALUES
	(1,'7eecc8ed-7cfb-4707-ac39-3ef02a852223','quest1 instance1',3600000,NULL,NULL),
	(1,'07b18bb8-085e-4396-ab64-9e20d1303499','quest1 instance2',0000000,NULL,NULL),
	(1,'259e6fe6-8b83-4c7f-b011-0727290085b7','quest1 instance3',3600000,NULL,NULL),
	(2,'8b3c46b1-0353-409e-aae8-9d033def559e','quest2 instance4',3600000,NULL,NULL),
	(2,'9f93f5fe-5abc-4d9f-ae26-31ebe21b3dc6','quest2 instance5',3600000,NULL,NULL),
	(2,'ee842bf4-1c42-4ef5-a997-9eb6caece31f','quest2 instance6',3600000,NULL,NULL),
	(2,'e1509f40-a093-41f9-bc6a-468a95165743','quest2 instance7',0000000,NULL,NULL);

	INSERT INTO qc(questionnaire_id,challenge_id,language_filter) VALUES
	(1,1,'python'),
	(1,2,'java'),
	(1,3,'kotlin'),
	(2,2,'java,kotlin'),
	(2,3,'kotlin,javascript'),
	(2,4,'javascript'),
	(2,5,'csharp,java');
	
	-- INSERT INTO QUESTIONNAIRE_ANSWER
	CALL p_insertquestionnaireanswer(
		1, -- questionnaire_instance
		1, -- qc_id 1 -> challenge 1
		'python',
		'print("Hello World!")',
		TRUE
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire_instance
		2, -- qc_id 2 -> challenge 2
		'java',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		'TRUE'
	);
	
	CALL p_insertquestionnaireanswer(
		3, -- questionnaire_instance
		3, -- qc_id 3 -> challenge 3
		'kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		TRUE
	);
	
	CALL p_insertquestionnaireanswer(
		4, -- questionnaire_instance
		4, -- qc_id 4 -> challenge 2
		'java',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		TRUE
	);
	
	CALL p_insertquestionnaireanswer(
		5, -- questionnaire_instance
		5, -- qc_id 5 -> challenge 3
		'kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE'
	);
	
	CALL p_insertquestionnaireanswer(
		6, -- questionnaire_instance
		6, -- qc_id 6 -> challenge 4
		'javascript',
		'function main() {
			console.log("Hello World!");
		}());',
		TRUE
	);
	
	CALL p_insertquestionnaireanswer(
		7, -- questionnaire_instance
		7, -- qc_id 7 -> challenge 5
		'csharp',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World!");
			}
		}',
		TRUE
	);

    COMMIT;
END;
$BODY$;
