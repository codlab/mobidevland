package com.mobidevland.objects;

import android.graphics.Bitmap;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class Events extends DbmsSimple{
    /**
     *
     */
    private String mTitle;

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
     * Content media url
     */
    private String mPhoto;
    /**
     * Created time in timestamp unix
     */
    private long mCreated;

    public Events(long id, String title, String media, String description, String target, long date, long created){
        setId(id);
        setTitle(title);
        setPhoto(media);
        setDescription(description);
        setTarget(target);
        setDate(date);
        setCreated(created);
    }

    public void setTitle(String set){
        mTitle = set;
    }

    public void setPhoto(String media){
        mPhoto = media;
    }

    public void setDescription(String set){
        mDescription = set;
    }

    public void setTarget(String set){
        mTarget = set;
    }


    public void setDate(long date){
        mDate = date;
    }

    public void setCreated(long set){
        mCreated = set;
    }

    public String getPhoto(){
        return mPhoto;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getTarget(){
        return mTarget;
    }

    public long getOriginalCreationDate(){
        return mCreated;
    }

    public long getCreationDate(){
        return mDate;
    }


    public Bitmap getBitmap(){
        return getBitmap(mPhoto);
    }

}
