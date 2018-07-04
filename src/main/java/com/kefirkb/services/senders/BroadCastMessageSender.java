package com.kefirkb.services.senders;

import com.kefirkb.model.BroadCastMessage;
import com.kefirkb.model.PersonalMessage;
import com.kefirkb.services.MessageQueuesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(BroadCastMessageSender.class);

    private static final int PROCESS_QUEUE_RATE_MILLIS = 100;
    private static final int INITIAL_DELAY_MILLIS = 5000;
    private static final int MAX_TIMEOUT_QUEUE_WAIT_MILLIS = 500;

    private final ScheduledExecutorService executorService;

    public BroadCastMessageSender(int corePoolSize) {
        executorService = Executors.newScheduledThreadPool(corePoolSize, task -> {
            Thread thread = Executors.defaultThreadFactory().newThread(task);
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    public void start() {
        executorService.scheduleAtFixedRate(BroadCastMessageSender::deliverMessage, INITIAL_DELAY_MILLIS, PROCESS_QUEUE_RATE_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("Interrupted broadcast sender ",e);
        }
    }

    private static void deliverMessage() {
        MessageQueuesHolder.availableChatChannels().forEach(
                chatChannelName -> {
                    try {
                        BroadCastMessage broadCastMessage = MessageQueuesHolder.getNextChatChannelMessage(chatChannelName, MAX_TIMEOUT_QUEUE_WAIT_MILLIS, TimeUnit.MILLISECONDS);
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
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.warn("Interrupted personal sender", e);
                    }
                }
        );
    }
}
