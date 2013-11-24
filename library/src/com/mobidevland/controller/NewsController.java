package com.mobidevland.controller;

import com.mobidevland.listeners.NewsListener;
import com.mobidevland.objects.News;
import com.mobidevland.objects.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class NewsController {
    private static NewsController mInstance;

    private ArrayList<News> mNews;
    public ArrayList<News> getNews(){
        if(mNews==null)mNews=new ArrayList<News>();
        return mNews;
    }
    public News getNews(long id){
        for(News u : mNews){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    private ArrayList<NewsListener> mListeners;
    private ArrayList<NewsListener> getNewsListener(){
        if(mListeners==null)mListeners=new ArrayList<NewsListener>();
        return mListeners;
    }
    public void addNewsListener(NewsListener listener){
        if(!getNewsListener().contains(listener))getNewsListener().add(listener);
    }
    public void removeNewsListener(NewsListener listener){
        getNewsListener().remove(listener);
    }

    public static NewsController getInstance(){
        if(mInstance == null)mInstance = new NewsController();
        return mInstance;
    }

    private NewsController(){

    }

    public void addNews(JSONArray object){
        if(object != null){
            for(int i=0;i<object.length();i++){
                JSONObject obj = object.optJSONObject(i);
                if(obj != null)addNews(obj);
            }
        }
    }

    public void addNews(JSONObject object){
        long id=object.optLong("id");
        String title=object.optString("titre","");
        long created=object.optLong("created",0);
        long date=object.optLong("date",0);
        String [] urls=object.optString("urls","").split(",");
        String visual=object.optString("visuel","");
        String content=object.optString("contenu","");
        News n = new News(id, title, content, visual, urls, date, created);
        addNews(n);
    }

    public void addNews(News news){
        if(news != null && getNews(news.getId()) == null){
            mNews.add(news);

            for(NewsListener l : getNewsListener()){
                l.onNews(news);
            }
        }
    }


}
