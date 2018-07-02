package com.kefirkb.services.impl;

import com.kefirkb.model.User;
import com.kefirkb.services.AuthService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple stub service for auth
 */
public class DummyAuthService implements AuthService {

	private static final Map<String, String> PERSISTED_USERS_REGISTRY =
			new HashMap<String, String>() {{
				put("user0", "user0");
				put("user1", "user1");
				put("user2", "user2");
				put("user3", "user3");
				put("user4", "user4");
				put("user5", "user5");
				put("user6", "user6");
				put("user7", "user7");
				put("user8", "user8");
				put("user9", "user9");
				put("user10", "user10");
			}};
	private static final Set<String> LOGGED_CHANNELS = ConcurrentHashMap.newKeySet();

	@Override
	public boolean tryLogon(@Nonnull String userName, @Nonnull String password, @Nonnull Channel channel) throws Exception {
		Objects.requireNonNull(userName);
		ChannelId channelId = channel.id();

		if (isLogged(channelId)) {
			throw new Exception("You already logged!");
		}

		boolean logged = password.equals(PERSISTED_USERS_REGISTRY.get(userName));

		if (logged) {
			DummyUserRepository.LOGGED_USERS.put(channelId.asLongText(), new User(channel, userName));
			LOGGED_CHANNELS.add(channelId.asLongText());
			return true;
		}
		return false;
	}

	@Override
	public boolean isLogged(@Nonnull ChannelId channelId) {
		return LOGGED_CHANNELS.contains(channelId.asLongText());
	}

	@Override
	public void logoutUser(ChannelId channelId) {
		LOGGED_CHANNELS.remove(channelId.asLongText());
		DummyUserRepository.LOGGED_USERS.remove(channelId.asLongText());
	}
}
