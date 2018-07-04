package com.kefirkb.services.senders;

import com.kefirkb.model.PersonalMessage;
import com.kefirkb.services.MessageQueuesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(PersonalMessageSender.class);

    private static final int PROCESS_QUEUE_RATE_MILLIS = 100;
    private static final int INITIAL_DELAY_MILLIS = 5000;
    private static final int MAX_TIMEOUT_QUEUE_WAIT_MILLIS = 1000;

    private final ScheduledExecutorService executorService;

    public PersonalMessageSender(int corePoolSize) {
        executorService = Executors.newScheduledThreadPool(corePoolSize, task -> {
            Thread thread = Executors.defaultThreadFactory().newThread(task);
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    public void start() {
        executorService.scheduleAtFixedRate(PersonalMessageSender::deliverMessage, INITIAL_DELAY_MILLIS, PROCESS_QUEUE_RATE_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("Interrupted personal sender", e);
        }
    }

    private static void deliverMessage() {
        PersonalMessage personalMessage;
        try {
            if (!Thread.currentThread().isInterrupted()) {
                personalMessage = MessageQueuesHolder.getNextPersonalMessage(MAX_TIMEOUT_QUEUE_WAIT_MILLIS, TimeUnit.MILLISECONDS);
                if (personalMessage != null && personalMessage.getChannelReceiver().isRegistered()) {
                    personalMessage.getChannelReceiver()
                            .writeAndFlush(personalMessage.getSenderName() + ':' + ' ' + personalMessage.getMessageText() + "\r\n");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted personal sender", e);
        } catch (RejectedExecutionException e) {
            log.error("Error: ", e);
        }
    }
}
