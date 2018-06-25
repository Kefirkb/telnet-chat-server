package com.kefirkb.model;

import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class PersonalMessage implements Message {
	String messageText;
	String senderName;
	Channel channelReceiver;

	public PersonalMessage(String messageText, String senderName, Channel receiver) {
		this.messageText = messageText;
		this.senderName = senderName;
		this.channelReceiver = receiver;
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

	public Channel getChannelReceiver() {
		return channelReceiver;
	}
}
