package eu.codlab.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kevin on 30/05/13.
 */
public class Cache {
    private Context _context;
    private JSONObject _metadata;
    private String _data;
    private String _name;

    private long _actual_invalidate_time = -1;

    public Cache(Context context, String name, int delta){
        _context = context;
        _name = name;
        _metadata = new JSONObject();
        if(getFileMetaData().exists()){
            try{
                String metadata = readFileAsString(getFileMetaData().getAbsolutePath());
                JSONObject tmp = new JSONObject(metadata);
                _metadata = tmp;

                _actual_invalidate_time = _metadata.optLong("invalidate",-1);

            }catch(Exception e){}
        }
        if(delta > 0){
            try{
                _metadata.put("invalidate", System.currentTimeMillis()+delta);
            }catch(Exception e){}
        }
    }

    public File getFile(){
        File cacheDir = _context.getCacheDir();
        File subImg = new File(cacheDir,"data");
        subImg.mkdirs();
        subImg = new File(subImg, DownloadFile.onlyAlphaNumeric(_name)+".json");
        return subImg;
    }

    public File getFileMetaData(){
        File cacheDir = _context.getCacheDir();
        File subImg = new File(cacheDir,"data");
        subImg.mkdirs();
        subImg = new File(subImg, DownloadFile.onlyAlphaNumeric(_name)+".meta");
        return subImg;
    }

    public boolean exists(){
        return getFile().exists();
    }

    public String read(){
        try{
            String data = readFileAsString(getFile().getAbsolutePath());
            _data = data;
            return data;
        }catch(Exception e){}
        return null;
    }

    public Bitmap readBitmap(){
        return BitmapFactory.decodeFile(getFile().getAbsolutePath());
    }

    public boolean writeBitmap(Bitmap bitmap){
        boolean bool = false;
        try{
            File file = getFile();
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);


            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

            bool = true;

            if(_metadata != null)
                _write(getFileMetaData(), _metadata.toString());

        }catch (Exception e){}
        return bool;
    }
    public boolean write(String data){
        if(_metadata != null)
            _write(getFileMetaData(), _metadata.toString());
        return _write(getFile(), data);
    }

    private boolean _write(File file, String data){
        _data = data;
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter( new FileWriter(file.getAbsoluteFile()));
            writer.write(data);
        }
        catch ( IOException e){}
        finally{
            try{
                if ( writer != null)
                    writer.close( );
                return true;
            }
            catch ( IOException e){}
        }
        return false;
    }

    private boolean _shouldRecreate(){
        if(_actual_invalidate_time > 0){
            return _actual_invalidate_time < System.currentTimeMillis();
        }else if(_actual_invalidate_time < 0){
            return _metadata != null && _metadata.has("invalidate") && _metadata.optLong("invalidate") < System.currentTimeMillis() ? true : false;
        }
        return false;
    }

    public boolean shouldRecreate(){
        boolean ret = _shouldRecreate();
        Log.d("shouldRecreate", ret + "");

        return ret;
    }

    private static String readFileAsString(String filePath)
            throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
