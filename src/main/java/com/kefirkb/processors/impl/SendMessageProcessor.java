package com.kefirkb.processors.impl;

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

public class SendMessageProcessor implements CommandProcessor {

	private final String commandName = "/message";
	private final UserService userService;
	private final ChatChannelService chatChannelService;
	private final MessageService messageService;

	public SendMessageProcessor(@Nonnull UserService userService,
                                @Nonnull ChatChannelService chatChannelService,
                                @Nonnull MessageService messageService) {
		this.userService = userService;
		this.chatChannelService = chatChannelService;
		this.messageService = messageService;
	}

	@Nonnull
	@Override
	public String process(@Nonnull String[] args, @Nonnull Channel channel) throws Exception {
		User user = Objects.requireNonNull(userService.userByChannelId(channel.id()));
		ChatChannel chatChannel = user.getJoinedChatChannel();
		String message = args[0];

		if(chatChannel == null) {
			return "You are not in any channel!";
		}
        chatChannel.pushLastMessage(new BroadCastMessage(user.getUserName(), message, chatChannel));
		messageService.sendMessage(user.getUserName(), message, chatChannel);

		return "";
	}

	private void persistChanges(User user, ChatChannel chatChannel) {
		chatChannelService.saveChatChannel(chatChannel);
		userService.saveUser(user);
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