package eu.codlab.web;

import android.content.Context;
import android.os.AsyncTask;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kevin on 29/05/13.
 */
public final class DownloadFile extends AsyncTask<String, Double, Long> {
      private URL _url;
      private long total;
      private int phase = 0;
      private String _location;
      private IDownloadFile _listener;
      private boolean _sd_card_exception;
      private boolean _url_exception;

      private DownloadFile(){
            super();
            _sd_card_exception = false;
            _url_exception = false;
          }

      public DownloadFile(Context context, File location, URL url, IDownloadFile listener, String tmp){
        this();
        _url =url;
        _location = location.getAbsolutePath();
        if(!_location.endsWith("/"))
          _location+="/";
        _listener = listener;
      }

    public static String onlyAlphaNumeric(String value){
        return value.replaceAll("[^A-Za-z0-9]", "");
    }

      @Override
      protected Long doInBackground(String... url) {
            int count;
            URLConnection conexion = null;
            int lenghtOfFile = 0;
            InputStream input = null;

          File save = null;
            try {
                  _sd_card_exception = false;
                  phase = 0;
                  //_url = new URL(url[0]);
                //save = new File(_location,onlyAlphaNumeric(onlyAlphaNumeric(_url.getPath())));
                save = new File(_location);
                conexion = _url.openConnection();
                  conexion.connect();
                  // this will be useful so that you can show a tipical 0-100% progress bar
                  lenghtOfFile = conexion.getContentLength();

                  // downlod the file
                  input = new BufferedInputStream(_url.openStream());
                } catch (Exception e) {
                  _url_exception = true;
                  e.printStackTrace();
                  return 0L;
                }
          File f = save;
          if(f.exists()){
              _listener.onFile(f);
          }else{
            try {


                  OutputStream output = new FileOutputStream(f);

                  byte data[] = new byte[1024];

                  total= 0;

                  while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress(((int)((total*50./lenghtOfFile)*1000))*1./1000);
                        output.write(data, 0, count);
                      }

                  output.flush();
                  output.close();
                  input.close();

                } catch (Exception e) {
                  _sd_card_exception = true;
                  e.printStackTrace();
                }
          }
          _listener.onFile(save);
            return total;
          }

      public void onProgressUpdate(Double... args){
            _listener.receiveProgress((phase == 0) ? "% phase 1/2 (50%)" : "% phase 2/2 (50%)", args[0]);
          }

      protected void onPostExecute(Long result) {
            if(_sd_card_exception == true)
              _listener.onErrorSd();
            if(_url_exception == true)
              _listener.onErrorUrl();
            _listener.onPost(result);
          }
}
