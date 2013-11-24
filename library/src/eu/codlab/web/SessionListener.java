package eu.codlab.web;

/**
 * Created by kevinleperf on 24/11/2013.
 */
public interface SessionListener {
    public void connectionTimedOut();
    public void onChanged(boolean connected);

}
