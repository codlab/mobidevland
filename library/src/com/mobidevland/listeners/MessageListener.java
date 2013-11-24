package com.mobidevland.listeners;

import com.mobidevland.objects.Message;
import com.mobidevland.objects.User;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public interface MessageListener {
    public void onMessage(Message message, User emitter, User receiver);
}
