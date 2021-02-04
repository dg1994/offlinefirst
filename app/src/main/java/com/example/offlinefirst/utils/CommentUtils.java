package com.example.offlinefirst.utils;

import com.example.offlinefirst.model.Comment;

public class CommentUtils {
    public static Comment clone(Comment from, boolean syncPending) {
        return new Comment(from.getId(), from.getChatId(), from.getText(),
                from.getTimestamp(), syncPending);
    }

    public static Comment clone(Comment from, long id) {
        return new Comment(id, from.getChatId(), from.getText(), from.getTimestamp(),
                from.isSyncPending());
    }
}
