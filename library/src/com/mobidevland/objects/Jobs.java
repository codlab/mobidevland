package com.mobidevland.objects;

import android.graphics.Bitmap;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class Jobs extends DbmsSimple {
    private String mTitle;
    private String mUrl;
    private String mLocation;
    private String mDescription;
    private String mCompany;
    private String mProfil;
    private String mPhoto;

    private long mDate;
    private long mCreated;

    public Jobs(long id, String titre, String url, String location, String company, String description, String profil, long creation, long date, String photo){
        setId(id);
        setTitle(titre);
        setCompany(company);
        setUrl(url);
        setLocation(location);
        setDescription(description);
        setProfil(profil);
        setCreationDate(date);
        setOriginalCreationDate(creation);
        setPhoto(photo);
    }

    public void setPhoto(String photo){
        mPhoto = photo;
    }
    public void setTitle(String set){
        mTitle = set;
    }
    public void setUrl(String set){
        mUrl = set;
    }
    public void setLocation(String set){
        mLocation = set;
    }
    public void setDescription(String set){
        mDescription = set;
    }
    public void setProfil(String set){
        mProfil = set;
    }
    public void setOriginalCreationDate(long set){
        mCreated = set;
    }
    public void setCreationDate(long set){
        mDate = set;
    }
    public void setCompany(String company){
        mCompany = company;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getUrl(){
        return mUrl;
    }

    public String getLocation(){
        return mLocation;
    }

    public String getProfil(){
        return mProfil;
    }

    public long getOriginalCreationDate(){
        return mCreated;
    }

    public long getCreationDate(){
        return mDate;
    }

    public String getPhoto(){
        return mPhoto;
    }

    public String getCompany(){
        return mCompany;
    }

    public String getDescription(){
        return mDescription;
    }


    public Bitmap getBitmap(){
        return getBitmap(mPhoto);
    }
}
