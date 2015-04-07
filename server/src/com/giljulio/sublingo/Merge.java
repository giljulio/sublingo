package com.giljulio.sublingo;

import java.util.logging.Logger;

/**
 * Created by Gil on 20/11/14.
 */
public class Merge {

    private static Logger log = Logger.getLogger(Merge.class.getSimpleName());

    public static void main(String...args){
        String[][] data = new String[][]{
                new String[]{"The dog ran and ran", "The dog ran and ran"},
                new String[]{"Hey Hey", "Yo"},
                new String[]{"Gil Julio", "Jill Hulio"},
                new String[]{"AAA", "BBB"},
                new String[]{"AAAAAAAAAAAAAA", "A"},
                new String[]{"B", "A"},
                new String[]{"Daniel Gil", "Gil Daniel"}
        };
        for(String[] strings : data){
            log.info("\n" + strings[0] + "\n" +
                    "" + strings[1] + "\n" +
                    "Score: " + computeLevenshteinDistance(strings[0], strings[1]) + "/" + Math.max(strings[0].length(), strings[1].length()) + " - " +
                    ((double)computeLevenshteinDistance(strings[0], strings[1]) / (double)Math.max(strings[0].length(), strings[1].length())) * 100 + "% inconsistency" +"\n\n");
        }
    }

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static int computeLevenshteinDistance(String str1,String str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++)
            distance[i][0] = i;
        for (int j = 1; j <= str2.length(); j++)
            distance[0][j] = j;

        for (int i = 1; i <= str1.length(); i++)
            for (int j = 1; j <= str2.length(); j++)
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));

        return distance[str1.length()][str2.length()];
    }
}
