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
value:`print("Hello World!")`
});

export const defaultUnitTests = {
    java: "import org.junit.Assert;import org.junit.Test;public class unitTests {@Test public void TestCase1() {Assert.assertTrue(true);}}",
    kotlin: "import org.junit.Assert import org.junit.Test class UnitTests {@Test fun foo() {Assert.assertTrue(true);}}",
    javascript: "should contain default test code for javascript",
    csharp: "should contain default test code for C#",
    python: "import unittest\r\n\r\ndef foo():\r\n    return  True\r\n\r\nclass TestChallenge(unittest.TestCase):\r\n    def test(self):\r\n        self.assertTrue(foo())\r\n\r\nif __name__ == '__main__':\r\n    unittest.main()"
}

export const API_VERSION = "v0"

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
    createUser: ()=> `/${API_VERSION}/users`,
    myUserOperations: () => `/${API_VERSION}/users/me`,
    myCredentials: () => `/${API_VERSION}/users/me/password`,
    // RUN CODE
    executeCode: () => `/${API_VERSION}/execute`,
    // LANGUAGES
    languages: () => `/${API_VERSION}/codeLanguages`,
    // CHALLENGES
    challenge: (challengeId) => `/${API_VERSION}/challenges/${challengeId}`,
    challenges: () => `/${API_VERSION}/challenges`,
    challengeAnswerByChallengeIdAndUserId: (challengeId, userId) => `/${API_VERSION}/challengeAnswers/challenges/${challengeId}/users/${userId}`,
    challengeAnswer: (challengeAnswerId) => `/${API_VERSION}/challengeAnswers/${challengeAnswerId}`,
    challengeAnswers: () => `/${API_VERSION}/challengeAnswers`,
    getAllChallenges: () => `/${API_VERSION}/challenges`,
    getRandomChallenge: () => `/${API_VERSION}/challenges/random`,
    // QUESTIONNAIRES
    createQuestionnaire: () => `/${API_VERSION}/questionnaires/withChallenges`,
    getQuestionnaireWithChallenges: (questionnaireId) => `/${API_VERSION}/questionnaires/${questionnaireId}/withChallenges`,
    getQuestionnaireById: (id) => `/${API_VERSION}/questionnaires/${id}`,
    getAllUserQuestionnaires: (id) => `/${API_VERSION}/questionnaires/users`,
    saveQuestionnaire: () => `/${API_VERSION}/questionnaires/{id}`,
    // QUESTIONNAIRE INSTANCES
    createQuestionnaireInstance: () => `/${API_VERSION}/questionnaireInstances`,
    getAllQuestionnaireInstancesByQuestionnaireId: (questionnaireId) => `/${API_VERSION}/questionnaireInstances/questionnaires/${questionnaireId}`,
    // QUESTIONNAIRE ANSWERS
    createQuestionnaireAnswer: () => `/${API_VERSION}/questionnaireAnswers`, 
    getAllQuestionnaireAnswersFromQuestionnaireCreator: () => `/${API_VERSION}/questionnaireAnswers`,
    getQuestionnaireAnswers: (id) => `/${API_VERSION}/questionnaireAnswers/questionnaireInstances/${id}`,   

}