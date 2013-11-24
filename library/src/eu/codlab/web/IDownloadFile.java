package eu.codlab.web;

import java.io.File;

/**
 * Created by kevin on 29/05/13.
 */
public interface IDownloadFile {
    public void receiveProgress(String msg, Double args);

    public void onFile(File file);
    public void onFile(String file);
    public void onPost(Long result);

    public void onErrorSd();

    public void onErrorUrl();
}
