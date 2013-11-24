package com.mobidevland.objects;

import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class User extends DbmsSimple{
    private String mLogin;
    private String mMail;
    private String mToken;//null if not us :p
    private boolean mAdmin;
    private long mCreatedAt;
    private long mUpdatedAt;
    private String mCompany;
    private String mName;
    private String mLastName;
    private String mSurname;
    private String mAddress;
    private String mPC;
    private String mCity;
    private String mState;
    private String mLanguage;
    private String mPhone;
    private String mMobile;
    private String mWebsite;
    private String mIcon;
    private String mGps;
    private String mExpertise;

    public User(long id, String login, String mail, String token, boolean admin, long created, long updated,
                String company, String name, String last, String surn, String address, String pc,
                String city, String state, String language, String phone, String mobile, String website, String icon,
                String expertise, String gps){
        setId(id);
        setLogin(login);
        setMail(mail);
        setToken(token);
        setAdmin(admin);
        setCreatedAt(created);
        setUpdatedAt(updated);
        setCompany(company);
        setName(name);
        setLastName(last);
        setSurname(surn);
        setAddress(address);
        setPostalCode(pc);
        setCity(city);
        setState(state);
        setLanguage(language);
        setPhone(phone);
        setMobile(mobile);
        setWebsite(website);
        setIcon(icon);
        setGps(gps);
        setExpertise(expertise);
    }

    public void onJSON(JSONObject object){
        //TODO onjson management
    }

    public boolean equals(Object object){
        if(object != null && object instanceof User){
            return ((User)object).getId() == getId();
        }
        return false;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getLogin(){
        return mLogin;
    }

    public void setMail(String m){
        mMail = m;
    }

    public String getMail(){
        return mMail;
    }

    public void setToken(String s){
        mToken = s;
    }

    public String getToken(){
        return mToken;
    }

    public void setAdmin(boolean admin){
        mAdmin = admin;
    }
    public boolean getAdmin(){
        return mAdmin;
    }

    public void setCreatedAt(long c){
        mCreatedAt = c;
    }
    public long getCreated(){
        return mCreatedAt;
    }

    public void setUpdatedAt(long u){
        mUpdatedAt = u;
    }
    public long getUpdated(){
        return mUpdatedAt;
    }

    public void setCompany(String company){
        mCompany = company;
    }
    public String getCompany(){
        return mCompany;
    }

    public void setName(String name){
        mName = name;
    }
    public String getName(){
        return mName;
    }

    public void setLastName(String l){
        mLastName = l;
    }
    public String getLastName(){
        return mLastName;
    }

    public void setSurname(String s){
        mSurname = s;
    }
    public String getSurname(){
        return mSurname;
    }

    public void setAddress(String a){
        mAddress = a;
    }
    public String getAddress(){
        return mAddress;
    }

    public void setPostalCode(String c){
        mPC = c;
    }
    public String getPostalCode(){
        return mPC;
    }

    public void setCity(String c){
        mCity = c;
    }

    public String getCity(){
        return mCity;
    }

    public void setState(String s){
        mState = s;
    }
    public String getState(){
        return mState;
    }

    public void setLanguage(String l){
        mLanguage = l;
    }
    public String getLanguage(){
        return mLanguage;
    }

    public void setPhone(String p){
        mPhone = p;
    }
    public String getPhone(){
        return mPhone;
    }

    public void setMobile(String m){
        mMobile = m;
    }
    public String getMobile(){
        return mMobile;
    }

    public void setWebsite(String w){
        mWebsite = w;
    }
    public String getWebsite(){
        return mWebsite;
    }

    public void setExpertise(String expertise){
        mExpertise = expertise;
    }
    public String getExpertise(){
        return mExpertise;
    }

    public void setGps(String gps){
        mGps = gps;
    }
    public String getGps(){
        return mGps;
    }
    public void setIcon(String icon){
        mIcon = icon;
    }
    public String getIcon(){
        return mIcon;
    }

    public Bitmap getBitmap(){
        return getBitmap(mIcon);
    }
}
