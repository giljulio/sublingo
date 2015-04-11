package com.giljulio.sublingo.merge.technique.impl;

import com.giljulio.sublingo.Constants;
import com.giljulio.sublingo.merge.Utils;
import com.giljulio.sublingo.merge.model.PredictedSubtitle;
import com.giljulio.sublingo.merge.model.Word;
import com.giljulio.sublingo.merge.technique.MergeTechnique;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by Gil on 07/04/15.
 */
public class EditDistanceWord extends MergeTechnique {


    @Override
    public String getTechniqueName() {
        return "Edit Distance Word";
    }

    @Override
    public PredictedSubtitle execute() {
        int wordCount = getProbableWordCount();

        Iterator<String> it = getSubtitles().iterator();
        while (it.hasNext()) {
            if (getWordCount(it.next()) != wordCount) {
                it.remove();
            }
        }

        StringBuilder sentenceBuilder = new StringBuilder();

        String words[][] = new String[getSubtitles().size()][wordCount];

        for (int i = 0; i < words.length; i++){
            words[i] = getSubtitles().get(i).trim().split("\\s+");
        }

        for (int wordIndex = 0; wordIndex < wordCount; wordIndex++) {

            int distance[][] = new int[getSubtitles().size()][getSubtitles().size()];
            for (int i = 0; i < distance.length; i++) {
                for (int j = 0; j < distance[i].length; j++) {
                    distance[i][j] = Utils.computeLevenshteinDistance(words[i][wordIndex], words[j][wordIndex]);
                }
            }


            if (Constants.VERBOSE_MODE) {
                for (int i = 0; i < getSubtitles().size(); i++) {
                    System.out.println(Arrays.toString(distance[i]));
                }
                for (int i = 0; i < getSubtitles().size(); i++) {
                    System.out.print((words[i][wordIndex]) + " ");
                }
                System.out.println("\n\n\n");
            }

            ArrayList<Word> predictedWords = new ArrayList<>();
            for (int i = 0; i < distance.length; i++) {
                int total = 0;
                for (int j = 0; j < distance[i].length; j++) {
                    total += distance[j][i];
                }
                predictedWords.add(new Word(words[i][wordIndex], total));
            }

            predictedWords.sort(new Comparator<Word>() {
                @Override
                public int compare(Word o1, Word o2) {
                    if (o1.getProbability() == o2.getProbability())
                        return 0;
                    return o1.getProbability() < o2.getProbability() ? -1 : 1;
                }
            });


            sentenceBuilder.append("" + predictedWords.get(0).getWord() + " ");
        }

        return new PredictedSubtitle(sentenceBuilder.toString(), 0.8);
    }

    private int getProbableWordCount() throws InvalidParameterException {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (String subtitle : getSubtitles()){
            int wordCount = getWordCount(subtitle);
            Integer sum = map.get(wordCount);
            if(sum == null) {
                sum = 0;
            }
            map.put(wordCount, sum + 1);
        }

        int wordCountCeiling = 0;
        int wordCountMostUsed = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > wordCountCeiling){
                wordCountCeiling = entry.getValue();
                wordCountMostUsed = entry.getKey();
            }
        }

        if (wordCountCeiling * 2 > getSubtitles().size()) {
            return wordCountMostUsed;
        }
        throw new InvalidParameterException("Cannot find a stable word count in the subtitles");
    }

    private int getWordCount(String sentence){
        sentence = sentence.trim();
        if (sentence.isEmpty())
            return 0;
        return sentence.split("\\s+").length;
    }

}
