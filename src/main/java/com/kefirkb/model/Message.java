package com.kefirkb.model;

import javax.annotation.Nonnull;

/**
 * Simple message interface
 */
public interface Message {
	/**
	 * Method returns text of message
	 *
	 * @return text of message
	 */
	@Nonnull
	String getMessageText();

	/**
	 * Method returns sender name
	 *
	 * @return sender name
	 */
	@Nonnull
	String getSenderName();
}
