package com.kefirkb.services.senders;

import com.kefirkb.model.BroadCastMessage;
import com.kefirkb.services.MessageQueuesHolder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BroadCastMessageSender implements MessageSender {
	private final ScheduledExecutorService executorService;

	public BroadCastMessageSender(int corePoolSize) {
		executorService = Executors.newScheduledThreadPool(corePoolSize);
	}

	@Override
	public void start() {
		executorService.scheduleAtFixedRate(BroadCastMessageSender::deliverMessage, 5000, 2000, TimeUnit.MILLISECONDS);
	}

	@Override
	public void stop() {
	}

	private static void deliverMessage() {
		MessageQueuesHolder.availableChatChannels().forEach(
				chatChannelName -> {
					try {
						BroadCastMessage broadCastMessage = MessageQueuesHolder.getNextChatChannelMessage(chatChannelName, 500, TimeUnit.MILLISECONDS);
						if (broadCastMessage != null) {
							// TODO should make parrallel sending message
							// this is correct behavior.
							// If while message sending chat channel was changed (new user joined) there is correctly that he will not receive older messages
							broadCastMessage.getChannelAddress().getUsers().forEach(
									user -> {
										user.getChannel().write(broadCastMessage.getSenderName() + ": " + broadCastMessage.getMessageText() + System.lineSeparator());
										user.getChannel().flush();
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
