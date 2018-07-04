package com.kefirkb.processors.impl;

import com.kefirkb.TelnetRequestHandler;
import com.kefirkb.exceptions.CommandException;
import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.MessageService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.kefirkb.registries.ServerMessagesRegistry.USER_IS_NOT_IN_ANY_CHANNEL;
import static java.util.Objects.requireNonNull;

public class UserListCommandProcessor implements CommandProcessor {

    private final String commandName = "/users";
    private final UserService userService;
    private final MessageService messageService;

    public UserListCommandProcessor(@Nonnull UserService userService,
                                    @Nonnull MessageService messageService) {
        this.userService = requireNonNull(userService, "userService");
        this.messageService = requireNonNull(messageService, "messageService");
    }

    @Override
    public void process(@Nonnull String[] args, @Nonnull Channel channel) throws CommandException {
        requireNonNull(args, "args");
        requireNonNull(channel, "channel");
        User user = requireNonNull(userService.userByChannelId(channel.id()));

        ChatChannel chatChannel = user.getJoinedChatChannel();

        if (chatChannel == null) {
            throw new CommandException(USER_IS_NOT_IN_ANY_CHANNEL);
        }
        String userList = chatChannel.getUsers().stream().map(User::getUserName).collect(Collectors.joining(", "));
        messageService.sendMessage(TelnetRequestHandler.SERVER_NAME,
                userList,
                channel);
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
