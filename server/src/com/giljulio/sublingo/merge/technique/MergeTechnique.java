package com.giljulio.sublingo.merge.technique;

import com.giljulio.sublingo.merge.model.PredictedSubtitle;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
* Created by Gil on 07/04/15.
*/
public abstract class MergeTechnique {

    /**
     * Subtitles
     */
    @NotNull private ArrayList<String> subtitles = new ArrayList<>();



    public abstract String getTechniqueName();


    /**
     * Process the user generated subtitles here
     * @return the predicted subtitle
     */
    @NotNull public abstract PredictedSubtitle execute();


    /**
     * Gets the current subtitles the merge plan
     * @return array of subtitles
     */
    @NotNull public ArrayList<String> getSubtitles() {
        return subtitles;
    }


}
