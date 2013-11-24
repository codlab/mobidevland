package com.mobidevland.objects;

import android.graphics.Bitmap;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class Participe extends DbmsSimple{

    /**
     *
     */
    private long mUser;

    /**
     * Target description
     */
    private long mEvent;

    public Participe(long id, long user, long event){
        setId(id);
        setUser(user);
        setEvent(event);
    }

    public void setUser(long media){
        mUser = media;
    }

    public void setEvent(long set){
        mEvent = set;
    }

    public long getUser(){
        return mUser;
    }

    public long getEvent(){
        return mEvent;
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}
