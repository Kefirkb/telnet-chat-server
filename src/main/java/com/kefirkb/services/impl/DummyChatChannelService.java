package com.kefirkb.services.impl;

import com.kefirkb.model.ChatChannel;
import com.kefirkb.services.ChatChannelService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DummyChatChannelService implements ChatChannelService {
	private static final Map<String, ChatChannel> chatChannels = new ConcurrentHashMap<>();

	@Nullable
	@Override
	public ChatChannel chatChannelByName(@Nonnull String chatChannelName) {
		return chatChannels.get(chatChannelName);
	}

	@Nonnull
	@Override
	public List<ChatChannel> getAllChannels() {
		return Collections.unmodifiableList(new ArrayList<>(chatChannels.values()));
	}

	@Nullable
	@Override
	public ChatChannel saveChatChannel(@Nonnull ChatChannel chatChannel) {
		return chatChannels.put(chatChannel.getChatChannelName(), chatChannel);
	}
}
