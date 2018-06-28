package com.kefirkb.processors;

import io.netty.channel.Channel;

import javax.annotation.Nonnull;

/**
 * Simple request processor interface
 */
public interface CommandProcessor {

	/**
	 * Processes incoming command
	 * You should validate isEnsuredAuthorize before execution externally
	 * @see CommandProcessor#isEnsuredAuthorize()
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

	/**
	 * Marks that for execution command user should be authorized
	 * @return true if user it need to authorize before execution this command
	 */
	boolean isEnsuredAuthorize();
}
