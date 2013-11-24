package com.mobidevland.objects;

import android.graphics.Bitmap;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class Adverts extends DbmsSimple{

    /**
     *
     */
    private String mMedia;

    /**
     * Target description
     */
    private String mDescription;

    /**
     * Creation date which can evolve
     */
    private long mDate;

    /**
     * Target url
     */
    private String mTarget;

    /**
     * Contents media url
     */
    private String[] mUrls;
    /**
     * Created time in timestamp unix
     */
    private long mCreated;

    public Adverts(long id, String media, String description, String target, long date, long created){
        setId(id);
        setMedia(media);
        setDescription(description);
        setTarget(target);
        setDate(date);
        setCreated(created);
    }

    public void setMedia(String media){
        mMedia = media;
    }

    public void setDescription(String set){
        mDescription = set;
    }

    public void setTarget(String set){
        mTarget = set;
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

    public String getMedia(){
        return mMedia;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getTarget(){
        return mTarget;
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
        return getBitmap(mMedia);
    }
}
