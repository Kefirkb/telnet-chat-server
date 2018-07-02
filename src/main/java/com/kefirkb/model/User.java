package com.kefirkb.model;

import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Simple user entity
 */
public class User {
	private ChatChannel joinedChatChannel;
	private final Channel channel;
	private String userName;

	public User(@Nonnull Channel channel, @Nonnull String userName) {
		this.channel = Objects.requireNonNull(channel);
		this.userName = userName;
	}

	public void joinToChannel(@Nullable ChatChannel joinedChatChannel) {
		this.joinedChatChannel = joinedChatChannel;
	}

	@Nonnull
	public String getUserName() {
		return userName;
	}

	@Nonnull
	public Channel getChannel() {
		return channel;
	}

	@Nullable
	public ChatChannel getJoinedChatChannel() {
		return joinedChatChannel;
	}
}
