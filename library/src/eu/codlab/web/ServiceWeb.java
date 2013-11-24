package eu.codlab.web;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.mobidevland.controller.AdvertsController;
import com.mobidevland.controller.EventsController;
import com.mobidevland.controller.JobsController;
import com.mobidevland.controller.MessageCorrespondanceController;
import com.mobidevland.controller.NewsController;
import com.mobidevland.controller.UserController;
import com.mobidevland.library.R;
import com.mobidevland.objects.User;


public class ServiceWeb extends Service {
    private static ServiceWeb _this;
    private static User _user;
    private DownloaderService _downloader;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void init() {
        //was here list of every controller

        _downloader = new DownloaderService();
        _downloader.onCreate();
        _this = this;

    }

    private void destroy() {

        _this = null;
        _user = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startDownloader();

        init();
    }

    public void receiveIntent(Intent intent) {
        if (intent.getExtras() != null) {
            if ((intent.hasExtra("new_news") && intent.hasExtra("message_text"))) {
                Notification notif = getNotification("Nouvelle news", intent.getExtras().getString("message_text"));
                NotificationManager _manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                _manager.notify(100, notif);
                Intent serviceIntent = new Intent(this, DownloaderService.class);
                serviceIntent.setAction(DownloaderService.ACTION_DOWNLOAD_NEWS);
                startService(serviceIntent);
            } else if (intent.hasExtra("new_message") && intent.hasExtra("message_text")) {
                //TODO add user photo etc...
                Notification notif = getNotification("Nouveau message", intent.getExtras().getString("message_text"));
                NotificationManager _manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                _manager.notify(101, notif);
                Intent serviceIntent = new Intent(this, DownloaderService.class);
                serviceIntent.setAction(DownloaderService.ACTION_DOWNLOAD_MESSAGES);
                startService(serviceIntent);
            } else if (intent.hasExtra("new_jobs") && intent.hasExtra("message_text")) {
                Notification notif = getNotification("Nouvelle proposition de job", intent.getExtras().getString("message_text"));
                NotificationManager _manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                _manager.notify(101, notif);
                Intent serviceIntent = new Intent(this, DownloaderService.class);
                serviceIntent.setAction(DownloaderService.ACTION_DOWNLOAD_JOBS);
                startService(serviceIntent);
            } else if (intent.hasExtra("new_events") && intent.hasExtra("message_text")) {
                Notification notif = getNotification("Nouvel event", intent.getExtras().getString("message_text"));
                NotificationManager _manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                _manager.notify(101, notif);
                Intent serviceIntent = new Intent(this, DownloaderService.class);
                serviceIntent.setAction(DownloaderService.ACTION_DOWNLOAD_EVENTS);
                startService(serviceIntent);
            } else if (intent.hasExtra("new_adverts") && intent.hasExtra("message_text")) {
                Intent serviceIntent = new Intent(this, DownloaderService.class);
                serviceIntent.setAction(DownloaderService.ACTION_DOWNLOAD_ADVERTS);
                startService(serviceIntent);
            } else if (intent.hasExtra("message_text")) {
                //Notification notif = getNotification("Alerte", intent.getExtras().getString("message_text"));
                //NotificationManager _manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //_manager.notify(99, notif);
            }
            //Toast.makeText(getApplicationContext(), intent.getExtras().getString("message_text"), Toast.LENGTH_LONG).show();
        }
    }

    private void startDownloader() {
        Intent serviceIntent = new Intent(this, DownloaderService.class);
        //serviceIntent.setAction(DownloaderService.GO);
        startService(serviceIntent);
    }

    private Notification getNotification(String title, String message) {
        CharSequence text = title;//getText(R.string.title);

        Notification notification = new Notification(R.drawable.notif_tmp, text,
                System.currentTimeMillis());

        PendingIntent contentIntent = null;
                //PendingIntent.getActivity(this, 0,new Intent(this, AppActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, title, message, contentIntent);
        return notification;
    }

    private Notification getNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.service);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.notif_tmp, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = null;
                //PendingIntent.getActivity(this, 0,new Intent(this, AppActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.service),text, contentIntent);
        return notification;
    }

    public void onManageIntent(Intent intent) {
        if (intent == null) {
        } else if ("usersjson".equals(intent.getAction()) && intent.hasExtra("json")) {
            String res = intent.getStringExtra("json");
            if (res != null) {
                try {
                    JSONArray json = new JSONArray(res);
                    UserController.getInstance().addUser(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("userjson".equals(intent.getAction()) && intent.hasExtra("json")) {
            String res = intent.getStringExtra("json");
            if (res != null) {
                try {
                    JSONObject json = new JSONObject(res);
                    getUser().onJSON(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("newsjson".equals(intent.getAction()) && intent.hasExtra("json")) {
            String res = intent.getStringExtra("json");
            if (res != null) {
                try {
                    JSONArray json = new JSONArray(res);
                    NewsController.getInstance().addNews(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("advertsjson".equals(intent.getAction()) && intent.hasExtra("json")) {
            String res = intent.getStringExtra("json");
            if (res != null) {
                try {
                    JSONArray json = new JSONArray(res);
                    AdvertsController.getInstance().addAdverts(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("eventsjson".equals(intent.getAction()) && intent.hasExtra("json")) {
            String res = intent.getStringExtra("json");
            if (res != null) {
                try {
                    JSONArray json = new JSONArray(res);
                    EventsController.getInstance().addEvents(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("jobsjson".equals(intent.getAction()) && intent.hasExtra("json")) {
            String res = intent.getStringExtra("json");
            if (res != null) {
                try {
                    JSONArray json = new JSONArray(res);
                    JobsController.getInstance().addJobs(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("messagesjson".equals(intent.getAction()) && intent.hasExtra("json")) {
            String res = intent.getStringExtra("json");
            if (res != null) {
                try {
                    JSONArray json = new JSONArray(res);
                    MessageCorrespondanceController.getInstance().addMessage(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ("participejson".equals(intent.getAction()) && intent.hasExtra("json")) {
            String res = intent.getStringExtra("json");
            if (res != null) {
                try {
                    JSONArray json = new JSONArray(res);
                    //NewsController.getInstance().onJSON(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void quit() {
        destroy();
        _this = null;
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //this.startForeground(42, getNotification());
        startDownloader();
        _this = this;
        //REGISTER MAIN ACTIVITY
        /*if (AppActivity.getCurrent() != null)
            this.registerListener(AppActivity.getCurrent());*/

        onManageIntent(intent);
        //TODO manage 2nd service here
        //

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        quit();
        _downloader.onDestroy();
        super.onDestroy();

        this.stopSelf();
    }

    public static ServiceWeb getService() {
        return _this;
    }

    public User getUser() {
        return _user;
    }
}
