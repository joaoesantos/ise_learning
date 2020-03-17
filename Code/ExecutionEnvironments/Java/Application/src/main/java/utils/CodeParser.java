package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeParser {
    /**
     * Removes end lines and duplicates spaces from text.
     *
     * @param text
     * @return
     */
    public static String removeEndLinesAndDuplicateSpaces(String text) {
        return text.replaceAll("[\n|\r\n]+"," ").replaceAll("[\\s]{2,}", " ");
    }

    /**
     * Extract public class name from text, return null if unsuccessful.
     *
     * @param codeText
     * @return
     */
    public static String extractClassName(String codeText) {
        Pattern p = Pattern.compile("public\\sclass\\s([^{]+)[{]");
        Matcher m = p.matcher(codeText);
        return m.find() ? m.group(1).trim() : null;
    }
}
