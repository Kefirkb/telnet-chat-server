package com.kefirkb.processors.impl;

import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.MessageService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.Objects;

public class JoinChannelCommandProcessor implements CommandProcessor {
	private final String commandName = "/join";
	private final ChatChannelService chatChannelService;
	private final UserService userService;
	private final MessageService messageService;

	public JoinChannelCommandProcessor(@Nonnull ChatChannelService chatChannelService,
									   @Nonnull UserService userService,
									   @Nonnull MessageService messageService)
	{
		this.chatChannelService = chatChannelService;
		this.userService = userService;
		this.messageService = messageService;
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
			message = "";
		}

		// TODO is it need service layer for join/left user
		ChatChannel leftChannel = leftUserCurrentChannel(user);

		joinUserToChatChannel(user, chatChannel);

		persistChanges(user, chatChannel);

		if(leftChannel != null) {
			messageService.sendMessage(user.getUserName(), "left " + leftChannel.getChatChannelName(), leftChannel);
		}
		messageService.sendMessage(user.getUserName(), "joined to " + chatChannelName, chatChannel);

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

	private ChatChannel leftUserCurrentChannel(User user) {
		if(user.getJoinedChatChannel() != null) {
			ChatChannel userChatChannel = Objects.requireNonNull(chatChannelService.chatChannelByName(user.getJoinedChatChannel().getChatChannelName()));
			userChatChannel.leftUser(user);
			user.joinToChannel(null);
			return chatChannelService.saveChatChannel(userChatChannel);
		}
		return null;
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
