package com.mobidevland.controller;

import com.mobidevland.listeners.AdvertListener;
import com.mobidevland.listeners.EventListener;
import com.mobidevland.objects.Adverts;
import com.mobidevland.objects.Events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class EventsController {
    private static EventsController mInstance;

    private ArrayList<Events> mEvents;
    public ArrayList<Events> getEvents(){
        if(mEvents==null)mEvents=new ArrayList<Events>();
        return mEvents;
    }
    public Events getEvents(long id){
        for(Events u : mEvents){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    private ArrayList<EventListener> mListeners;
    private ArrayList<EventListener> getEventsListener(){
        if(mListeners==null)mListeners=new ArrayList<EventListener>();
        return mListeners;
    }
    public void addEventsListener(EventListener listener){
        if(!getEventsListener().contains(listener))getEventsListener().add(listener);
    }
    public void removeEventsListener(EventListener listener){
        getEventsListener().remove(listener);
    }

    public static EventsController getInstance(){
        if(mInstance == null)mInstance = new EventsController();
        return mInstance;
    }

    private EventsController(){

    }

    public void addEvents(JSONArray object){
        if(object != null){
            for(int i=0;i<object.length();i++){
                JSONObject obj = object.optJSONObject(i);
                if(obj != null)addEvents(obj);
            }
        }
    }

    public void addEvents(JSONObject object){
        long id=object.optLong("id");
        String title=object.optString("titre","");
        long created=object.optLong("created",0);
        long date=object.optLong("date",0);
        String description=object.optString("description","");
        String target=object.optString("url","");
        String media=object.optString("photo","");
        Events advert = new Events(id, title, media, description, target, date, created);
        addEvents(advert);
    }

    public void addEvents(Events advert){
        if(advert != null && getEvents(advert.getId()) == null){
            mEvents.add(advert);

            for(EventListener l : getEventsListener()){
                l.onEvent(advert);
            }
        }
    }


}
