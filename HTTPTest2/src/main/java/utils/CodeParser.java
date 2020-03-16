package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeParser {
    public static String removeEndLinesAndDuplicateSpaces(String text) {
        return text.replaceAll("[\n|\r\n]+"," ").replaceAll("[\\s]{2,}", " ");
    }

    public static String extractClassName(String codeText) {
        Pattern p = Pattern.compile("public\\sclass\\s([^{]+)[{]");
        Matcher m = p.matcher(codeText);
        return m.find() ? m.group(1).trim() : null;
    }
}
