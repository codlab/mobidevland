package eu.codlab.web;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloaderService extends Service {
    public static final String ACTION_SET_BLOB_USER = "eu.codlab.web.android.downloader.ACTION_SET_BLOB_USER";
    public static final String ACTION_DOWNLOAD_USER = "eu.codlab.web.android.downloader.ACTION_DOWNLOAD_USER";
    public static final String ACTION_DOWNLOAD_USERS = "eu.codlab.web.android.downloader.ACTION_DOWNLOAD_USERS";
    public static final String ACTION_DOWNLOAD_NEWS = "eu.codlab.web.android.downloader.ACTION_DOWNLOAD_NEWS";
    public static final String ACTION_DOWNLOAD_EVENTS = "eu.codlab.web.android.downloader.ACTION_DOWNLOAD_EVENTS";
    public static final String ACTION_DOWNLOAD_MESSAGES = "eu.codlab.web.android.downloader.ACTION_DOWNLOAD_MESSAGES";
    public static final String ACTION_DOWNLOAD_PARTICIPE = "eu.codlab.web.android.downloader.ACTION_DOWNLOAD_PARTICIPE";
    public static final String ACTION_DOWNLOAD_JOBS = "eu.codlab.web.android.downloader.ACTION_DOWNLOAD_JOBS";
    public static final String ACTION_DOWNLOAD_ADVERTS = "eu.codlab.web.android.downloader.ACTION_DOWNLOAD_ADVERTS";


    private static DownloaderService _this;

    public static DownloaderService getInstance() {
        return _this;
    }

    public static String getUrlTokennized() {
        return DownloaderServiceObject.getUrlTokennized();
    }

    public static String get(URL url) {
        return DownloaderServiceObject.get(url);
    }

    private DownloaderServiceObject _object;

    private DownloaderServiceObject getObject() {
        if (_object == null) {
            _object = new DownloaderServiceObject();
            _object.onCreate(this);
        }
        return _object;
    }

    public void onCreate() {
        getObject().onCreate(this);
        _this = this;
    }

    public void onDestroy() {
        getObject().onDestroy();
        _object = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getAndSendUser(boolean force) throws MalformedURLException {
        getObject().getAndSendUsers(force);
    }

    public void getAndSendUsers(boolean force) throws MalformedURLException {
        getObject().getAndSendUsers(force);
    }
    public void getAndSendAdverts(boolean force) throws MalformedURLException {
        getObject().getAndSendAdverts(force);
    }
    public void getAndSendJobs(boolean force) throws MalformedURLException {
        getObject().getAndSendJobs(force);
    }
    public void getAndSendNews(boolean force) throws MalformedURLException {
        getObject().getAndSendNews(force);
    }
    public void getAndSendParticipe(boolean force) throws MalformedURLException {
        getObject().getAndSendParticipe(force);
    }
    public void getAndSendMessages(boolean force) throws MalformedURLException {
        getObject().getAndSendMessages(force);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return getObject().onStartCommand(intent, flags, startId);
    }

}
