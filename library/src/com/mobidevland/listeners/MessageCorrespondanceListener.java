package com.mobidevland.listeners;

import com.mobidevland.controller.MessagesController;
import com.mobidevland.objects.User;

/**
 * Created by kevinleperf on 23/11/2013.
 */
public interface MessageCorrespondanceListener {
    public void onCorrespondance(MessagesController controller, long id_emitter, long id_receiver, User emitter, User receiver);
}
