package com.kefirkb.model;

import io.netty.channel.Channel;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

/**
 * Message user directly delivering message
 */
public class PersonalMessage implements Message {
    private final String messageText;
    private final String senderName;
    private final Channel channelReceiver;

    public PersonalMessage(@Nonnull String messageText, @Nonnull String senderName, @Nonnull Channel receiver) {
        this.messageText = requireNonNull(messageText, "messageText");
        this.senderName = requireNonNull(senderName, "senderName");
        this.channelReceiver = requireNonNull(receiver, "receiver");
    }

    @Nonnull
    @Override
    public String getMessageText() {
        return messageText;
    }

    @Nonnull
    @Override
    public String getSenderName() {
        return senderName;
    }

    public Channel getChannelReceiver() {
        return channelReceiver;
    }
}
