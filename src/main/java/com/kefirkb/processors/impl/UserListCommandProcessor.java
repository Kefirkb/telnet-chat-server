package com.kefirkb.processors.impl;

import com.kefirkb.exceptions.CommandException;
import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.registries.CommonRegistry;
import com.kefirkb.services.MessageService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.kefirkb.registries.ServerMessagesRegistry.USER_IS_NOT_IN_ANY_CHANNEL;
import static java.util.Objects.requireNonNull;

public class UserListCommandProcessor implements CommandProcessor {
    private static final Logger log = LoggerFactory.getLogger(UserListCommandProcessor.class);

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
        log.info("Start process command " + commandName + " " + Arrays.deepToString(args));
        requireNonNull(args, "args");
        requireNonNull(channel, "channel");
        User user = requireNonNull(userService.userByChannelId(channel.id()));

        ChatChannel chatChannel = user.getJoinedChatChannel();

        if (chatChannel == null) {
            log.error(USER_IS_NOT_IN_ANY_CHANNEL);
            throw new CommandException(USER_IS_NOT_IN_ANY_CHANNEL);
        }
        String userList = chatChannel.getUsers().stream().map(User::getUserName).collect(Collectors.joining(", "));
        messageService.sendMessage(System.getProperty(CommonRegistry.SERVER_NAME_PROPERTY),
                userList,
                channel);
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
