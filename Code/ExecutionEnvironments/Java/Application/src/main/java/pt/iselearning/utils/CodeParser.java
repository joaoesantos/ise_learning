package pt.iselearning.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeParser {
    /**
     * Extract public class name from text, return null if unsuccessful.
     *
     * @param codeText
     * @return
     */
    public static String extractClassName(String codeText) {
        Pattern p = Pattern.compile("public[\\s]+class[\\s]+([^{]+)[{]");
        Matcher m = p.matcher(codeText);
        return m.find() ? m.group(1).trim() : null;
    }
}
