package eu.codlab.web;

/**
 * Created by kevinleperf on 24/11/2013.
 */
public interface SessionListener {
    /**
     * Call to this method are done from background processes
     * Do not perform Ui modification from here
     */
    public void connectionTimedOut();

    /**
     * Call to this method are done from background processes
     * Do not perform Ui modification from here
     *
     * @param connected
     */
    public void onChanged(boolean connected);

}
