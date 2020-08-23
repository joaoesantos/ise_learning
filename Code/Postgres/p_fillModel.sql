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
		'import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {Assert.assertTrue(true);}}'
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
		'import org.junit.Assert import org.junit.Test class UnitTests {@Test fun foo() {Assert.assertTrue(true);}}'
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
		''
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
		''
	),
	-- CHALLENGE 1 EM PYTHON
	(
		1,
		'python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_1!")',
		''
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
		'import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {Assert.assertTrue(true);}}'
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
		'import org.junit.Assert import org.junit.Test class UnitTests {@Test fun foo() {Assert.assertTrue(true);}}'
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
		''
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
		''
	),
	-- CHALLENGE 2 EM PYTHON
	(
		2,
		'python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_2!")',
		''
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
		'import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {Assert.assertTrue(true);}}'
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
		'import org.junit.Assert import org.junit.Test class UnitTests {@Test fun foo() {Assert.assertTrue(true);}}'
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
		''
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
		''
	),
	-- CHALLENGE 3 EM PYTHON
	(
		3,
		'python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_3!")',
		''
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
		''
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
		'import org.junit.Assert import org.junit.Test class UnitTests {@Test fun foo() {Assert.assertTrue(true);}}'
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
		''
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
		''
	),
	-- CHALLENGE 5 EM PYTHON
	(
		5,
		'python',
		'#WRITE YOUR CODE HERE',
		'print("Hello World_5!")',
		''
	);

	-- INSERT INTO CHALLENGE_ANSWER
	CALL p_insertchallengeanswer
	(
		2, -- user
		1, -- challenge
		'python',
		'print("Hello World!")',
		''
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
			public static void test() {System.out.println(\"<<Test>>\");}  \r\n
		}',
		'import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {code.test(); Assert.assertTrue(true);}}'
	);
	
	CALL p_insertchallengeanswer
	(
		4, -- user
		1, -- challenge
		'python',
		'print("Hello World!")',
		''
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
		'import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {Assert.assertTrue(true);}}'
	);
	
	CALL p_insertchallengeanswer
	(
		4, -- user
		3, -- challenge
		'kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}
		fun cenas() {println(\"<<Test>>\")}
		',
		'import org.junit.Assert import org.junit.Test class UnitTests {@Test fun foo() {cenas(); Assert.assertTrue(true);}}'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		1, -- challenge
		'python',
		'print("Hello World!")',
		''
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
		'import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {Assert.assertTrue(true);}}'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		3, -- challenge
		'kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'import org.junit.Assert import org.junit.Test class UnitTests {@Test fun foo() {Assert.assertTrue(true);}}"'
	);
	
	CALL p_insertchallengeanswer
	(
		5, -- user
		4, -- challenge
		'javascript',
		'function main() {
			console.log("Hello World!");
		}());',
		''
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
		''
	);


	INSERT INTO questionnaire(creator_id,timer) VALUES
	(1,3600000),
	(3,3600000),
	(3,0);

	INSERT INTO qc(questionnaire_id,challenge_id,lang_filter) VALUES
	(1,1,'python'),(1,2,'java'),(1,3,'kotlin'),
	(2,2,'java'),(2,3,'kotlin'),(2,4,'javascript'),(2,5,'csharp');
	
	-- INSERT INTO QUESTIONNAIRE_ANSWER
	CALL p_insertquestionnaireanswer(
		1, -- questionnaire
		1, -- qc_id 1 -> challenge 1
		'python',
		'print("Hello World!")',
		'TRUE',
		'label1'
	);
	
	CALL p_insertquestionnaireanswer(
		1, -- questionnaire
		2, -- qc_id 2 -> challenge 2
		'java',
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
		'kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE',
		'label1'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		4, -- qc_id 4 -> challenge 2
		'java',
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
		'kotlin',
		'fun main(args: Array<String>) {
			println("Hello, World!")
		}',
		'TRUE',
		'label2'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		6, -- qc_id 6 -> challenge 4
		'javascript',
		'function main() {
			console.log("Hello World!");
		}());',
		'TRUE',
		'label2'
	);
	
	CALL p_insertquestionnaireanswer(
		2, -- questionnaire
		7, -- qc_id 7 -> challenge 5
		'csharp',
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
