package com.giljulio.sublingo.task.impl;

import com.giljulio.sublingo.Engine;
import com.giljulio.sublingo.model.Video;
import com.giljulio.sublingo.task.Task;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Created by Gil on 03/01/15.
 */
public class AudioExtractionTask implements Task {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(AudioExtractionTask.class.getName());

    Video video;

    public AudioExtractionTask(Video video){
        this.video = video;
    }

    @Override
    public void execute(Engine context) {
        logger.info("Starting Audio extraction task...");
        Runtime rt = Runtime.getRuntime();
        try {
            String command =
                    String.format("ffmpeg -i data/%s.mp4 -ab 160k -ac 2 -ar 44100 -vn data/%s.wav", video.getId(), video.getId());
            logger.info(command);
            Process process = rt.exec(command);
            InputStream in = process.getInputStream();
            System.out.print(IOUtils.toString(in, "UTF-8"));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Finishing Audio extraction task...");
    }
}
