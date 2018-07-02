package com.kefirkb.services.senders;

import com.kefirkb.model.BroadCastMessage;
import com.kefirkb.model.PersonalMessage;
import com.kefirkb.services.MessageQueuesHolder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Simple job which tries to deliver broadcast messages.
 *
 * @implNote this sender gets messages from chat channel queue messages,
 * map this message to personal message and put this message into personal messages queue
 */
public class BroadCastMessageSender implements MessageSender {
    private final ScheduledExecutorService executorService;

    public BroadCastMessageSender(int corePoolSize) {
        executorService = Executors.newScheduledThreadPool(corePoolSize);
    }

    @Override
    public void start() {
        executorService.scheduleAtFixedRate(BroadCastMessageSender::deliverMessage, 5000, 100, TimeUnit.MILLISECONDS);
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
        MessageQueuesHolder.availableChatChannels().forEach(
                chatChannelName -> {
                    try {
                        BroadCastMessage broadCastMessage = MessageQueuesHolder.getNextChatChannelMessage(chatChannelName, 500, TimeUnit.MILLISECONDS);
                        if (broadCastMessage != null) {
                            broadCastMessage.getChannelAddress().getUsers().forEach(
                                    user -> {
                                        if (!Thread.currentThread().isInterrupted()) {
                                            MessageQueuesHolder.putPersonalMessage(
                                                    new PersonalMessage(broadCastMessage.getMessageText(), broadCastMessage.getSenderName(), user.getChannel()));
                                        }
                                    }
                            );
                        }
                    } catch (Throwable e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }
        );
    }
}
