package com.kefirkb.services;

import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.PersonalMessage;
import com.kefirkb.model.User;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class MessageServiceImpl implements MessageService {

	@Override
	public void sendMessage(@Nonnull String messageText, @Nonnull ChatChannel chatChannel) {
		// do nothing now
	}

	@Override
	public void sendMessage(@Nonnull String senderName, @Nonnull String messageText, @Nonnull Channel channelReceiver) {
		MessageQueuesHolder.putPersonalMessage(new PersonalMessage(messageText, senderName, channelReceiver));
	}
}
