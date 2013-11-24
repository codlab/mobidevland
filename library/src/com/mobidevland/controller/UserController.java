package com.mobidevland.controller;

import com.mobidevland.listeners.UserListener;
import com.mobidevland.objects.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class UserController {
    private static UserController mInstance;

    private ArrayList<User> mUsers;
    public ArrayList<User> getUsers(){
        if(mUsers==null)mUsers=new ArrayList<User>();
        return mUsers;
    }
    public User getUser(long id){
        for(User u : mUsers){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    private ArrayList<UserListener> mListeners;
    private ArrayList<UserListener> getUserListener(){
        if(mListeners==null)mListeners=new ArrayList<UserListener>();
        return mListeners;
    }
    public void addUserListener(UserListener listener){
        if(!getUserListener().contains(listener))getUserListener().add(listener);
    }
    public void removeUserListener(UserListener listener){
        getUserListener().remove(listener);
    }

    public static UserController getInstance(){
        if(mInstance == null)mInstance = new UserController();
        return mInstance;
    }

    private UserController(){

    }

    public void addUser(JSONObject object){
        long id=object.optLong("id");
        String login=object.optString("login","");
        String mail=object.optString("email","");
        String token=object.optString("persistence_token","");
        boolean admin=object.optBoolean("admin",false);
        long created=object.optLong("created_at",0);
        long updated=object.optLong("updated_at",0);
        String company=object.optString("company","");
        String name=object.optString("name","");
        String last=object.optString("lastname","");
        String surn=object.optString("surname","");
        String address=object.optString("address","");
        String pc=object.optString("postalcode","");
        String city=object.optString("city","");
        String state=object.optString("state","");
        String language=object.optString("language","");
        String phone=object.optString("phone","");
        String mobile=object.optString("mobile","");
        String website=object.optString("website","");
        String gps=object.optString("gps","");
        String expertise=object.optString("expertise","");
        String icon=object.optString("icon","");
        User u = new User(id, login, mail, token, admin, created, updated, company, name, last,
                surn, address, pc, city, state, language, phone, mobile, website, icon,expertise,gps);
        addUser(u);
    }

    public void addUser(JSONArray object){
        if(object != null){
            for(int i=0;i<object.length();i++){
                JSONObject obj = object.optJSONObject(i);
                if(obj != null)addUser(obj);
            }
        }
    }

    public void addUser(User user){
        if(user != null && getUser(user.getId()) == null){
            mUsers.add(user);

            for(UserListener l : getUserListener()){
                l.onUser(user);
            }
        }
    }


}
