package com.mobidevland.controller;

import com.mobidevland.listeners.AdvertListener;
import com.mobidevland.objects.Adverts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class AdvertsController {
    private static AdvertsController mInstance;

    private ArrayList<Adverts> mAdverts;
    public ArrayList<Adverts> getAdverts(){
        if(mAdverts==null)mAdverts=new ArrayList<Adverts>();
        return mAdverts;
    }
    public Adverts getAdverts(long id){
        for(Adverts u : mAdverts){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    private ArrayList<AdvertListener> mListeners;
    private ArrayList<AdvertListener> getAdvertsListener(){
        if(mListeners==null)mListeners=new ArrayList<AdvertListener>();
        return mListeners;
    }
    public void addAdvertsListener(AdvertListener listener){
        if(!getAdvertsListener().contains(listener))getAdvertsListener().add(listener);
    }
    public void removeAdvertsListener(AdvertListener listener){
        getAdvertsListener().remove(listener);
    }

    public static AdvertsController getInstance(){
        if(mInstance == null)mInstance = new AdvertsController();
        return mInstance;
    }

    private AdvertsController(){
    }

    public void addAdverts(JSONArray object){
        if(object != null){
            for(int i=0;i<object.length();i++){
                JSONObject obj = object.optJSONObject(i);
                if(obj != null)addAdverts(obj);
            }
        }
    }

    public void addAdverts(JSONObject object){
        long id=object.optLong("id");
        String media=object.optString("media","");
        long created=object.optLong("created",0);
        long date=object.optLong("date",0);
        String description=object.optString("description","");
        String target=object.optString("target","");
        Adverts advert = new Adverts(id, media, description, target, date, created);
        addAdverts(advert);
    }

    public void addAdverts(Adverts advert){
        if(advert != null && getAdverts(advert.getId()) == null){
            mAdverts.add(advert);

            for(AdvertListener l : getAdvertsListener()){
                l.onAdvert(advert);
            }
        }
    }


}
