package com.mobidevland.controller;

import com.mobidevland.listeners.MessageListener;
import com.mobidevland.objects.Message;
import com.mobidevland.objects.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class MessagesController {
    private User mUser1;
    private User mUser2;

    private ArrayList<Message> mMessage;
    public ArrayList<Message> getMessage(){
        if(mMessage==null)mMessage=new ArrayList<Message>();
        return mMessage;
    }

    public boolean isEqual(User user1, User user2){
        return (user1 != null && user2 != null &&
                ((user1.getId() == mUser1.getId() && user2.getId() == mUser2.getId()) ||
                        (user1.getId() == mUser2.getId() && user2.getId() == mUser1.getId())));
    }

    public Message getMessage(long id){
        for(Message u : mMessage){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    private ArrayList<MessageListener> mListeners;
    private ArrayList<MessageListener> getMessageListener(){
        if(mListeners==null)mListeners=new ArrayList<MessageListener>();
        return mListeners;
    }
    public void addMessageListener(MessageListener listener){
        if(!getMessageListener().contains(listener))getMessageListener().add(listener);
    }
    public void removeMessageListener(MessageListener listener){
        getMessageListener().remove(listener);
    }

    public MessagesController(User user1, User user2){
        mUser1 = user1;
        mUser2 = user2;
    }

    public User getUser1(){
        return mUser1;
    }

    public User getUser2(){
        return mUser2;
    }

    public void addMessage(JSONArray object){
        if(object != null){
            for(int i=0;i<object.length();i++){
                JSONObject obj = object.optJSONObject(i);
                if(obj != null)addMessage(obj);
            }
        }
    }

    public void addMessage(JSONObject object){
        long id=object.optLong("id");
        long emitter=object.optLong("emitter",0);
        long receiver=object.optLong("receiver",0);
        //todo download user which are not known
        long created=object.optLong("created",0);
        long date=object.optLong("date",0);
        String message=object.optString("message","");
        String media=object.optString("media","");
        Message m = new Message(id, emitter, receiver, message, media, date, created);
        addMessage(m);
    }

    public void addMessage(Message message){
        if(message != null && getMessage(message.getId()) == null){
            mMessage.add(message);

            for(MessageListener l : getMessageListener()){
                l.onMessage(message, (message.getEmitter() == mUser1.getId() ? mUser1 : mUser2),
                        (message.getEmitter() == mUser1.getId() ? mUser2 : mUser1));
            }
        }
    }


}
