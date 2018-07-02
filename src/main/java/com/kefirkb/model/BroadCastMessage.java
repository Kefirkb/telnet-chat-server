package com.kefirkb.model;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Message for broadcast delivering to all participants of chat channel
 */
@Immutable
public class BroadCastMessage implements Message {
	private final String messageText;
	private final String senderName;
	private final ChatChannel channelAddress;

	public BroadCastMessage(String senderName, String messageText, ChatChannel channelAddress) {
		this.messageText = messageText;
		this.senderName = senderName;
		this.channelAddress = channelAddress;
	}

	@Nonnull
	@Override
	public String getMessageText() {
		return messageText;
	}

	@Nonnull
	@Override
	public String getSenderName() {
		return senderName;
	}

	public ChatChannel getChannelAddress() {
		return channelAddress;
	}
}
