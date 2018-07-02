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
import java.util.stream.Collectors;

public class UserListCommandProcessor implements CommandProcessor {

    private final String commandName = "/users";
    private final UserService userService;
    private final ChatChannelService chatChannelService;
    private final MessageService messageService;

    public UserListCommandProcessor(@Nonnull UserService userService,
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

        if (chatChannel == null) {
            return "You are not in any channel!";
        }
        String userList = chatChannel.getUsers().stream().map(User::getUserName).collect(Collectors.joining(", "));
//        messageService.sendMessage(TelnetRequestHandler.SERVER_NAME,
//                userList,
//                channel);

        return userList;
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
