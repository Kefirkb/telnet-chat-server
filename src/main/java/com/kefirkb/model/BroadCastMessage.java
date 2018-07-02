package com.kefirkb.model;

import javax.annotation.Nonnull;

public class BroadCastMessage implements Message {
	String messageText;
	String senderName;
	ChatChannel channelAddress;

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
