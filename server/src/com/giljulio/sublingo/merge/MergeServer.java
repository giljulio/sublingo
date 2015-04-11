package com.giljulio.sublingo.merge;

import com.giljulio.sublingo.merge.model.PredictedSubtitle;
import com.giljulio.sublingo.merge.technique.MergeTechnique;
import com.giljulio.sublingo.merge.technique.impl.EditDistanceSentence;
import com.giljulio.sublingo.merge.technique.impl.EditDistanceWord;
import com.giljulio.sublingo.merge.technique.impl.MostCommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by Gil on 20/11/14.
 */
public class MergeServer {

    private static Logger log = Logger.getLogger(MergeServer.class.getSimpleName());

    public static void main(String...args) {

        args = new String[]{
                /*"Capiatlism can be improvd",
                "Capitalism cn be improved",
                "Capitalism imprved",
                "Capitalism can be emproved",
                "Capitalism emproved",
                "Captalism can be improved",
                "capitalsim can be improved",
                "Capiatlism can be improved",
                "improved can be Capitalism ",
                "I dont know any english",*/
                "gil",
                "dani",
                "dani",
                "dani",
                "jil",
                "jil",
                "gil",
                "dunia"

        };

        ArrayList<MergeTechnique> techniques = new ArrayList<>();
        techniques.add(new EditDistanceWord());
        techniques.add(new EditDistanceSentence());
        techniques.add(new MostCommon());

        for (MergeTechnique technique : techniques){
            technique.getSubtitles().addAll(Arrays.asList(args));
            PredictedSubtitle t = technique.execute();
            System.out.println(technique.getTechniqueName() + ": \n" + t.getSubtitle() + "\n\n");
        }
    }

}
