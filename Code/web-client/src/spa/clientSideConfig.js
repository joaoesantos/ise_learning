export const defaultLanguage = 'java'
export const CodeMirrorOptions = new Map()

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
    python: "import unittest\r\n\r\ndef foo():\r\n    return  True\r\n\r\nclass TestChallenge(unittest.TestCase):\r\n    def test(self):\r\n        self.assertTrue(foo())\r\n\r\nif __name__ == '__main__':\r\n    unittest.main()"
}

const API_VERSION = "v0"

export const languageLabelMappings = {
    java: "Java",
    kotlin: "Kotlin",
    javascript: "JavaScript",
    csharp: "C#",
    python: "Python"
}

export const feloniousStatusCodes = [
    403,    // Forbidden
    404,    // Not Found
    500     // Internal Server Error
]

export const apiUrlTemplates = {
    // USER
    login: () => `/${API_VERSION}/login`,
    logout: () => `/${API_VERSION}/logout`,
    createUser:()=> `/${API_VERSION}/users`,
    myUserOperations: () => `/${API_VERSION}/users/me`,
    myCredentials: () => `/${API_VERSION}/users/me/password`,
    // RUN CODE
    executeCode: () => `/${API_VERSION}/execute`,
    // CHALLENGES
    challenge: (challengeId) => `/${API_VERSION}/challenges/${challengeId}`,
    challenges: () => `/${API_VERSION}/challenges`,
    challengeAnswerByChallengeIdAndUserId: (challengeId, userId) => `/${API_VERSION}/challengeAnswers/${challengeId}/answers/users/${userId}`,
    challengeAnswer: (challengeAnswerId) => `/${API_VERSION}/challengeAnswers/${challengeAnswerId}`,
    challengeAnswers: () => `/${API_VERSION}/challengeAnswers`,
    languages: () => `/${API_VERSION}/codeLanguages`,
    getAllChallenges: () => `/${API_VERSION}/challenges`,
    getRandomChallenge: () => `/${API_VERSION}/challenges/random`,
    // QUESTIONNAIRES
    getQuestionnaireInstances: () => `/${API_VERSION}/questionnaireAnswers`,
    getQuestionnaireAnswers: (id) => `/${API_VERSION}/questionnaireAnswers/questionnaireInstances/${id}`,
    getChallenges: () => `/${API_VERSION}/challenges`,
    getQuestionnaire: () => `/${API_VERSION}/questionnaires/{id}`,
    saveQuestionnaire: () => `/${API_VERSION}/questionnaires/{id}`,
    createQuestionnaire: () => `/${API_VERSION}/questionnaires/withChallenges`,
    getQuestionnaireWithChallenges: () => `/${API_VERSION}/questionnaires/{questionnaireId}/withChallenges`,
    createQuestionnaireAnswer: () => `/${API_VERSION}/questionnaireAnswers`
}