package com.giljulio.sublingo.merge.model;

import com.sun.istack.internal.Nullable;

/**
 * Created by Gil on 07/04/15.
 */
public class PredictedSubtitle {

    /**
     * The subtitle was generated
     */
    @Nullable String subtitle;

    /**
     * Probably of the subtitle being correct
     * -1 if the subtitle prediction doesnt have a probability
     */
    double probability = -1;

    public PredictedSubtitle() {
    }

    public PredictedSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public PredictedSubtitle(String subtitle, double probability) {
        this.subtitle = subtitle;
        this.probability = probability;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Gets the probablility the subtitle being correct
     * @return the probability min 0 and max 1 (1 being sure it is correct)
     */
    public double getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }
}
