package com.giljulio.sublingo.split.task.impl;

import com.giljulio.sublingo.split.Engine;
import com.giljulio.sublingo.split.model.Video;
import com.giljulio.sublingo.split.task.Task;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by Gil on 05/01/15.
 */
public class SplitterTask implements Task {


    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(AudioExtractionTask.class.getName());

    Path path;

    public SplitterTask(Path path){
        this.path = path;
    }

    @Override
    public void execute(Engine context) {
        String id = UUID.randomUUID().toString();
        File oldFilename = new File("data/processing/" + path.getFileName());
        File newFilename = new File("data/" + id + ".mp4");
        oldFilename.renameTo(newFilename);
        Video video = new Video(id);

//        context.pushTask(new AudioExtractionTask(video));
//        context.pushTask(new AnalyseAudioTask(video));
        context.pushTask(new SplitVideoTask(video));
    }
}
