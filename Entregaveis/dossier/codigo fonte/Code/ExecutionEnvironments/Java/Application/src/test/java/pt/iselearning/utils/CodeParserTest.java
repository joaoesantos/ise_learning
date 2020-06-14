package pt.iselearning.utils;

import org.junit.Assert;
import org.junit.Test;

public class CodeParserTest {
    private static String codeExample = "public class                                     code { \r\n \r\n \r\n \r\n \r\npublic static void main(String[] args) {  \r\n  System.out.println(\"Hello World\");  \r\n}  \r\n}";

    @Test
    public void testRemoveEndLinesAndDuplicateSpaces() {
        String expected = "public class code { public static void main(String[] args) { System.out.println(\"Hello World\"); } }";
        String actual = CodeParser.removeEndLinesAndDuplicateSpaces(codeExample);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testExtractClassName() {
        String expected = "code";
        String actual = CodeParser.extractClassName(codeExample);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testExtractClassNameCannotParseClass() {
        String codeExample = "public classwerwerwrtw code { public static void main(String[] args) { System.out.println(\"Hello World\"); } }";
        String actual = CodeParser.extractClassName(codeExample);
        Assert.assertEquals(null, actual);
    }
}
