package eu.codlab.web;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kevinleperf on 04/07/13.
 */
public class DownloaderServiceObject {

    DownloaderService _parent;

    private DownloaderService getParent(){
        return _parent;
    }

    private final static String _url = "178.170.116.112";
    private static Handler _handler;
    private static boolean _downloading;

    private static String _token;

    private void log(String display){
        //Log.d("DownloaderService", display);
    }
    public static String getUrl(){
        return "http://"+_url+":3000/";
    }
    public static String getToken(){
        return _token;
    }
    public static String getUrlNotTokennized(){
        return getUrl()+"api/";
    }
    public static String getUrlTokennized(){
        return getUrl()+"api/"+_token+"/";
    }
    private final void getUpdatedToken(){
        _token = ServiceWeb.getService().getSharedPreferences(getSharedName(), 0).getString("token", "null");
    }
    public final static String getSharedName(){
        return "APPSHARED";
    }
    public DownloaderServiceObject(){
    }

    public void onCreate(DownloaderService parent){
        _parent = parent;
        log("onCreate");
        _downloading = false;
    }

    //Download User Data
    private class AsyncDownloadUser extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadUser(){
            _send_after = true;

        }

        public AsyncDownloadUser(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading user...");
                getAndSendUser(!_send_after);
                _downloading = false;
            }
            if(_send_after){
                postDownloadNews();
            }
            return null;
        }
    }

    public void getAndSendUser(boolean force){
        final String users;
        try {
            getAndSendUsersAfter(getUser(new URL(getUrlTokennized()+"user/update?gcm_device="+ GCMRegistrar.getRegistrationId(ServiceWeb.getService().getApplicationContext())), force));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private void getAndSendUserAfter(String res){
        if(res == null)return;
        Intent i = new Intent(ServiceWeb.getService(), ServiceWeb.class);
        i.setAction("userjson");
        i.putExtra("json", res);
        if(_handler != null)
            ServiceWeb.getService().onManageIntent(i);
    }


    //Download User Data
    private class AsyncDownloadNews extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadNews(){
            _send_after = true;

        }

        public AsyncDownloadNews(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading news...");
                try {
                    getAndSendNews(!_send_after);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                _downloading = false;
            }
            if(_send_after){
                postDownloadAdverts();
            }
            return null;
        }
    }

    public void getAndSendNews(boolean force){
        try {
            final String res = getNews(new URL(getUrlNotTokennized()+"news/list"), force);
            if(res != null){
                //TODO download images
                Intent i = new Intent(ServiceWeb.getService(), ServiceWeb.class);
                i.setAction("newsjson");
                i.putExtra("json", res);
                if(_handler != null)
                    ServiceWeb.getService().onManageIntent(i);
                //DownloaderService.this.startService(i);
                //download news photo
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try{
                            JSONArray json = new JSONArray(res);

                            if(json != null){
                                JSONObject object = null;
                                for(int id=0;id<json.length();id++){
                                    object = json.optJSONObject(id);
                                    if(object != null){
                                        try{
                                            if(object.has("visuel") && object.getString("visuel") != null && object.getString("visuel").length() > 0){
                                                String image_url = object.getString("visuel");
                                                if(image_url.startsWith("/"))
                                                    image_url="http://178.170.116.112:3000"+image_url;
                                                getImage(image_url, -1, "");
                                            }
                                        }catch(Exception e){e.printStackTrace();}
                                    }
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                t.start();

            }else{
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    //Download User Data
    private class AsyncDownloadAdverts extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadAdverts(){
            _send_after = true;

        }

        public AsyncDownloadAdverts(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading user...");
                getAndSendAdverts(!_send_after);
                _downloading = false;
            }
            if(_send_after){
                postDownloadEvents();
            }
            return null;
        }
    }

    public void getAndSendAdverts(boolean force){
        try {
            final String res = getAdverts(new URL(getUrlNotTokennized()+"adverts/list"), force);
            if(res != null){
                //TODO download images
                Intent i = new Intent(ServiceWeb.getService(), ServiceWeb.class);
                i.setAction("advertsjson");
                i.putExtra("json", res);
                if(_handler != null)
                    ServiceWeb.getService().onManageIntent(i);

                //download adverts photo
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try{
                            JSONArray json = new JSONArray(res);

                            if(json != null){
                                JSONObject object = null;
                                for(int id=0;id<json.length();id++){
                                    object = json.optJSONObject(id);
                                    if(object != null){
                                        try{
                                            if(object.has("media") && object.getString("media") != null && object.getString("media").length() > 0){
                                                String image_url = object.getString("media");
                                                if(image_url.startsWith("/"))
                                                    image_url="http://178.170.116.112:3000"+image_url;
                                                getImage(image_url, -1, "");
                                            }
                                        }catch(Exception e){e.printStackTrace();}
                                    }
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                t.start();
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Download User Data
    private class AsyncDownloadEvents extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadEvents(){
            _send_after = true;

        }

        public AsyncDownloadEvents(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading events...");
                getAndSendEvents(!_send_after);
                _downloading = false;
            }
            if(_send_after){
                postDownloadJobs();
            }
            return null;
        }
    }

    public void getAndSendEvents(boolean force){
        try {
            final String res = getEvents(new URL(getUrlNotTokennized()+"events/list"), force);
            if(res != null){
                //TODO download events
                Intent i = new Intent(ServiceWeb.getService(), ServiceWeb.class);
                i.setAction("eventsjson");
                i.putExtra("json", res);
                if(_handler != null)
                    ServiceWeb.getService().onManageIntent(i);
                //download events photo
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try{
                            JSONArray json = new JSONArray(res);

                            if(json != null){
                                JSONObject object = null;
                                for(int id=0;id<json.length();id++){
                                    object = json.optJSONObject(id);
                                    if(object != null){
                                        try{
                                            if(object.has("photo") && object.getString("photo") != null && object.getString("photo").length() > 0){
                                                String image_url = object.getString("photo");
                                                if(image_url.startsWith("/"))
                                                    image_url="http://178.170.116.112:3000"+image_url;
                                                getImage(image_url, -1, "");
                                            }
                                        }catch(Exception e){e.printStackTrace();}
                                    }
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                t.start();
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Download User Data
    private class AsyncDownloadJobs extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadJobs(){
            _send_after = true;

        }

        public AsyncDownloadJobs(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading user...");
                getAndSendJobs(!_send_after);

                _downloading = false;
            }
            if(_send_after){
                postDownloadUsers();
            }
            return null;
        }
    }

    public void getAndSendJobs(boolean force){
        try {
            final String res = getJobs(new URL(getUrlNotTokennized()+"jobs/list"), force);
            if(res != null){
                //TODO download images
                Intent i = new Intent(ServiceWeb.getService(), ServiceWeb.class);
                i.setAction("jobsjson");
                i.putExtra("json", res);
                if(_handler != null)
                    ServiceWeb.getService().onManageIntent(i);

                //download job photo
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try{
                            JSONArray json = new JSONArray(res);

                            if(json != null){
                                JSONObject object = null;
                                for(int id=0;id<json.length();id++){
                                    object = json.optJSONObject(id);
                                    if(object != null){
                                        try{
                                            if(object.has("photo") && object.getString("photo") != null && object.getString("photo").length() > 0){
                                                String image_url = object.getString("photo");
                                                if(image_url.startsWith("/"))
                                                    image_url="http://178.170.116.112:3000"+image_url;
                                                getImage(image_url, -1, "");
                                            }
                                        }catch(Exception e){e.printStackTrace();}
                                    }
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                t.start();
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Download User Data
    private class AsyncDownloadUsers extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadUsers(){
            _send_after = true;

        }

        public AsyncDownloadUsers(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading user...");
                try {
                    final String res = getUsers(new URL(getUrlTokennized()+"users/list"), !_send_after);
                        getAndSendUsersAfter(res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                _downloading = false;
            }
            if(_send_after){
                postDownloadMessages();
            }
            return null;
        }
    }

    public void getAndSendUsers(boolean force){
        final String users;
        try {
            getAndSendUsersAfter(getUsers(new URL(getUrlTokennized() + "users/list"), force));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private void getAndSendUsersAfter(final String users){
        if(users == null)return;
        Intent intent = new Intent(ServiceWeb.getService(), ServiceWeb.class);
        intent.setAction("usersjson");
        intent.putExtra("json", users);
        if(_handler != null)
            ServiceWeb.getService().onManageIntent(intent);
        //download the users icon
        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    JSONArray json = new JSONArray(users);

                    if(json != null){
                        JSONObject object = null;
                        for(int id=0;id<json.length();id++){
                            object = json.optJSONObject(id);
                            if(object != null){
                                try{
                                    if(object.has("icon") && object.getString("icon") != null && object.getString("icon").length() > 0){
                                        String image_url = object.getString("icon");
                                        if(image_url.startsWith("/"))
                                            image_url="http://178.170.116.112:3000"+image_url;
                                        getImage(image_url, -1, "");
                                    }
                                }catch(Exception e){e.printStackTrace();}
                            }
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        };
        t.start();
    }


    /**
     * TODO download only correspondances
     */
    //Download User Data
    private class AsyncDownloadMessages extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadMessages(){
            _send_after = true;

        }

        public AsyncDownloadMessages(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading user...");
                getAndSendMessages(!_send_after);
                _downloading = false;
            }
            if(_send_after){
                postDownloadParticipe();
            }
            return null;
        }
    }

    public void getAndSendMessages(boolean force){
        try {
            final String res = getMessages(new URL(getUrlTokennized()+"user/list_messages"), force);
            if(res != null){
                //TODO download images
                Intent i = new Intent(ServiceWeb.getService(), ServiceWeb.class);
                i.setAction("jobsjson");
                i.putExtra("json", res);
                if(_handler != null)
                    ServiceWeb.getService().onManageIntent(i);
                //now download the different media in the messages
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try{
                            JSONArray json = new JSONArray(res);

                            if(json != null){
                                JSONObject object = null;
                                for(int id=0;id<json.length();id++){
                                    object = json.optJSONObject(id);
                                    if(object != null){
                                        try{
                                            if(object.has("media") && object.getString("media") != null && object.getString("media").length() > 0){
                                                String image_url = object.getString("media");
                                                if(image_url.startsWith("/"))
                                                    image_url="http://178.170.116.112:3000"+image_url;
                                                getImage(image_url, -1, "");
                                            }
                                        }catch(Exception e){e.printStackTrace();}
                                    }
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                };
                t.start();
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //Download User Data
    private class AsyncDownloadParticipe extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadParticipe(){
            _send_after = true;

        }

        public AsyncDownloadParticipe(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading user...");
                getAndSendParticipe(!_send_after);
                _downloading = false;
            }
            if(_send_after){
                postDownloadUser();
            }
            return null;
        }
    }

    public void getAndSendParticipe(boolean force){
        try {
            final String res = getParticipe(new URL(getUrlTokennized()+"user/participation"), force);
            if(res != null){
                Intent i = new Intent(ServiceWeb.getService(), ServiceWeb.class);
                i.setAction("participejson");
                i.putExtra("json", res);
                if(_handler != null)
                    ServiceWeb.getService().onManageIntent(i);
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private class AsyncUploadBitmap extends AsyncTask<URL, Object, Object>{
        private String _url;
        private String _blob_uri;
        private Context _context;

        public AsyncUploadBitmap(Context context, String url, String blob_uri){
            _url = url;
            _blob_uri = blob_uri;
            _context = context;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(true || !_downloading){
                _downloading = true;
                try {
                    final String res = DownloaderServiceObject.this.get(_url, "blob", _blob_uri);
                    if(res != null){
                        try{
                            Log.d("res",res);
                            JSONObject object = new JSONObject(res);
                            if(object.has("icon") && object.getString("icon") != null){
                                Cache c = new Cache(_context, object.getString("icon"), -1);

                                Bitmap bm = BitmapFactory.decodeFile(_blob_uri);
                                bm = Bitmap.createScaledBitmap(bm, 96,96,true);
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.PNG, 75, bos);

                                c.writeBitmap(bm);
                            }
                        }catch(Exception e){e.printStackTrace();}

                        Intent i = new Intent(ServiceWeb.getService(), ServiceWeb.class);
                        i.setAction("userjson");
                        i.putExtra("json", res);
                        ServiceWeb.getService().onManageIntent(i);

                    }else{
                        //TODO manage network state !
                    }

                    forceDownloadMessages();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                _downloading = false;
            }
            return null;
        }

    }


private long _last_download;
    //Download User Data
    private class AsyncDownloadChat extends AsyncTask<URL, Object, Object>{
        private boolean _send_after;
        public AsyncDownloadChat(){
            _send_after = true;

        }

        public AsyncDownloadChat(boolean send_after){
            _send_after = send_after;
        }

        @Override
        protected Object doInBackground(URL... arg0) {
            if(!_downloading){
                _downloading = true;
                log("Downloading user...");
                getAndSendChat(true);
                _downloading = false;
            }
            if(_send_after){
                postDownloadChat();
            }
            return null;
        }
    }

    public void getAndSendChat(boolean force){
        try {
            final String res = getParticipe(new URL(getUrlTokennized()+"user/list_chat"), force);
            if(res != null){
                try{
                    JSONArray array = new JSONArray(res);
                    if(array != null && array.length() > 0){
                        JSONObject object = null;
                        for(int i=0;i<array.length();i++){
                            object = array.optJSONObject(i);
                            if(object != null && object.has("created")){
                                long created = object.optLong("created",0);
                                if(created > _last_download)_last_download=created;
                            }
                        }
                    }
                    Intent intent = new Intent(ServiceWeb.getService(), ServiceWeb.class);
                    intent.setAction("chatjson");
                    intent.putExtra("json", res);
                    if(_handler != null)
                        ServiceWeb.getService().onManageIntent(intent);
                }catch(Exception e){}
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void forceDownloadChat(){
        AsyncDownloadChat data = new AsyncDownloadChat(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            data.execute();
    }
    public void forceDownloadUser(){
        AsyncDownloadUser data = new AsyncDownloadUser(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            data.execute();
    }
    public void forceDownloadAdverts(){
        AsyncDownloadAdverts data = new AsyncDownloadAdverts(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            data.execute();
    }
    public void forceDownloadNews(){
        AsyncDownloadNews _async_force = new AsyncDownloadNews(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            _async_force.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            _async_force.execute();
    }

    public void forceDownloadEvents(){
        AsyncDownloadEvents data = new AsyncDownloadEvents(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            data.execute();
    }

    public void forceDownloadJobs(){
        AsyncDownloadJobs data = new AsyncDownloadJobs(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            data.execute();
    }

    public void forceDownloadMessages(){
        AsyncDownloadMessages data = new AsyncDownloadMessages(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            data.execute();
    }
    public void forceDownloadParticipe(){
        AsyncDownloadParticipe data = new AsyncDownloadParticipe(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            data.execute();
    }

    public void forceDownloadUsers(){
        AsyncDownloadUsers data = new AsyncDownloadUsers(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            data.execute();
    }

    private final Runnable _download_user=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadUser data = new AsyncDownloadUser();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };

    private final Runnable _download_advert=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadAdverts data = new AsyncDownloadAdverts();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };

    private final Runnable _download_news=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadNews data = new AsyncDownloadNews();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };

    private final Runnable _download_events=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadEvents data = new AsyncDownloadEvents();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };

    private final Runnable _download_job=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadJobs data = new AsyncDownloadJobs();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };

    private final Runnable _download_message=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadMessages data = new AsyncDownloadMessages();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };

    private final Runnable _download_participe=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadParticipe data = new AsyncDownloadParticipe();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };


    private final Runnable _download_chat=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadChat data = new AsyncDownloadChat();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };


    private final Runnable _download_users=new Runnable(){
        @Override
        public void run(){
            AsyncDownloadUsers data = new AsyncDownloadUsers();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }
    };


    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("DownloaderService", "start downloader");
        //update the token from the shared preferences
        getUpdatedToken();

        //START DOWNLOADING FROM HERE


        synchronized(this){
            if(_handler == null){
                _user_cache_loaded = false;
                _adverts_cache_loaded = false;
                _news_cache_loaded = false;
                _events_cache_loaded = false;
                _job_cache_loaded = false;
                _message_cache_loaded = false;
                _participe_cache_loaded = false;
                _handler = new Handler();
                _handler.post(this._download_user);
            }
        }

        if(intent == null){
        }else if(DownloaderService.ACTION_DOWNLOAD_CHAT.equals(intent.getAction())){
            this.forceDownloadChat();
        }else if(DownloaderService.ACTION_DOWNLOAD_USER.equals(intent.getAction())){
            this.forceDownloadUser();
        }else if(DownloaderService.ACTION_DOWNLOAD_USERS.equals(intent.getAction())){
            this.forceDownloadUsers();
        }else if(DownloaderService.ACTION_DOWNLOAD_ADVERTS.equals(intent.getAction())){
            this.forceDownloadAdverts();
        }else if(DownloaderService.ACTION_DOWNLOAD_NEWS.equals(intent.getAction())){
            this.forceDownloadNews();
        }else if(DownloaderService.ACTION_DOWNLOAD_MESSAGES.equals(intent.getAction())){
            this.forceDownloadMessages();
        }else if(DownloaderService.ACTION_DOWNLOAD_EVENTS.equals(intent.getAction())){
            this.forceDownloadEvents();
        }else if(DownloaderService.ACTION_DOWNLOAD_PARTICIPE.equals(intent.getAction())){
            this.forceDownloadParticipe();
        }else if(DownloaderService.ACTION_DOWNLOAD_JOBS.equals(intent.getAction())){
            this.forceDownloadJobs();
        }else if(DownloaderService.ACTION_SET_BLOB_USER.equals(intent.getAction())){
            String blob = intent.getStringExtra("blob");
            String url = getUrlTokennized()+"user/update";
            AsyncUploadBitmap data = new AsyncUploadBitmap(ServiceWeb.getService().getApplicationContext(), url, blob);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                data.execute();
        }else{

        }

        //super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }

    //TODO change timers AFTER HERE
    Handler _chat;
    private void postDownloadChat(){
        synchronized (this){
            if(_chat == null)_chat = new Handler();
            _chat.postDelayed(_download_chat, 15000);
        }
    }
    private void postDownloadUser(){
        synchronized (this){
            if(_handler != null)
                _handler.postDelayed(_download_user, _user_cache_loaded ? 1000 : 10);
        }
    }
    private void postDownloadAdverts(){
        //reset a new pack of download every 4mins
        synchronized (this){
            if(_handler != null)
                _handler.postDelayed(_download_advert, _adverts_cache_loaded ? 1000 : 10);
        }
    }
    private void postDownloadNews(){
        //reset a new pack of download every 4mins
        synchronized (this){
            if(_handler != null)
                _handler.postDelayed(_download_news, _news_cache_loaded ? 1000 : 10);
        }
    }
    private void postDownloadEvents(){
        //reset a new pack of download every 4mins
        synchronized (this){
            if(_handler != null)
                _handler.postDelayed(_download_events, _events_cache_loaded ? 1000 : 10);
        }
    }

    private void postDownloadJobs(){
        //reset a new pack of download every 4mins
        synchronized (this){
            if(_handler != null)
                _handler.postDelayed(_download_job, _job_cache_loaded ? 1000 : 10);
        }
    }
    private void postDownloadUsers(){
        synchronized(this){
            if(_handler != null)
                _handler.postDelayed(_download_users, _users_cache_loaded ? 1000 : 10);
        }
    }
    private void postDownloadMessages(){
        synchronized (this){
            if(_handler != null)
                _handler.postDelayed(_download_message, _message_cache_loaded ? 1000 : 10);
        }
    }
    private void postDownloadParticipe(){
        synchronized (this){
            if(_handler != null)
                _handler.postDelayed(_download_participe, _participe_cache_loaded ? 120000 : 10);
        }
    }


    private boolean _user_cache_loaded;
    public String getUser(URL url, boolean force){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url.getPath(),120000);
        if(!force && (!cache.shouldRecreate() || !_user_cache_loaded) && cache.exists()){
            _user_cache_loaded = true;
            return cache.read();
        }else{
            _user_cache_loaded = true;
            String result = get(url);
            if(result != null){
                cache.write(result);
            }else{
                return cache.read();
            }
            return result;
        }
    }

    boolean _adverts_cache_loaded;
    public String getAdverts(URL url, boolean force){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url.getPath(),30000);
        if(!force && (!cache.shouldRecreate() || !_adverts_cache_loaded) && cache.exists()){
            _adverts_cache_loaded = true;
            return cache.read();
        }else{
            _adverts_cache_loaded = true;
            String result = get(url);
            if(result != null){
                cache.write(result);
            }else{
                return cache.read();
            }
            return result;
        }
    }

    boolean _news_cache_loaded;
    public String getNews(URL url, boolean force){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url.getPath(),30000);
        if(!force && (!cache.shouldRecreate() || !_news_cache_loaded) && cache.exists()){
            _news_cache_loaded = true;
            return cache.read();
        }else{
            _news_cache_loaded = true;
            String result = get(url);
            if(result != null){
                cache.write(result);
            }else{
                return cache.read();
            }
            return result;
        }
    }

    boolean _events_cache_loaded;
    public String getEvents(URL url, boolean force){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url.getPath(),30000);
        if(!force && (!cache.shouldRecreate() || !_events_cache_loaded) && cache.exists()){
            _events_cache_loaded = true;
            return cache.read();
        }else{
            _events_cache_loaded = true;
            String result = get(url);
            if(result != null){
                cache.write(result);
            }else{
                return cache.read();
            }
            return result;
        }
    }

    boolean _job_cache_loaded;
    public String getJobs(URL url, boolean force){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url.getPath(),30000);
        if(!force && (!cache.shouldRecreate() || !_job_cache_loaded) && cache.exists()){
            _job_cache_loaded = true;
            return cache.read();
        }else{
            _job_cache_loaded = true;
            String result = get(url);
            if(result != null){
                cache.write(result);
            }else{
                return cache.read();
            }
            return result;
        }
    }

    private boolean _users_cache_loaded;
    public String getUsers(URL url, boolean force){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url.getPath(),240000);
        if(!force && (!cache.shouldRecreate() || !_users_cache_loaded) && cache.exists()){
            _users_cache_loaded = true;
            return cache.read();
        }else{
            _users_cache_loaded = true;
            String result = get(url);
            if(result != null){
                cache.write(result);
            }else{
                return cache.read();
            }
            return result;
        }
    }

    boolean _message_cache_loaded;
    public String getMessages(URL url, boolean force){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url.getPath(),30000);
        if(!force && (!cache.shouldRecreate() || !_message_cache_loaded) && cache.exists()){
            _message_cache_loaded = true;
            return cache.read();
        }else{
            _message_cache_loaded = true;
            String result = get(url);
            if(result != null){
                cache.write(result);
            }else{
                return cache.read();
            }
            return result;
        }
    }

    boolean _participe_cache_loaded;
    public String getParticipe(URL url, boolean force){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url.getPath(),30000);
        if(!force && (!cache.shouldRecreate() || !_participe_cache_loaded) && cache.exists()){
            _participe_cache_loaded = true;
            return cache.read();
        }else{
            _participe_cache_loaded = true;
            String result = get(url);
            if(result != null){
                cache.write(result);
            }else{
                return cache.read();
            }
            return result;
        }
    }

    public void getImage(String url, int refresh_time, String path){
        Cache cache = new Cache(ServiceWeb.getService().getApplicationContext(),url,refresh_time);
        if(!cache.exists() || cache.shouldRecreate()){
            try{
                if(url.startsWith("/")){
                    url="http://178.170.116.112:3000"+url;
                }
                URL _url = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                Bitmap bmImg = BitmapFactory.decodeStream(is);
                cache.writeBitmap(bmImg);

                //return bmImg;
            }catch(Exception e){e.printStackTrace();}
            //return null;
        }
    }

    public static String get(URL url) {

        // this does no network IO
        URLConnection conn;
        try {
            conn = url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        InputStream in;
        int http_status;
        String res = null;
        try {

            // this opens a connection, then sends GET & headers
            conn.connect();

            // can't get status before getInputStream.  If you try, you'll
            //  get a nasty exception.

            // better check it first
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            // now you can try to consume the data
            in = conn.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                res = total.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
        return res;

    }

    public static String get(String url, String attachment_name, String path) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String res ="";
        try{
            Bitmap bm = BitmapFactory.decodeFile(path);
            bm = Bitmap.createScaledBitmap(bm, 96,96,true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 75, bos);
            byte[] data = bos.toByteArray();

            MultipartEntity entity = new MultipartEntity();
            File myFile = new File(path);
            FileBody fileBody = new FileBody(myFile);
            ByteArrayBody blob = new ByteArrayBody(data, attachment_name);
            entity.addPart("blob", blob);
            httppost.setEntity(entity);
            //httppost.getParams().setParameter("project", id);
            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s= s.append(sResponse);
            }
            res = s.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public void onDestroy(){
        synchronized (this){
            Handler tmp = _handler;
            _handler = null;
            if(tmp != null){
                tmp.removeCallbacks(this._download_user);
                tmp.removeCallbacks(this._download_advert);
                tmp.removeCallbacks(this._download_events);
                tmp.removeCallbacks(this._download_job);
                tmp.removeCallbacks(this._download_message);
                tmp.removeCallbacks(this._download_news);
                tmp.removeCallbacks(this._download_participe);
                tmp.removeCallbacks(this._download_users);
            }
        }
    }
}
