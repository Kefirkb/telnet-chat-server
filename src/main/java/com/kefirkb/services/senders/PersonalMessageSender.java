package com.kefirkb.services.senders;

import com.kefirkb.model.PersonalMessage;
import com.kefirkb.services.MessageQueuesHolder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
		executorService.shutdownNow();
	}

	private static void deliverMessage() {
		PersonalMessage personalMessage = null;
		try {
			personalMessage = MessageQueuesHolder.getNextPersonalMessage();
			personalMessage.getChannelReceiver().writeAndFlush(personalMessage.getSenderName() + ':' + ' ' + personalMessage.getMessageText() + "\r\n");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
