package com.mobidevland.objects;

import android.graphics.Bitmap;

import eu.codlab.web.Cache;
import eu.codlab.web.ServiceWeb;

/**
 * Created by kevinleperf on 23/11/2013.
 */
abstract class DbmsSimple {
    private long mId;


    final protected void setId(long id){
        mId = id;
    }

    final public long getId(){
        return mId;
    }


    private Bitmap _icon;
    final protected Bitmap getBitmap(String url){
        if(url != null && !"".equals(url) && url == null){
            if(url.startsWith("/"))
                url="http://178.170.116.112:3000"+url;
            if(!url.startsWith("http://")){
                if(url.startsWith("/"))
                    url="http://178.170.116.112:3000"+url;
                else
                    url="http://178.170.116.112:3000/"+url;
            }
            try{
                Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(), url,-1);
                _icon = cache.readBitmap();
            }catch(Exception e){e.printStackTrace();}
        }
        return _icon;
    }

    abstract public Bitmap getBitmap();
}
