package com.kefirkb.services.senders;

import com.kefirkb.model.PersonalMessage;
import com.kefirkb.services.MessageQueuesHolder;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Simple job which tries to deliver personal messages.
 *
 * @implNote this sender gets messages from personal queue messages and write this message into netty channel of target user
 */
public class PersonalMessageSender implements MessageSender {

    private final ScheduledExecutorService executorService;

    public PersonalMessageSender(int corePoolSize) {
        executorService = Executors.newScheduledThreadPool(corePoolSize);
    }

    @Override
    public void start() {
        executorService.scheduleAtFixedRate(PersonalMessageSender::deliverMessage, 5000, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void deliverMessage() {
        PersonalMessage personalMessage = null;
        try {
            if (!Thread.currentThread().isInterrupted()) {
                personalMessage = MessageQueuesHolder.getNextPersonalMessage(1, TimeUnit.SECONDS);
                if (personalMessage != null && personalMessage.getChannelReceiver().isActive()) {
                    personalMessage.getChannelReceiver().writeAndFlush(personalMessage.getSenderName() + ':' + ' ' + personalMessage.getMessageText() + "\r\n");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }
}
