package me.adegokeobasa.gitwatch.utils;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class StringUtils {
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}
