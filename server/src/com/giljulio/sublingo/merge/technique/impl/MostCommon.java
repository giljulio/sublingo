package com.giljulio.sublingo.merge.technique.impl;

import com.giljulio.sublingo.merge.model.PredictedSubtitle;
import com.giljulio.sublingo.merge.technique.MergeTechnique;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This technique simply checks if there is a subtitle
 * that has been submitted much more than others.
 *
 * To be used only when:
 * predicted probability > 0.5
 *
 * WARNING: This only uses the most used subtitle
 */
public class MostCommon extends MergeTechnique {


    @Override
    public String getTechniqueName() {
        return "Most common";
    }

    /**
     * Due to the nature of the algorithm, the result should
     * be given less importance;
     */
    private static final double PROBABILITY_DECEMENT = 0.3;


    HashMap<String, Integer> map = new HashMap<>();

    @Override
    public PredictedSubtitle execute() {
        //Adds all the subtitles to the map
        addAllToMap();

        int subCount = getSubtitles().size();

        int maxCount = 0;
        String subtitle = "";
        for (Map.Entry<String, Integer> entry : map.entrySet()){
            if(maxCount < entry.getValue()){
                maxCount = entry.getValue();
                subtitle = entry.getKey();
            }
        }

        return new PredictedSubtitle(subtitle, (subCount / maxCount) * PROBABILITY_DECEMENT);
    }

    /**
     * Adds all the subs to the map
     */
    private void addAllToMap(){
        ArrayList<String> subtitles = getSubtitles();
        for (String subtitle : subtitles){
            Integer count = map.get(subtitle);
            if(count != null) {
                map.put(subtitle, count + 1);
            } else {
                map.put(subtitle, 1);
            }
        }
    }
}
