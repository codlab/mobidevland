package com.mobidevland.controller;

import com.mobidevland.listeners.EventListener;
import com.mobidevland.listeners.JobListener;
import com.mobidevland.objects.Events;
import com.mobidevland.objects.Jobs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class JobsController {
    private static JobsController mInstance;

    private ArrayList<Jobs> mJobs;
    public ArrayList<Jobs> getJobs(){
        if(mJobs==null)mJobs=new ArrayList<Jobs>();
        return mJobs;
    }
    public Jobs getJob(long id){
        for(Jobs u : mJobs){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    private ArrayList<JobListener> mListeners;
    private ArrayList<JobListener> getJobsListener(){
        if(mListeners==null)mListeners=new ArrayList<JobListener>();
        return mListeners;
    }
    public void addEventsListener(JobListener listener){
        if(!getJobsListener().contains(listener))getJobsListener().add(listener);
    }
    public void removeEventsListener(JobListener listener){
        getJobsListener().remove(listener);
    }

    public static JobsController getInstance(){
        if(mInstance == null)mInstance = new JobsController();
        return mInstance;
    }

    private JobsController(){

    }

    public void addJobs(JSONArray object){
        if(object != null){
            for(int i=0;i<object.length();i++){
                JSONObject obj = object.optJSONObject(i);
                if(obj != null)addJobs(obj);
            }
        }
    }
    public void addJobs(JSONObject object){

    }

    public void addUser(Jobs job){
        if(job != null && getJob(job.getId()) == null){
            mJobs.add(job);

            for(JobListener l : getJobsListener()){
                l.onJob(job);
            }
        }
    }


}
