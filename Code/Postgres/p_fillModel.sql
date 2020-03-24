------------------------------------
--          FILL MODEL            --
------------------------------------
SET search_path TO ise_learning,public;

CREATE OR REPLACE PROCEDURE p_fillmodel()
LANGUAGE 'plpgsql'

AS $BODY$
BEGIN
	SET search_path TO ise_learning,public;

	INSERT INTO USERS(username,password,name,email) VALUES
	('user1','user1','User One','user1@example.com'),	-- created challenge 1,2 & questionnaire 1
	('user2','user2','User Two','user2@example.com'),	-- created challenge 3,4 & answered challenge 1,2
	('user3','user3','User Three','user3@example.com'),	-- created challenge 5 & questionnaire 2,3
	('user4','user4','User Four','user4@example.com'),	-- answered challenge 1,2 & answered questionnaire 1,2
	('user5','user5','User Five','user5@example.com');	-- answered challenge 1,2,3,4,5 & answered questionnaire 1,2,3
	
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

	INSERT INTO CODELANGUAGE (codeLanguage) VALUES
	('Java'),('Kotlin'),('JavaScript'),('C#'),('Python');

	INSERT INTO TAG (tag) VALUES
	('Array'),('Divide and Conquer'),('Hash Table'),('Recursion');

	INSERT INTO RUNCODE(codeLanguage,code,output) VALUES
	-- JAVA
	(
		'Java',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		'Hello World!'
	),
	-- KOTLIN
	(
		'Kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'Hello World!'
	),
	-- JAVASCRIPT
	(
		'JavaScript',
		'function main() {
			console.log("Hello World!");
		}());',
		'Hello World!'
	),
	-- C#
	(
		'C#',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World!");
			}
		}',
		'Hello World!'
	),
	-- PYTHON
	(
		'Python',
		'print("Hello World!")',
		'Hello World!'
	);

	INSERT INTO CHALLENGE(creatorID,codeLanguage,challengeText,challengeCode,solutionCode,unitTests,privacy) VALUES
	-- CHALLENGE 1
	(
		1,
		'Python',
		'Type on the console the sentence <Hello World!>',
		'#WRITE YOUR CODE HERE',
		'print("Hello World!")',
		TRUE,
		FALSE
	),
	-- CHALLENGE 2
	(
		1,
		'Java',
		'Type on the console the sentence <Hello World!>',
		'public class Main {
			public static void main(String[] args) {
				// WRITE YOUR CODE HERE
			}
		}',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		TRUE,
		FALSE
	),
	-- CHALLENGE 3
	(
		2,
		'Java',
		'Type on the console the sentence <Hello World!>',
		'public class Main {
			public static void main(String[] args) {
				// WRITE YOUR CODE HERE
			}
		}',
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		TRUE,
		TRUE
	),
	-- CHALLENGE 4
	(
		2,
		'JavaScript',
		'Type on the console the sentence <Hello World!>',
		'function main() {
			// WRITE YOUR CODE HERE
		}());',
		'function main() {
			console.log("Hello World!");
		}());',
		TRUE,
		TRUE
	),
	-- CHALLENGE 5
	(
		3,
		'C#',
		'Type on the console the sentence <Hello World!>',
		'class HelloWorld {
			static void Main() {
				// WRITE YOUR CODE HERE;
			}
		}',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World!");
			}
		}',
		TRUE,
		FALSE
	);

	INSERT INTO CT(challengeID,tag) VALUES
	(1,'Array'),(1,'Divide and Conquer'),(1,'Recursion'),
	(2,'Divide and Conquer'),
	(3,'Array'),
	(4,'Divide and Conquer'),
	(5,NULL);
	
	-- INSERT INTO CHALLENGE_ANSWER
	CALL p_insertchallengeanswer
	(
		2, -- user
		1, -- challenge
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		2, -- user
		2, -- challenge 
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
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		4, -- user
		2, -- challenge 
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
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		1, -- challenge
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		2, -- challenge 
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
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		4, -- challenge
		'function main() {
			console.log("Hello World!");
		}());',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		5, -- challenge
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World!");
			}
		}',
		'TRUE'
	);


	INSERT INTO QUESTIONNAIRE(creatorID,timer) VALUES
	(1,3600000),
	(3,3600000),
	(3,0);

	INSERT INTO QC(questionnaireID,challengeID) VALUES
	(1,1),(1,2),(1,3),
	(2,2),(2,3),(2,4),(2,5);
	
	-- INSERT INTO QUESTIONNAIRE_ANSWER
	CALL p_insertquestionnaireanswer(
		1, -- questionnaire
		1, -- qcID 1 -> challenge 1
		'print("Hello World!")',
		'TRUE',
		'label1'
	);
	
	CALL p_insertquestionnaireanswer(
		1, -- questionnaire
		2, -- qcID 2 -> challenge 2
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		'TRUE',
		'label1'
	);
	
	CALL p_insertquestionnaireanswer(
		1, -- questionnaire
		3, -- qcID 3 -> challenge 3
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE',
		'label1'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		4, -- qcID 4 -> challenge 2
		'public class Main {
			public static void main(String[] args) {
				System.out.println("Hello World!");
			}
		}',
		'TRUE',
		'label2'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		5, -- qcID 5 -> challenge 3
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE',
		'label2'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		6, -- qcID 6 -> challenge 4
		'function main() {
			console.log("Hello World!");
		}());',
		'TRUE',
		'label2'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		7, -- qcID 7 -> challenge 5
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World!");
			}
		}',
		'TRUE',
		'label2'
	);

    COMMIT;
END;
$BODY$;
