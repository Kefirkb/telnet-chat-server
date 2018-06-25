package com.kefirkb.model;

import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class User {
	private ChatChannel joinedChatChannel;
	private final Channel channel;

	public User(@Nonnull Channel channel) {
		this.channel = Objects.requireNonNull(channel);
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
