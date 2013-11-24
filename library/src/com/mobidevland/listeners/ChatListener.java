package com.mobidevland.listeners;

import com.mobidevland.objects.Chat;
import com.mobidevland.objects.Message;
import com.mobidevland.objects.User;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public interface ChatListener {
    public void onMessage(Chat message, User emitter);
}
