package com.giljulio.sublingo.merge.technique.impl;

import com.giljulio.sublingo.Constants;
import com.giljulio.sublingo.merge.Utils;
import com.giljulio.sublingo.merge.model.PredictedSubtitle;
import com.giljulio.sublingo.merge.technique.MergeTechnique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Uses the levenshtein distance to check the distance from
 * each and all the subtitles and picks the one which takes
 * the least amount of changes to reach to the subtitle which
 * takes least amount of changes to change to all the other subtitles
 *
 * WARNING: This takes exponential time to complete
 */
public class EditDistanceSentence extends MergeTechnique {

    @Override
    public String getTechniqueName() {
        return "Edit Distance Sentence";
    }

    @Override
    public PredictedSubtitle execute() {
        int count = getSubtitles().size();
        int distance[][] = new int[count][count];


        for (int i = 0; i < distance.length; i++) {
            for (int j = 0; j < distance[i].length; j++) {
                distance[i][j] = Utils.computeLevenshteinDistance(getSubtitles().get(i), getSubtitles().get(j));
            }
        }


        if (Constants.VERBOSE_MODE) {
            for (int i = 0; i < count; i++) {
                System.out.println(Arrays.toString(distance[i]));
            }
        }

        ArrayList<PredictedSubtitle> predictedSubtitles = new ArrayList<>();
        for (int i = 0; i < distance.length; i++) {
            int total = 0;
            for (int j = 0; j < distance[i].length; j++) {
                total += distance[i][j];
            }
            predictedSubtitles.add(new PredictedSubtitle(getSubtitles().get(i), total));
        }

        predictedSubtitles.sort(new Comparator<PredictedSubtitle>() {
            @Override
            public int compare(PredictedSubtitle o1, PredictedSubtitle o2) {
                if (o1.getProbability() == o2.getProbability())
                    return 0;
                return o1.getProbability() < o2.getProbability() ? -1 : 1;
            }
        });



        if (Constants.VERBOSE_MODE) {
            for (PredictedSubtitle predictedSubtitle : predictedSubtitles) {
                System.out.println(predictedSubtitle.getSubtitle() + " ------- " + predictedSubtitle.getProbability());
            }
        }

        return predictedSubtitles.get(0);
    }
    
    
    
    
    
    public PredictedSubtitle ex3ecute() {
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
            System.out.println("\n" + strings[0] + "\n" +
                    "" + strings[1] + "\n" +
                    "Score: " + Utils.computeLevenshteinDistance(strings[0], strings[1]) + "/" + Math.max(strings[0].length(), strings[1].length()) + " - " +
                    ((double) Utils.computeLevenshteinDistance(strings[0], strings[1]) / (double)Math.max(strings[0].length(), strings[1].length())) * 100 + "% inconsistency" +"\n\n");
        }
        return null;
    }

}
