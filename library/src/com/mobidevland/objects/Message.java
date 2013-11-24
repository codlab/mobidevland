package com.mobidevland.objects;

import android.graphics.Bitmap;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class Message extends DbmsSimple{

    /**
     *
     */
    private long mEmitter;

    /**
     * Target description
     */
    private long mReceiver;

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

    public Message(long id, long emitter, long receiver, String message, String media, long date, long created){
        setId(id);
        setMedia(media);
        setMessage(message);
        setEmitter(emitter);
        setReceiver(receiver);
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

    public void setReceiver(long urls){
        mReceiver = urls;//ouch ...
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

    public long getReceiver(){
        return mReceiver;
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
