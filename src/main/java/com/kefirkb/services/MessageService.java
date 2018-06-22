package com.kefirkb.services;

import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;

import javax.annotation.Nonnull;

/**
 * Service for sending messages
 */
public interface MessageService {
	/**
	 * Method sends message to channel.
	 *
	 * @param messageText specified message text
	 * @param chatChannel destination for message
	 *
	 * @implNote must be thread-safe
	 * @see com.kefirkb.model.BroadCastMessage
	 */
	void sendMessage(@Nonnull String messageText, @Nonnull ChatChannel chatChannel);

	/**
	 * Method sends message to concrete user
	 *
	 * @param messageText specified message text
	 * @param user        destination for message
	 *
	 * @implNote must be thread-safe
	 * @see com.kefirkb.model.PersonalMessage
	 */
	void sendMessage(@Nonnull String messageText, @Nonnull User user);
}
