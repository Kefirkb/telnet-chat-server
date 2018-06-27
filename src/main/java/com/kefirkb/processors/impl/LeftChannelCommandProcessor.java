package com.kefirkb.processors.impl;

import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LeftChannelCommandProcessor implements CommandProcessor {

	private final String commandName = "/left";
	private final UserService userService;
	private final ChatChannelService chatChannelService;

	public LeftChannelCommandProcessor(@Nonnull UserService userService, @Nonnull ChatChannelService chatChannelService) {
		this.userService = userService;
		this.chatChannelService = chatChannelService;
	}

	@Nonnull
	@Override
	public String process(@Nonnull String[] args, @Nonnull Channel channel) throws Exception {
		User user = Objects.requireNonNull(userService.userByChannelId(channel.id()));
		ChatChannel chatChannel = user.getJoinedChatChannel();

		if(chatChannel == null) {
			return "You are not in any channel!";
		}
		user.joinToChannel(null);
		chatChannel = Objects.requireNonNull(chatChannelService.chatChannelByName(chatChannel.getChatChannelName()));
		chatChannel.leftUser(user);

		userService.saveUser(user);
		chatChannelService.saveChatChannel(chatChannel);

		return "You have left " + chatChannel.getChatChannelName();
	}

	@Nonnull
	@Override
	public String getName() {
		return commandName;
	}

	@Override
	public boolean isEnsuredAuthorize() {
		return false;
	}
}
