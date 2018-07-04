package com.kefirkb.processors.impl;

import com.kefirkb.exceptions.CommandException;
import com.kefirkb.model.BroadCastMessage;
import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.MessageService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static com.kefirkb.registries.ServerMessagesRegistry.USER_IS_NOT_IN_ANY_CHANNEL;
import static java.util.Objects.requireNonNull;

public class SendMessageProcessor implements CommandProcessor {
	private static final Logger log = LoggerFactory.getLogger(SendMessageProcessor.class);

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
		log.info("Start process command " + commandName + " " + Arrays.deepToString(args));
		requireNonNull(args, "args");
		requireNonNull(channel, "channel");
		User user = requireNonNull(userService.userByChannelId(channel.id()));

		ChatChannel chatChannel = user.getJoinedChatChannel();
		String message = args[0];

		if(chatChannel == null) {
			log.error(USER_IS_NOT_IN_ANY_CHANNEL);
			throw new CommandException(USER_IS_NOT_IN_ANY_CHANNEL);
		}
        chatChannel.pushLastMessage(new BroadCastMessage(user.getUserName(), message, chatChannel));
		messageService.sendMessage(user.getUserName(), message, chatChannel);
		log.info("End process command " + commandName + " " + Arrays.deepToString(args));
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
