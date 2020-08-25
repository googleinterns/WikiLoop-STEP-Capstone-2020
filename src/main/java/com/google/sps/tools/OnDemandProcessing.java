package com.google.sps.tools;

import java.util.List;
import java.util.ArrayList;

import java.io.*;

import java.lang.ClassLoader;

import com.google.sps.data.WikiMedia;
import com.google.sps.data.EditComment;

import com.google.appengine.api.datastore.*;


import com.google.sps.data.User;
import com.google.sps.data.Users;
import com.google.sps.tests.MockData;
import com.google.sps.data.EditComment;
import com.google.sps.data.Perspective;
import com.google.sps.data.Attribute;
import com.google.sps.data.WikiMediaRequestQueue;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import java.io.FileReader;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class OnDemandProcessing {

    public static void main(String args[]) throws SchedulerException{
      JobDetail jobDetail = JobBuilder.newJob(WikiMediaRequestQueue.class).build();

      Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CroneTrigger")
      .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(20)
      .repeatForever()).build();

      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.start();
      scheduler.scheduleJob(jobDetail, trigger);

    }
}