package com.giljulio.sublingo.task.impl;

import com.giljulio.sublingo.Engine;
import com.giljulio.sublingo.model.Video;
import com.giljulio.sublingo.task.Task;
import com.sun.istack.internal.Nullable;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gil on 12/12/14.
 */
public class SplitVideoTask implements Task {


    //data  ffmpeg -i video.mp4 -ss 00:00:03 -t 00:00:08 -async 1 cut.mp4


    Video video;

    public SplitVideoTask(Video video){
        this.video = video;
    }


    @Override
    public void execute(Engine context) {
        Runtime rt = Runtime.getRuntime();
        try {
            int length = getLength();
            for (int i = 0; i < 3; i++) {
                Thread.sleep(200);
                int outerbound = ((i * 10) + 10) * 1000;
                if (outerbound > length)
                    outerbound = length;
                System.out.println("ffmpeg -i data/" + video.getId() + ".mp4 " +
                        "-ss " + formatSeconds(i * 10 * 1000) +
                        " -to " + formatSeconds(outerbound) + " -async 1 data/output/" + video.getId() +"_" + i +".mp4");

                Process process = rt.exec("ffmpeg -i data/" + video.getId() + ".mp4 " +
                        "-ss " + formatSeconds(i * 10 * 1000) +
                        " -to " + formatSeconds(outerbound) + " -async 1 data/output/" + video.getId() +"_" + i +".mp4");
                process.waitFor();
                context.pushTask(new UploadToS3Task(new Video(video.getId() + "_" + i)));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void printCurrentPath(){
        Runtime rt = Runtime.getRuntime();
        try {
            Process process = rt.exec("pwd");
            InputStream in = process.getInputStream();
            System.out.println(IOUtils.toString(in, "UTF-8"));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private Integer getLength(){
        Runtime rt = Runtime.getRuntime();
        try {
            Process process = rt.exec("ffprobe -loglevel error -show_format -show_streams data/" + video.getId() + ".mp4 -print_format json");
            InputStream in = process.getInputStream();
            JSONObject json = new JSONObject(IOUtils.toString(in, "UTF-8"));
            in.close();
            JSONObject format = json.getJSONObject("format");
            String duration = format.getString("duration");
            return Double.valueOf(duration).intValue() * 1000;
        } catch (IOException|JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    private String formatSeconds(int millis){
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    /*public static void main(String...args) {
        int max = 10000;
        int value1 = 30000;
        int value2 = 50;

        HashMap<Integer, Integer> map = new HashMap<>();
        int index = 0;

        while (value1 * index < max) {
            map.put(value1 * index, value1 * index);
            index++;
        }
        index = 0;
        while (value2 * index < max) {
            map.put(value2 * index, value2 * index);
            index++;
        }

        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet())
        {
            sum += entry.getValue();
        }
        System.out.println("The sum: " + sum);
    }*/
}
