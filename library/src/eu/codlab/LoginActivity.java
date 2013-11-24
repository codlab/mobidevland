package eu.codlab;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import android.app.ProgressDialog;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mobidevland.library.R;

import eu.codlab.web.DownloaderService;
import eu.codlab.web.DownloaderServiceObject;
import eu.codlab.web.ServiceWeb;

public class LoginActivity extends Activity {
    /********
     *
     * SET HERE THE SECOND LOGINACTIVITY TO YOUR FINAL ACTIVITY WHZRE YOU WILL CONNECTED
     *
     *
     *
     *
     */
    private Class mNextActivity = LoginActivity.class;


    private ProgressDialog _progress;
    private void quitProgress(){
        synchronized (this){
            if(_progress != null)
                _progress.dismiss();
            _progress = null;
        }
    }

    private static int _download;

    private void attemptLoad(final Intent intent){
        synchronized(this){
            if(ServiceWeb.getService() != null && ServiceWeb.getService().getUser() != null && ServiceWeb.getService().getUser() != null){
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        }
    }

    private void quitAndLoad(final Intent intent){
        _download = 1;
        attemptLoad(intent);
        quitProgress();
    }

    private void showWaiting(){
        _progress = ProgressDialog.show(LoginActivity.this, "Login...","Veuillez patienter", true);
        if(!_progress.isShowing())
            _progress.show();
    }
    private synchronized void quitWaiting(){
        //_download = 1;
        if(_progress != null && _progress.isShowing()){
            _progress.dismiss();
            _progress = null;
        }
    }
    private void quitWaitingError(){
        _download = 1;
        if(_progress.isShowing()){
            _progress.dismiss();
            _progress = null;
        }
    }

    private void startWaiting(Intent i){
        final Intent intent = i;
        Thread t = new Thread(){
            @Override
            public void run(){

                Intent sub_intent = new Intent(LoginActivity.this, DownloaderService.class);
                sub_intent.setAction(DownloaderService.ACTION_DOWNLOAD_USER);
                startService(sub_intent);

                do{
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){}
                }while(ServiceWeb.getService() == null ||
                        ServiceWeb.getService().getUser() == null);
                quitAndLoad(intent);
                _download = 1;
                quitProgress();
            }
        };
        t.start();

    }

    private final class LoginTask extends AsyncTask<String, String , String>{
		private final String _user;
		private final String _password;
		private final boolean _simplified;
		public LoginTask(boolean simplified, String user, String password){
			_simplified = simplified;

			if(user == null)
				user="null";

			if(password == null)
				password="null";
			try {
				user = URLEncoder.encode(user, "UTF-8");
				password = URLEncoder.encode(password, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				user="null";
				password="null";
				e.printStackTrace();
			}
			_user=user;
			_password=password;
		}
		@Override
		protected String doInBackground(String... arg0) {
            _download = 2;
			try {
				final String res = DownloaderServiceObject.get(new URL(DownloaderServiceObject.getUrl() + "service/login?login=" + _user + "&password=" + _password));
				if(res != null){
					try{
						JSONObject json = new JSONObject(res);

						if(json != null && json.has("token")){
                            LoginActivity.this.getSharedPreferences(DownloaderServiceObject.getSharedName(), 0).edit().putString("user", ((TextView)findViewById(R.id.login_user)).getText().toString()).putString("password", ((TextView)findViewById(R.id.login_password)).getText().toString()).putString("token", json.getString("token")).commit();
                            startService(new Intent(LoginActivity.this, ServiceWeb.class));
							Intent i = null;
								i = new Intent(LoginActivity.this, mNextActivity);

                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            startWaiting(i);
						}else{
							//login failed
                            quitWaitingError();
						}
					}catch(Exception e){
						//login failed
                        quitWaitingError();
					}
				}else{
                    quitWaitingError();
				}
			} catch (Exception e) {
				e.printStackTrace();
                quitWaitingError();
			}
			return null;
		}
	}

    @Override
    public void onPause(){
        super.onPause();

        quitWaiting();
    }

    @Override
    public void onResume(){
        super.onResume();

        if(_download==2){
            showWaiting();
        }else{
            Intent intent = new Intent(this, mNextActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            attemptLoad(intent);
        }

    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


        //TODO remove after completion of the app
		Button b = null;//(Button)findViewById(R.id.login_login);
		Button b2 = (Button)findViewById(R.id.login_login2);
        if(b != null)
            b.requestFocus();
		if(b2 != null)
            b2.requestFocus();
		TextView text = (TextView)findViewById(R.id.login_user);
		if(text != null){
			String tt = this.getSharedPreferences(DownloaderServiceObject.getSharedName(), 0).getString("user", "");
			text.setText(tt);
		}
		text = (TextView)findViewById(R.id.login_password);
		if(text != null){
			String tt = this.getSharedPreferences(DownloaderServiceObject.getSharedName(), 0).getString("password", "");
			text.setText(tt);
		}
        if(b != null)
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                _download = 2;
				LoginTask t = new LoginTask(true,((TextView)findViewById(R.id.login_user)).getText().toString(),
						((TextView)findViewById(R.id.login_password)).getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    t.execute();
			}

		});
		b2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
                showWaiting();
                _download = 2;
				LoginTask t = new LoginTask(false,((TextView)findViewById(R.id.login_user)).getText().toString(),
						((TextView)findViewById(R.id.login_password)).getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    t.execute();
			}

		});
	}

}