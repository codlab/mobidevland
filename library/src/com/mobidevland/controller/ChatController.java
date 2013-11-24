package com.mobidevland.controller;

import com.mobidevland.listeners.ChatListener;
import com.mobidevland.objects.Chat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class ChatController {
    private static ChatController mInstance;

    private ArrayList<Chat> mMessage;
    public ArrayList<Chat> getMessage(){
        if(mMessage==null)mMessage=new ArrayList<Chat>();
        return mMessage;
    }

    public Chat getMessage(long id){
        for(Chat u : getMessage()){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    private ArrayList<ChatListener> mListeners;
    private ArrayList<ChatListener> getMessageListener(){
        if(mListeners==null)mListeners=new ArrayList<ChatListener>();
        return mListeners;
    }
    public void addMessageListener(ChatListener listener){
        if(!getMessageListener().contains(listener))getMessageListener().add(listener);
    }
    public void removeMessageListener(ChatListener listener){
        getMessageListener().remove(listener);
    }


    public static ChatController getInstance(){
        if(mInstance == null)mInstance = new ChatController();
        return mInstance;
    }

    private ChatController(){
    }

    public void addChat(JSONArray object){
        if(object != null){
            for(int i=0;i<object.length();i++){
                JSONObject obj = object.optJSONObject(i);
                if(obj != null)addChat(obj);
            }
        }
    }

    public void addChat(JSONObject object){
        long id=object.optLong("id");
        long emitter=object.optLong("emitter", 0);
        //todo download user which are not known
        long created=object.optLong("created",0);
        long date=object.optLong("date", 0);
        String message=object.optString("message","");
        String media=object.optString("media","");
        Chat m = new Chat(id, emitter, message, media, date, created);
        addChat(m);
    }

    public void addChat(Chat message){
        if(message != null && getMessage(message.getId()) == null){
            mMessage.add(message);

            for(ChatListener l : getMessageListener()){
                l.onMessage(message, UserController.getInstance().getUser(message.getEmitter()));
            }
        }
    }


}
