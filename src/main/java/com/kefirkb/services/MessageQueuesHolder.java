package com.kefirkb.services;

import com.kefirkb.model.BroadCastMessage;
import com.kefirkb.model.PersonalMessage;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Simple message queues container
 */
public final class MessageQueuesHolder {

	private static final BlockingQueue<PersonalMessage> QUEUE_FOR_PERSONAL_MESSAGES = new LinkedBlockingQueue<>();
	private static final Map<String, BlockingQueue<BroadCastMessage>> MESSAGE_QUEUE_BY_CHAT_CHANNEL_NAME = new ConcurrentHashMap<>();

	public static void putPersonalMessage(PersonalMessage personalMessage) {
		QUEUE_FOR_PERSONAL_MESSAGES.add(personalMessage);
	}

	public static void putMessageToChatChannel(String chatChannelName, BroadCastMessage broadCastMessage) {
		MESSAGE_QUEUE_BY_CHAT_CHANNEL_NAME.get(chatChannelName).add(broadCastMessage);
	}

	public static void createChatChannelQueue(String chatChannelName) {
		MESSAGE_QUEUE_BY_CHAT_CHANNEL_NAME.putIfAbsent(chatChannelName, new LinkedBlockingQueue<>());
	}

	public static PersonalMessage getNextPersonalMessage(long timeOut, TimeUnit timeUnit) throws InterruptedException {
		return QUEUE_FOR_PERSONAL_MESSAGES.poll(timeOut, timeUnit);
	}

	public static BroadCastMessage getNextChatChannelMessage(String chatChannelName, long timeOut, TimeUnit timeUnit) throws InterruptedException {
		return MESSAGE_QUEUE_BY_CHAT_CHANNEL_NAME.get(chatChannelName).poll(timeOut, timeUnit);
	}

	public static Set<String> availableChatChannels() {
		return MESSAGE_QUEUE_BY_CHAT_CHANNEL_NAME.keySet();
	}

	public static void clearQueues() {
		QUEUE_FOR_PERSONAL_MESSAGES.clear();
		MESSAGE_QUEUE_BY_CHAT_CHANNEL_NAME.clear();
	}
}
