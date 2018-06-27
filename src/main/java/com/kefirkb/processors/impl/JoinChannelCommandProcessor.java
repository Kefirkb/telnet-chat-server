package com.kefirkb.processors.impl;

import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.Objects;

public class JoinChannelCommandProcessor implements CommandProcessor {
	private final String commandName = "/join";
	private final ChatChannelService chatChannelService;
	private final UserService userService;

	public JoinChannelCommandProcessor(@Nonnull ChatChannelService chatChannelService,
									   @Nonnull UserService userService)
	{
		this.chatChannelService = chatChannelService;
		this.userService = userService;
	}

	//TODO should do in transaction
	//TODO Create transaction guard or use Spring transactional
	@Nonnull
	@Override
	public String process(@Nonnull String[] args, @Nonnull Channel channel) throws Exception {

		if (args.length != 1) {
			return "You have bad parameters.";
		}
		String chatChannelName = args[0];

		if (chatChannelName == null) {
			return "Channel name should be specified";
		}

		User user = Objects.requireNonNull(userService.userByChannelId(channel.id()));

		if(user.getJoinedChatChannel() != null && chatChannelName.equals(user.getJoinedChatChannel().getChatChannelName())) {
			return user.getUserName() + " is already in " + chatChannelName;
		}

		ChatChannel chatChannel = chatChannelService.chatChannelByName(chatChannelName);
		String message;

		if (chatChannel == null) {
			chatChannel = new ChatChannel(chatChannelName, user);
			message = "Channel was created: " + chatChannelName;
		} else {
			// TODO should send broadcast messages to all joined users
			message = user.getUserName() + " joined to " + chatChannelName;
		}

		// TODO is it need service layer for join/unjoin user
		leftUserCurrentChannel(user, chatChannel);

		joinUserToChatChannel(user, chatChannel);

		persistChanges(user, chatChannel);

		return message;
	}

	private void persistChanges(User user, ChatChannel chatChannel) {
		chatChannelService.saveChatChannel(chatChannel);
		userService.saveUser(user);
	}

	private void joinUserToChatChannel(User user, ChatChannel chatChannel) {
		chatChannel.joinUser(user);
		user.joinToChannel(chatChannel);
	}

	private void leftUserCurrentChannel(User user, ChatChannel chatChannel) {
		if(user.getJoinedChatChannel() != null) {
			ChatChannel userChatChannel = Objects.requireNonNull(chatChannelService.chatChannelByName(user.getJoinedChatChannel().getChatChannelName()));
			userChatChannel.leftUser(user);
			chatChannelService.saveChatChannel(chatChannel);
		}
	}

	@Nonnull
	@Override
	public String getName() {
		return commandName;
	}

	@Override
	public boolean isEnsuredAuthorize() {
		return true;
	}
}
