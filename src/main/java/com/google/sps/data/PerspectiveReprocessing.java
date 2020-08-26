package com.google.sps.data;

import java.io.IOException;

import com.google.sps.data.WikiMedia;

import java.util.ArrayList;
import java.util.List;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.appengine.api.datastore.*;

import okhttp3.*;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PerspectiveReprocessing implements Job{ 
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
      System.out.println("We are running!!!");
      System.out.println("The time now is " + new Date());
      getWikiMediaResponse();
    }

    private void getWikiMediaResponse() {
      String buildUrl = "https://wikiloop-step-capstone-2020.uc.r.appspot.com/reprocess-data";
      try {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String postUrl = buildUrl;
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(10, TimeUnit.MINUTES) // write timeout
            .readTimeout(10, TimeUnit.MINUTES) // read timeout
            .build();
        Request request = new Request.Builder()
            .url(postUrl)
            .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
      }
      catch(IOException e) {
        System.out.println(e);
        e.printStackTrace();
      }
    }
}
