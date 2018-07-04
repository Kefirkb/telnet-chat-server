package com.kefirkb.processors.impl;

import com.kefirkb.exceptions.CommandException;
import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.registries.CommonRegistry;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.MessageService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.kefirkb.registries.ServerMessagesRegistry.*;
import static java.util.Objects.requireNonNull;

public class JoinChannelCommandProcessor implements CommandProcessor {
    private final String commandName = "/join";
    private final ChatChannelService chatChannelService;
    private final UserService userService;
    private final MessageService messageService;

    public JoinChannelCommandProcessor(@Nonnull ChatChannelService chatChannelService,
                                       @Nonnull UserService userService,
                                       @Nonnull MessageService messageService) {
        this.chatChannelService = requireNonNull(chatChannelService, "chatChannelService");
        this.userService = requireNonNull(userService, "userService");
        this.messageService = requireNonNull(messageService, "messageService");
    }

    //TODO should do in transaction
    @Override
    public void process(@Nonnull String[] args, @Nonnull Channel channel) throws CommandException {
        Objects.requireNonNull(args, "args");
        Objects.requireNonNull(channel, "channel");

        if (args.length != 1) {
            throw new CommandException(BAD_PARAMETERS);
        }
        String chatChannelName = args[0];

        if (chatChannelName == null) {
            throw new CommandException(INVALID_CHANNEL_NAME);
        }

        User user = requireNonNull(userService.userByChannelId(channel.id()));

        if (user.getJoinedChatChannel() != null && chatChannelName.equals(user.getJoinedChatChannel().getChatChannelName())) {
            throw new CommandException(user.getUserName() + " is already in " + chatChannelName);
        }

        ChatChannel chatChannel = chatChannelService.chatChannelByName(chatChannelName);

        if (chatChannel == null) {
            chatChannel = new ChatChannel(chatChannelName, user);
            messageService.sendMessage(System.getProperty(CommonRegistry.SERVER_NAME_PROPERTY), CHANNEL_WAS_CREATED + chatChannelName, user.getChannel());
        }

        // TODO need service layer for join/left user to channel
        ChatChannel leftChannel = leftUserCurrentChannel(user);

        joinUserToChatChannel(user, chatChannel);

        persistChanges(user, chatChannel);

        if (leftChannel != null) {
            messageService.sendMessage(user.getUserName(), LEFT_CHANNEL + leftChannel.getChatChannelName(), leftChannel);
        }
        messageService.sendMessage(user.getUserName(), JOINED_TO + chatChannelName, chatChannel);
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

    private void joinUserToChatChannel(User user, ChatChannel chatChannel) {
        chatChannel.joinUser(user);
        user.joinToChannel(chatChannel);
        chatChannel.getLastMessages().stream().forEach(broadCastMessage -> {
                    messageService.sendMessage(broadCastMessage.getSenderName(), broadCastMessage.getMessageText(), user.getChannel());
                }
        );
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
