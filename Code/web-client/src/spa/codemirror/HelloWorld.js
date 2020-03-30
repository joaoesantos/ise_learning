var map = new Map();

// JAVA
map.set('java',{
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
map.set('kotlin',{
mode:'text/x-java',
value:
`fun main(args: Array<String>) {
    println("Hello, World!")
}`
});
// JAVASCRIPT
map.set('javascript',{
mode:'javascript',
value:
`(function main() {
    console.log('Hello World!');
  }())`
});
// C#
map.set('csharp',{
mode:'csharp',
value:
`class HelloWorld {
    static void Main() {
        System.Console.WriteLine("Hello World!");
    }
}`
});
// PYTHON
map.set('python',{
mode:'python',
value:`print "Hello World!"`
});

export { map }