package com.example.offlinefirst.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.offlinefirst.domain.repository.LocalMessageRepository;
import com.example.offlinefirst.model.Message;
import com.example.offlinefirst.utils.MessageUtils;
import com.example.offlinefirst.utils.Constants;

import javax.inject.Inject;

public class MessageSaveWorker extends Worker {

    private LocalMessageRepository localMessageRepository;

    public MessageSaveWorker(LocalMessageRepository localMessageRepository,
                             @NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.localMessageRepository = localMessageRepository;
    }

    @NonNull
    @Override
    public Result doWork() {
        String messageId = getInputData().getString(Constants.KEY_COMMENT_ID);
        boolean syncPending = getInputData().getBoolean(Constants.KEY_COMMENT_SYNC_PENDING, false);
        Message message = localMessageRepository.getMessage(messageId);
        Message updatedMessage = MessageUtils.clone(message, syncPending);
        localMessageRepository.update(updatedMessage);
        return Result.success();
    }

    public static class Factory implements ChildWorkerFactory {

        private LocalMessageRepository localMessageRepository;

        @Inject
        public Factory(LocalMessageRepository localMessageRepository){
            this.localMessageRepository = localMessageRepository;
        }

        @Override
        public Worker create(Context appContext, WorkerParameters params) {
            return new MessageSaveWorker(localMessageRepository, appContext, params);
        }
    }
}
