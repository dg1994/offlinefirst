package com.example.offlinefirst.utils;

import com.example.offlinefirst.model.Message;

public class MessageUtils {
    public static Message clone(Message from, boolean syncPending) {
        return new Message(from.getId(), from.getChatId(), from.getText(),
                from.getTimestamp(), syncPending, from.getFrom());
    }
}
