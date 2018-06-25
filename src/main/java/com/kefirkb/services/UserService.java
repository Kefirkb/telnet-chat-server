package com.kefirkb.services;

import com.kefirkb.model.User;
import io.netty.channel.ChannelId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Service for get user by netty channel id
 *
 * @see User
 */
public interface UserService {
	/**
	 * Method gets users by netty channel id
	 *
	 * @param channelId netty channel id
	 *
	 * @return found user of null if not exists
	 * @implNote must be thread-safe
	 */
	@Nullable
	User userByChannelId(@Nonnull ChannelId channelId);
}
