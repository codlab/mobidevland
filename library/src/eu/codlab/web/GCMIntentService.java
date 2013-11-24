package eu.codlab.web;

import com.google.android.gcm.GCMBaseIntentService;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by kevin on 02/06/13.
 */
public class GCMIntentService extends GCMBaseIntentService {
    @Override
    protected void onMessage(Context context, final Intent intent) {
        AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                if(ServiceWeb.getService() != null)
                    ServiceWeb.getService().receiveIntent(intent);

                return 0;
            }
            @Override
            protected void onPostExecute(Integer result) {

            }
        };
        task.execute(null,null,null);
    }

    @Override
    protected void onError(Context context, String s) {

    }

    @Override
    protected void onRegistered(final Context context, String s) {
        AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                Intent intent = new Intent(context, DownloaderService.class);
                intent.setAction(DownloaderService.ACTION_DOWNLOAD_USER);
                startService(intent);

                return 0;
            }
            @Override
            protected void onPostExecute(Integer result) {

            }
        };
        task.execute(null,null,null);


    }

    @Override
    protected void onUnregistered(Context context, String s) {

    }
}
