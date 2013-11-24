package eu.codlab.web;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by kevinleperf on 24/11/2013.
 */
public class SessionController {
    private static SessionController mThis;

    private boolean mIsDownloading;
    private Context mContext;


    private SessionController(Context context){
        mIsDownloading = false;
        mContext = context;


    }

    public static SessionController getInstance(Context context){
        if(mThis == null)mThis = new SessionController(context);
        return mThis;
    }

    /**
     * Start the download service in not connected mode
     */
    public void startNotConnected(){
        mContext.startService(new Intent(mContext, DownloaderService.class));
    }

    /**
     * Erase the token and personal infos
     */
    public void disconnect(){
        mContext.getSharedPreferences(DownloaderServiceObject.getSharedName(), 0).edit().putString("user", null).putString("password", null).putString("persistence_token", null).commit();
        mContext.startService(new Intent(mContext, ServiceWeb.class));
    }

    /**
     * Initialize a connection
     * @param listener
     * @param login
     * @param password
     * @return true if the login process just start
     */
    public boolean connect(SessionListener listener, String login, String password){
        if(!mIsDownloading){

            LoginTask t = new LoginTask(listener, login, password);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                t.execute();
            return true;
        }
        return false;
    }

    /**
     * Initialize a register
     * @param listener
     * @param login
     * @param password
     * @param mail
     * @param name
     * @param firstname
     * @param lastname
     * @return true if the login process just start
     */
    public boolean register(SessionListener listener, String login, String password, String mail, String name, String firstname, String lastname){
        if(!mIsDownloading){
            RegisterTask t = new RegisterTask(listener, login, password, mail, name, firstname, lastname);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                t.execute();
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean isDownloading(){
        return mIsDownloading;
    }


    private final class LoginTask extends AsyncTask<String, String , String> {
        private final String _user;
        private final String _password;
        private final SessionListener _listener;
        public LoginTask(SessionListener listener, String login, String password){
            _listener = listener;

            if(login == null)
                login="null";

            if(password == null)
                password="null";
            try {
                login = URLEncoder.encode(login, "UTF-8");
                password = URLEncoder.encode(password, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                login="null";
                password="null";
                e.printStackTrace();
            }
            _user=login;
            _password=password;
        }
        @Override
        protected String doInBackground(String... arg0) {
            try {
                final String res = DownloaderServiceObject.get(new URL(DownloaderServiceObject.getUrl() + "api/login?noredirect=true&login=" + _user + "&password=" + _password));
                if(res != null){
                    try{
                        JSONObject json = new JSONObject(res);

                        if(json != null && json.has("persistence_token")){
                            mContext.getSharedPreferences(DownloaderServiceObject.getSharedName(), 0).edit().putString("user", _user).putString("password", _password).putString("persistence_token", json.getString("persistence_token")).commit();
                            mContext.startService(new Intent(mContext, ServiceWeb.class));



                            Intent sub_intent = new Intent(mContext, DownloaderService.class);
                            sub_intent.setAction(DownloaderService.ACTION_DOWNLOAD_USER);
                            mContext.startService(sub_intent);
                            int tmp=0;
                            do{
                                try{
                                    Thread.sleep(1000);
                                    tmp++;
                                }catch(Exception e){}
                            }while((ServiceWeb.getService() == null ||
                                    ServiceWeb.getService().getUser() == null) && tmp<20);
                            if(tmp>=20){
                                _listener.connectionTimedOut();
                            }else{
                                _listener.onChanged(true);
                            }

                        }else{
                            _listener.onChanged(false);
                        }
                    }catch(Exception e){
                        _listener.onChanged(false);
                    }
                }else{
                    _listener.onChanged(false);
                }
            } catch (Exception e) {
                _listener.onChanged(false);
            }
            return null;
        }
    }
    private final class RegisterTask extends AsyncTask<String, String , String> {
        private final String _user;
        private final String _password;
        private final String _name;
        private final String _mail;
        private final String _lastname;
        private final String _firstname;
        SessionListener _listener;

        public RegisterTask(SessionListener listener, String login, String password, String mail, String name, String firstname, String lastname){
            _listener = listener;
            if(login == null)login="null";
            if(password == null)password="null";
            if(mail == null)mail="null";
            if(firstname == null)firstname="null";
            if(lastname == null)lastname="null";
            try {
                login = URLEncoder.encode(login, "UTF-8");
                password = URLEncoder.encode(password, "UTF-8");
                mail = URLEncoder.encode(mail, "UTF-8");
                firstname = URLEncoder.encode(firstname, "UTF-8");
                lastname = URLEncoder.encode(lastname, "UTF-8");
                name = URLEncoder.encode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                login="null";
                password="null";
                mail="null";
                firstname="null";
                lastname="null";
                name="null";
            }
            _user=login;
            _password=password;
            _mail=mail;
            _firstname=firstname;
            _lastname=lastname;
            _name=name;
        }
        @Override
        protected String doInBackground(String... arg0) {
            try {
                final String res = DownloaderServiceObject.get(new URL(DownloaderServiceObject.getUrl() + "api/users/create?noredirect=true&login=" + _user + "&email="+_mail+"&password=" + _password+"&name="+_name+"&lastname="+_lastname+"&firstname="+_firstname));
                if(res != null){
                    try{
                        JSONObject json = new JSONObject(res);

                        if(json != null && json.has("persistence_token")){
                            mContext.getSharedPreferences(DownloaderServiceObject.getSharedName(), 0).edit().putString("user", _user).putString("password", _password).putString("persistence_token", json.getString("persistence_token")).commit();
                            mContext.startService(new Intent(mContext, ServiceWeb.class));
                            Intent sub_intent = new Intent(mContext, DownloaderService.class);
                            sub_intent.setAction(DownloaderService.ACTION_DOWNLOAD_USER);
                            mContext.startService(sub_intent);
                            int tmp=0;
                            do{
                                try{
                                    Thread.sleep(1000);
                                    tmp++;
                                }catch(Exception e){}
                            }while((ServiceWeb.getService() == null ||
                                    ServiceWeb.getService().getUser() == null) && tmp<20);
                            if(tmp>=20){
                                _listener.connectionTimedOut();
                            }else{
                                _listener.onChanged(true);
                            }
                        }else{
                            _listener.onChanged(false);
                        }
                    }catch(Exception e){
                        _listener.onChanged(false);
                    }
                }else{
                    _listener.onChanged(false);
                }
            } catch (Exception e) {
                _listener.onChanged(false);

            }
            return null;
        }
    }

    private void quitWaitingError(){

    }



}
