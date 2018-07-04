package com.kefirkb.processors.impl;

import com.kefirkb.exceptions.CommandException;
import com.kefirkb.model.BroadCastMessage;
import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.MessageService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class SendMessageProcessor implements CommandProcessor {

	private final String commandName = "/message";
	private final UserService userService;
	private final MessageService messageService;

	public SendMessageProcessor(@Nonnull UserService userService,
                                @Nonnull MessageService messageService) {
		this.userService = userService;
		this.messageService = messageService;
	}

	@Override
	public void process(@Nonnull String[] args, @Nonnull Channel channel) throws CommandException {
		requireNonNull(args, "args");
		requireNonNull(channel, "channel");
		User user = requireNonNull(userService.userByChannelId(channel.id()));

		ChatChannel chatChannel = user.getJoinedChatChannel();
		String message = args[0];

		if(chatChannel == null) {
			throw new CommandException("You are not in any channel!");
		}
        chatChannel.pushLastMessage(new BroadCastMessage(user.getUserName(), message, chatChannel));
		messageService.sendMessage(user.getUserName(), message, chatChannel);
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
