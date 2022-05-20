package com.example.offlinefirst.utils;

import com.example.offlinefirst.model.Comment;

public class CommentUtils {
    public static Comment clone(Comment from, boolean syncPending) {
        return new Comment(from.getId(), from.getChatId(), from.getText(),
                from.getTimestamp(), syncPending, from.getFrom());
    }
}
