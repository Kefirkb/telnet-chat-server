package com.kefirkb.services;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import javax.annotation.Nonnull;

/**
 * Service for authentication.
 */
public interface AuthService {
	/**
	 * Method tries to logon user by userName and password
	 *
	 * @param userName userName name
	 * @param password user password
	 * @param channel  netty channel
	 *
	 * @return true if successfully
	 *
	 * @throws Exception if currently logged
	 */
	boolean tryLogon(@Nonnull String userName, @Nonnull String password, @Nonnull Channel channel) throws Exception;

	/**
	 * Method checks that user by channelId is logged
	 *
	 * @param channelId netty channel id
	 *
	 * @return true if logged
	 */
	boolean isLogged(@Nonnull ChannelId channelId);

	/**
	 * Method logout users
	 *
	 * @param channelId channel id for logout
	 */
	void logoutUser(ChannelId channelId);
}
