package com.kefirkb.model;

import javax.annotation.Nonnull;

/**
 * Simple message interface
 */
public interface Message {
	@Nonnull
	String getMessageText();
	@Nonnull
	String getSenderName();
}
