package com.kefirkb.model;

import javax.annotation.Nonnull;

public class PersonalMessage implements Message {
	String messageText;
	String senderName;
	User userAddress;

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
