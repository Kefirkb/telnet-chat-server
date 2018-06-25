package com.kefirkb.model;

import javax.annotation.Nonnull;

public class BroadCastMessage implements Message {
	String messageText;
	String senderName;
	ChatChannel channelAddress;

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
}
