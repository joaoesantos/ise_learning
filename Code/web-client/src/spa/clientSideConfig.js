//export const apiBaseUrl = "http://localhost:3000";
export const apiBaseUrl = "http://localhost:8080";
export const defaultLanguage = 'java';
export const CodeMirrorOptions = new Map();

// JAVA
CodeMirrorOptions.set('java',{
mode:'text/x-java',
value:
`// "static void main" must be defined in a public class.
public class Main {
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
    profile: () => `/${apiVersion}/users/me`,
    executeCode: () => `/${apiVersion}/execute`,
    challenge: (challengeId) => `/${apiVersion}/challenges/${challengeId}`,
    challenges: () => `/${apiVersion}/challenges`,
    languages: () => `/${apiVersion}/codeLanguages`,
}