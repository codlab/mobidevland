package com.mobidevland.controller;

import com.mobidevland.listeners.JobListener;
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
    public void addJobsListener(JobListener listener){
        if(!getJobsListener().contains(listener))getJobsListener().add(listener);
    }
    public void removeJobsListener(JobListener listener){
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
        long id=object.optLong("id");
        String photo=object.optString("photo","");
        long created=object.optLong("created",0);
        long date=object.optLong("date",0);
        String description=object.optString("description","");
        String url=object.optString("url","");
        String titre=object.optString("titre","");
        String lieu=object.optString("lieu","");
        String company=object.optString("company","");
        String profil=object.optString("profil","");
        Jobs advert = new Jobs(id, titre, url, lieu, company, description, profil, created, date, photo);
        addJobs(advert);
    }

    public void addJobs(Jobs job){
        if(job != null && getJob(job.getId()) == null){
            mJobs.add(job);

            for(JobListener l : getJobsListener()){
                l.onJob(job);
            }
        }
    }


}
