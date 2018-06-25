package com.kefirkb.services.senders;

/**
 * Basic interface for message delivering to recipients. Message sender consumes messages from queue and sends to recipient
 */
public interface MessageSender {

	/**
	 * Method starts job for delivering messages
	 */
	void start();

	/**
	 * Method stops job
	 */
	void stop();
}
