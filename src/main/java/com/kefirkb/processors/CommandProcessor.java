package com.kefirkb.processors;

import io.netty.channel.Channel;

import javax.annotation.Nonnull;

/**
 * Simple request processor interface
 */
public interface CommandProcessor {

	// TODO Command dispatcher should delegate sending response to specific queue sender

	/**
	 * Processes incoming command
	 *
	 * @param args    arguments for command
	 * @param channel netty channel source of incoming command
	 *
	 * @return string response
	 *
	 * @throws Exception exception if something wrong
	 */
	@Nonnull
	String process(@Nonnull String[] args,@Nonnull Channel channel) throws Exception;

	/**
	 * Method returns specific name of command
	 *
	 * @return command name
	 */
	@Nonnull
	String getName();
}
