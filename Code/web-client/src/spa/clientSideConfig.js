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

export const apiVersion = "v0";

export const apiUrlTemplates = {
    login: () => `/${apiVersion}/login`,
    logout: () => `/${apiVersion}/logout`,
    createUser:()=> `/${apiVersion}/users`,
    myUserOperations: () => `/${apiVersion}/users/me`,
    myCredentials: () => `/${apiVersion}/users/me/password`,
    getAllChallenges: () => `/${apiVersion}/challenges`,
    getRandomChallenge: () => `/${apiVersion}/challenges/random`,
    getQuestionnaireInstances: () => `/${apiVersion}/questionnaireAnswers`,
    getQuestionnaireAnswers: (id) => `/${apiVersion}/questionnaireAnswers/questionnaireInstances/${id}`,
    getChallenges: () => `/${apiVersion}/challenges`,
    getQuestionnaire: () => `/${apiVersion}/questionnaires/{id}`,
    saveQuestionnaire: () => `/${apiVersion}/questionnaires/{id}`,
    createQuestionnaire: () => `/${apiVersion}/questionnaires/withChallenges`,
    getQuestionnaireWithChallenges: () => `/${apiVersion}/questionnaires/{questionnaireId}/withChallenges`
}