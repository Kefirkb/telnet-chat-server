package com.kefirkb.services.impl;

import com.kefirkb.model.User;
import com.kefirkb.services.UserService;
import io.netty.channel.ChannelId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Simple dummy user service
 */
public class DummyUserService implements UserService {

	@Nullable
	@Override
	public User userByChannelId(@Nonnull ChannelId channelId) {
		return DummyUserRepository.LOGGED_USERS.get(channelId.asLongText());
	}

	@Nullable
	@Override
	public User saveUser(@Nonnull User user) {
		return DummyUserRepository.LOGGED_USERS.put(user.getChannel().id().asLongText(), user);
	}
}
