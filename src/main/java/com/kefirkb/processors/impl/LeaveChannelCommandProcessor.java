package com.kefirkb.processors.impl;

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

import static java.util.Objects.requireNonNull;

public class LeaveChannelCommandProcessor implements CommandProcessor {

    private final String commandName = "/leave";
    private final UserService userService;
    private final ChatChannelService chatChannelService;
    private final MessageService messageService;

    public LeaveChannelCommandProcessor(@Nonnull ChatChannelService chatChannelService,
                                        @Nonnull UserService userService,
                                        @Nonnull MessageService messageService) {
        this.userService = requireNonNull(userService, "userService");
        this.chatChannelService = requireNonNull(chatChannelService, "chatChannelService");
        this.messageService = requireNonNull(messageService, "messageService");
    }

    @Override
    public void process(@Nonnull String[] args, @Nonnull Channel channel) throws CommandException {
        requireNonNull(args, "args");
        requireNonNull(channel, "channel");

        User user = requireNonNull(userService.userByChannelId(channel.id()));
        ChatChannel chatChannel = user.getJoinedChatChannel();

        if (chatChannel == null) {
            throw new CommandException("You are not in any channel!");
        }
        ChatChannel leftChannel = leftUserCurrentChannel(user);
        persistChanges(user, leftChannel);
        messageService.sendMessage(user.getUserName(), "left " + leftChannel.getChatChannelName(), leftChannel);
    }

    private void persistChanges(User user, ChatChannel chatChannel) {
        chatChannelService.saveChatChannel(chatChannel);
        userService.saveUser(user);
    }

    private ChatChannel leftUserCurrentChannel(User user) {
        if (user.getJoinedChatChannel() != null) {
            ChatChannel userChatChannel = requireNonNull(chatChannelService.chatChannelByName(user.getJoinedChatChannel().getChatChannelName()));
            userChatChannel.leaveUser(user);
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
        return false;
    }
}
