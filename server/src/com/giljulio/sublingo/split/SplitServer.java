package com.giljulio.sublingo.split;

import com.giljulio.sublingo.split.task.impl.SplitterTask;

import java.nio.file.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Gil on 03/01/15.
 */
public class SplitServer {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(SplitServer.class.getName());

    private static Engine engine;

    public static void main(String[] args){
        engine = new Engine();
        engine.start();
        registerDirectoryListerner();
    }


    private static void registerDirectoryListerner(){
        //define a folder root
        Path path = Paths.get("data/processing");

        try {
            WatchService ws = path.getFileSystem().newWatchService();
            path.register(ws, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey watch = null;
            while (true) {
                try {
                    watch = ws.take();
                } catch (InterruptedException ex) {
                    logger.severe("Interrupted");
                }
                List<WatchEvent<?>> events = watch.pollEvents();
                watch.reset();
                for (WatchEvent<?> event : events) {
                    WatchEvent.Kind<Path> kind = (WatchEvent.Kind<Path>) event.kind();
                    Path context = (Path) event.context();
                    if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                        logger.info("Detected: " + context.getFileName());
                        engine.pushTask(new SplitterTask(context.getFileName()));
                    }
                }
            }
        } catch (Exception e) {
            logger.severe("Server error: " + e.toString());
        }

    }
}

