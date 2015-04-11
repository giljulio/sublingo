package com.giljulio.sublingo.split.task.impl;

//import com.bitsinharmony.recognito.MatchResult;
//import com.bitsinharmony.recognito.Recognito;
//import com.bitsinharmony.recognito.VoicePrint;
import com.giljulio.sublingo.split.Engine;
import com.giljulio.sublingo.split.task.Task;

/**
 * Created by Gil on 01/04/15.
 */
public class VADTask implements Task {

    @Override
    public void execute(Engine context) {
                /*
        try {
            Recognito<String> recognito = new Recognito<>(16000.0f);

            VoicePrint print = recognito.createVoicePrint("Elvis", new File("OldInterview.wav"));

// handle persistence the way you want, e.g.:
// myUser.setVocalPrint(print);
// userDao.saveOrUpdate(myUser);

// Now check if the King is back
            List<MatchResult<String>> matches = recognito.identify(new File("SomeFatGuy.wav"));
            MatchResult<String> match = matches.get(0);

            if (match.getKey().equals("Elvis")) {
                System.out.println("Elvis is back !!! " + match.getLikelihoodRatio() + "% positive about it...");
            }
        } catch (Exception e){
            e.printStackTrace();
    }                 */
        }
}
