package com.mobidevland.controller;

import com.mobidevland.listeners.AdvertListener;
import com.mobidevland.listeners.MessageCorrespondanceListener;
import com.mobidevland.objects.Adverts;
import com.mobidevland.objects.Message;
import com.mobidevland.objects.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public class MessageCorrespondanceController {
    private static MessageCorrespondanceController mInstance;

    private ArrayList<MessagesController> mMessageController;
    public ArrayList<MessagesController> getMessageCorrespondance(){
        if(mMessageController==null)mMessageController=new ArrayList<MessagesController>();
        return mMessageController;
    }
    public MessagesController getMessagesController(User user1, User user2){
        if(user1 == null || user2 == null)return null;

        if(user1.getId() < user2.getId()){
            User tmp = user2;
            user2 = user1;
            user1 = tmp;
        }

        for(MessagesController u : mMessageController){
            if(u.isEqual(user1, user2)){
                return u;
            }
        }
        return null;
    }

    private ArrayList<MessageCorrespondanceListener> mListeners;
    private ArrayList<MessageCorrespondanceListener> getMessageCorrespondanceListener(){
        if(mListeners==null)mListeners=new ArrayList<MessageCorrespondanceListener>();
        return mListeners;
    }
    public void addMessageCorrespondanceListener(MessageCorrespondanceListener listener){
        if(!getMessageCorrespondanceListener().contains(listener))getMessageCorrespondanceListener().add(listener);
    }
    public void removeMessageCorrespondanceListener(MessageCorrespondanceListener listener){
        getMessageCorrespondanceListener().remove(listener);
    }

    public static MessageCorrespondanceController getInstance(){
        if(mInstance == null)mInstance = new MessageCorrespondanceController();
        return mInstance;
    }

    private MessageCorrespondanceController(){

    }

    public void addCorrespondance(JSONObject object){
        //TODO from JSON
    }

    public void addCorrespondance(MessagesController controller){
        if(controller != null && getMessagesController(controller.getUser1(), controller.getUser2()) == null){
            mMessageController.add(controller);

            for(MessageCorrespondanceListener l : getMessageCorrespondanceListener()){
                l.onCorrespondance(controller, controller.getUser1().getId(), controller.getUser2().getId(),
                        controller.getUser1(), controller.getUser2());
            }
        }
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
        //for efficiecy issue
        /*
        to add much more performance : we need to switch from a full array view to an array of objects in which :
        array of objects : user1,
                           user2,
                           array : messages of receiver, emitter, message, media

        this way we can then find the controller of messages where (user1,user2) belongs and then
        add to it every single messages which come the way

        and not looking for the users at each one...
         */

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
        if(message != null){
            User user1 = UserController.getInstance().getUser(message.getEmitter());
            User user2 = UserController.getInstance().getUser(message.getReceiver());
            if(user1 != null && user2 != null){
                MessagesController messagesController = getMessagesController(user1, user2);
                if(messagesController == null){
                    messagesController = new MessagesController(user1, user2);
                    addCorrespondance(messagesController);
                }
                messagesController.addMessage(message);
            }
        }
    }


}
