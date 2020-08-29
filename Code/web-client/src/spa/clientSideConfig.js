//export const apiBaseUrl = "http://localhost:3000";
export const apiBaseUrl = "http://localhost:8080";
export const defaultLanguage = 'java';
export const CodeMirrorOptions = new Map();

// JAVA
CodeMirrorOptions.set('java',{
mode:'text/x-java',
value:
`public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}`
});
// KOTLIN
CodeMirrorOptions.set('kotlin',{
mode:'text/x-java',
value:
`fun main(args: Array<String>) {
    println("Hello, World!")
}`
});
// JAVASCRIPT
CodeMirrorOptions.set('javascript',{
mode:'javascript',
value:
`(function main() {
    console.log('Hello World!');
}())`
});
// C#
CodeMirrorOptions.set('csharp',{
mode:'text/x-csharp',
value:
`class HelloWorld {
    static void Main() {
        System.Console.WriteLine("Hello World!");
    }
}`
});
// PYTHON
CodeMirrorOptions.set('python',{
mode:'python',
value:`print "Hello World!"`
});

export const defaultUnitTests = {
    java: "import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {Assert.assertTrue(true);}}",
    kotlin: "import org.junit.Assert import org.junit.Test class UnitTests {@Test fun foo() {Assert.assertTrue(true);}}",
    javascript: "should contain default test code for javascript",
    csharp: "should contain default test code for C#",
    python: "should contain default test code for python"
}

export const apiVersion = "v0";

export const languageLabelMappings = {
    java: "Java",
    kotlin: "Kotlin",
    javascript: "JavaScript",
    csharp: "C#",
    python: "Python"
}

export const apiUrlTemplates = {
    login: () => `/${apiVersion}/login`,
    logout: () => `/${apiVersion}/logout`,
<<<<<<< HEAD
    myUserOperations: () => `/${apiVersion}/users/me`,
    myCredentials: () => `/${apiVersion}/users/me/password`,
=======
>>>>>>> 6eb91f640aa7b3e37957c43e8e45cfb520a90391
    profile: () => `/${apiVersion}/users/me`,
    executeCode: () => `/${apiVersion}/execute`,
    challenge: (challengeId) => `/${apiVersion}/challenges/${challengeId}`,
    challenges: () => `/${apiVersion}/challenges`,
    challengeAnswerByChallengeIdAndUserId: (challengeId, userId) => `/${apiVersion}/challengeAnswers/${challengeId}/answers/users/${userId}`,
    challengeAnswer: (challengeAnswerId) => `/${apiVersion}/challengeAnswers/${challengeAnswerId}`,
    challengeAnswers: () => `/${apiVersion}/challengeAnswers`,
    languages: () => `/${apiVersion}/codeLanguages`,
}