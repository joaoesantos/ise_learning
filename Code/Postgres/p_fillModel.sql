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

	INSERT INTO code_language (code_language) VALUES
	('Java'),('Kotlin'),('JavaScript'),('C#'),('Python');

	INSERT INTO tag (tag) VALUES
	('Array'),('Divide and Conquer'),('Hash Table'),('Recursion');

	INSERT INTO runcode(code_language,code,output) VALUES
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

	INSERT INTO ct(challenge_id,tag) VALUES
	(1,'Array'),(1,'Divide and Conquer'),(1,'Recursion'),
	(2,'Divide and Conquer'),
	(3,'Array'),
	(4,'Divide and Conquer'),
	(5,NULL);
	
	INSERT INTO challenge_solution(challenge_id,code_language,challenge_code,solution_code,unit_tests) VALUES
	-- CHALLENGE 1 EM JAVA
	(
		1,
		'Java',
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
		'Kotlin',
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
		'JavaScript',
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
		'C#',
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
		'Python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_1!")',
		TRUE
	),
	-- CHALLENGE 2 EM JAVA
	(
		2,
		'Java',
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
		'Kotlin',
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
		'JavaScript',
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
		'C#',
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
		'Python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_2!")',
		TRUE
	),
	-- CHALLENGE 3 EM JAVA
	(
		3,
		'Java',
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
		'Kotlin',
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
		'JavaScript',
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
		'C#',
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
		'Python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_3!")',
		TRUE
	),
	-- CHALLENGE 4 EM JAVA
	(
		4,
		'Java',
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
		'Kotlin',
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
		'JavaScript',
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
		'C#',
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
		'Python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_5!")',
		TRUE
	);

	-- INSERT INTO CHALLENGE_ANSWER
	CALL p_insertchallengeanswer
	(
		2, -- user
		1, -- challenge
		'Python',
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		2, -- user
		2, -- challenge
		'Java',
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
		'Python',
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		4, -- user
		2, -- challenge
		'Java',
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
		'Kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		1, -- challenge
		'Python',
		'print("Hello World!")',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		2, -- challenge
		'Java',
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
		'Kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		4, -- challenge
		'JavaScript',
		'function main() {
			console.log("Hello World!");
		}());',
		'TRUE'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		5, -- challenge
		'C#',
		'class HelloWorld {
			static void Main() {
				System.Console.WriteLine("Hello World!");
			}
		}',
		'TRUE'
	);


	INSERT INTO questionnaire(creator_id,timer) VALUES
	(1,3600000),
	(3,3600000),
	(3,0);

	INSERT INTO qc(questionnaire_id,challenge_id,lang_filter) VALUES
	(1,1,'Python'),(1,2,'Java'),(1,3,'Kotlin'),
	(2,2,'Java'),(2,3,'Kotlin'),(2,4,'JavaScript'),(2,5,'C#');
	
	-- INSERT INTO QUESTIONNAIRE_ANSWER
	CALL p_insertquestionnaireanswer(
		1, -- questionnaire
		1, -- qc_id 1 -> challenge 1
		'Python',
		'print("Hello World!")',
		'TRUE',
		'label1'
	);
	
	CALL p_insertquestionnaireanswer(
		1, -- questionnaire
		2, -- qc_id 2 -> challenge 2
		'Java',
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
		3, -- qc_id 3 -> challenge 3
		'Kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE',
		'label1'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		4, -- qc_id 4 -> challenge 2
		'Java',
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
		5, -- qc_id 5 -> challenge 3
		'Kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE',
		'label2'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		6, -- qc_id 6 -> challenge 4
		'JavaScript',
		'function main() {
			console.log("Hello World!");
		}());',
		'TRUE',
		'label2'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		7, -- qc_id 7 -> challenge 5
		'C#',
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
