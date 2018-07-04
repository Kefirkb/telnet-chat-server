package com.kefirkb.processors.impl;

import com.kefirkb.exceptions.CommandException;
import com.kefirkb.model.ChatChannel;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.MessageService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static com.kefirkb.registries.ServerMessagesRegistry.LEFT_CHANNEL;
import static com.kefirkb.registries.ServerMessagesRegistry.USER_IS_NOT_IN_ANY_CHANNEL;
import static java.util.Objects.requireNonNull;

public class LeaveChannelCommandProcessor implements CommandProcessor {
    private static final Logger log = LoggerFactory.getLogger(LeaveChannelCommandProcessor.class);

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
        log.info("Start process command " + commandName + " " + Arrays.deepToString(args));

        requireNonNull(args, "args");
        requireNonNull(channel, "channel");

        User user = requireNonNull(userService.userByChannelId(channel.id()));
        ChatChannel chatChannel = user.getJoinedChatChannel();

        if (chatChannel == null) {
            log.error(USER_IS_NOT_IN_ANY_CHANNEL);
            throw new CommandException(USER_IS_NOT_IN_ANY_CHANNEL);
        }
        ChatChannel leftChannel = leftUserCurrentChannel(user);
        persistChanges(user, leftChannel);

        if (leftChannel != null)
            messageService.sendMessage(user.getUserName(), LEFT_CHANNEL + leftChannel.getChatChannelName(), leftChannel);
        log.info("End process command " + commandName + " " + Arrays.deepToString(args));
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
