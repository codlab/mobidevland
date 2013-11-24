package com.mobidevland.objects;

import android.graphics.Bitmap;


/**
 * Created by kevinleperf on 23/11/2013.
 */
public class News extends DbmsSimple{

    /**
     * Title of the news
     */
    private String mTitle;

    /**
     * Creation date which can evolve
     */
    private long mDate;

    /**
     * Content text
     */
    private String mContent;

    /**
     * Main Photo url
     */
    private String mVisual;

    /**
     * Contents media url
     */
    private String[] mUrls;
    /**
     * Created time in timestamp unix
     */
    private long mCreated;

    public News(long id, String title, String content, String visual, String [] urls, long date, long created){
        setId(id);
        setTitle(title);
        setContent(content);
        setVisual(visual);
        setUrls(urls);
        setDate(date);
        setCreated(created);
    }

    public void setTitle(String set){
        mTitle = set;
    }

    public void setContent(String set){
        mContent = set;
    }

    public void setVisual(String set){
        mVisual = set;
    }

    public void setUrls(String [] urls){
        mUrls = urls;//ouch ...
    }

    public void setDate(long date){
        mDate = date;
    }

    public void setCreated(long set){
        mCreated = set;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getContent(){
        return mContent;
    }

    public String getVisual(){
        return mVisual;
    }

    public String [] getUrls(){
        return mUrls;
    }

    public long getOriginalCreationDate(){
        return mCreated;
    }

    public long getCreationDate(){
        return mDate;
    }




    public Bitmap getBitmap(){
        return getBitmap(mVisual);
    }
}
