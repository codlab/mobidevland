package com.mobidevland.objects;

import android.graphics.Bitmap;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class Chat extends DbmsSimple{

    /**
     *
     */
    private long mEmitter;


    /**
     * Creation date which can evolve
     */
    private long mDate;

    /**
     * Target url
     */
    private String mMessage;

    /**
     * Contents media url
     */
    private String mMedia;
    /**
     * Created time in timestamp unix
     */
    private long mCreated;

    public Chat(long id, long emitter, String message, String media, long date, long created){
        setId(id);
        setMedia(media);
        setMessage(message);
        setEmitter(emitter);
        setDate(date);
        setCreated(created);
    }

    public void setMedia(String media){
        mMedia = media;
    }

    public void setMessage(String set){
        mMessage = set;
    }

    public void setEmitter(long set){
        mEmitter = set;
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

    public String getMessage(){
        return mMessage;
    }
    public long getEmitter(){
        return mEmitter;
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
