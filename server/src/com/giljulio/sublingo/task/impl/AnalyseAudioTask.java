package com.giljulio.sublingo.task.impl;

import com.giljulio.sublingo.Engine;
import com.giljulio.sublingo.model.Video;
import com.giljulio.sublingo.task.Task;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by Gil on 05/01/15.
 */
public class AnalyseAudioTask implements Task {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(AudioExtractionTask.class.getName());

    Video video;

    public AnalyseAudioTask(Video video){
        this.video = video;
    }

    @Override
    public void execute(Engine context) {
        logger.info("Starting Audio analyses task...");
        File file = new File(String.format("data/%s.wav", video.getId()));

        //WaveData waveData = new WaveData();
        //int[] amplitude = waveData.extractAmplitudeFromFile(file);

        /*try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            int size = audioInputStream.available();
            byte[] b = new byte[size];
            logger.info("Byte array length: " + b.length);
            double ave[] = new double[1000];
            int count = 0;
            PrintWriter writer = new PrintWriter("data/" + video.getId() + ".txt", "UTF-8");
            if (size == audioInputStream.read(b)) {
                for (int i = 0; i < b.length; i++){
                    ave[count++] = Math.abs((double) (b[i+1] << 8 | b[i] & 0xFF) / 32767.0);
                    if(count == ave.length){
                        writer.println("" + (DoubleStream.of(ave).sum() / (double)ave.length));
                        count = 0;
                    }
                }
            }
            writer.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }*/

        logger.info("Finishing Audio analyses task");

    }
}
