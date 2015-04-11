package com.giljulio.sublingo.split.task.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.giljulio.sublingo.split.Engine;
import com.giljulio.sublingo.split.model.Clip;
import com.giljulio.sublingo.split.model.Video;
import com.giljulio.sublingo.split.net.HttpRequest;
import com.giljulio.sublingo.split.task.Task;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Gil on 01/04/15.
 */
public class UploadToS3Task implements Task {

    private static final String BUCKET_NAME = "sublingo";

    Video video;

    public UploadToS3Task(Video video) {
        this.video = video;
    }

    @Override
    public void execute(Engine context) {
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.", e);
        }

        AmazonS3 s3 = new AmazonS3Client(credentials);
        Region euWest = Region.getRegion(Regions.EU_WEST_1);
        s3.setRegion(euWest);
        try {


            HttpRequest request = HttpRequest.post("https://api.parse.com/1/classes/clips/");
            request.contentType(HttpRequest.CONTENT_TYPE_JSON);
            request.header("X-Parse-Application-Id", "eBc3RsK73vT1NPDgIwjwTNbzq5A47dnDjzbBMfhO");
            request.header("X-Parse-REST-API-Key", "jUbjITpOPuPFqduvNdAOX8PXW8zypCfrQKm4oaZ8");
            JSONObject body = new JSONObject();
//            body.put("url", "https://s3-eu-west-1.amazonaws.com/sublingo/" + video.getId() + ".mp4");
            request.send(body.toString());

            if(request.code()/100 == 2) {
                System.out.println("Added video clip to API successfully");
                Clip clip = new Gson().fromJson(request.body(), Clip.class);
                PutObjectRequest objectRequest = new PutObjectRequest(BUCKET_NAME, clip.getObjectId() + ".mp4", new File("data/output/" + video.getId() + ".mp4"));
                objectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
                s3.putObject(objectRequest);
                System.out.println("Uploaded finished: https://s3-eu-west-1.amazonaws.com/sublingo/" + clip.getObjectId() + ".mp4");
            } else {
                System.out.println(request.body());
            }
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
