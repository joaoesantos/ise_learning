package pt.iselearning.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeParser {
    /**
     * Extract class name from text, return null if unsuccessful.
     *
     * @param codeText
     * @return
     */
    public static String extractClassName(String codeText) {
        Pattern p = Pattern.compile("class[\\s]+([^{]+)[{]");
        Matcher m = p.matcher(codeText);
        return m.find() ? m.group(1).trim() : null;
    }
}
