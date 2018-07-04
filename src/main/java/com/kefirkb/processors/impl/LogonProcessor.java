package com.kefirkb.processors.impl;

import com.kefirkb.TelnetRequestHandler;
import com.kefirkb.exceptions.AuthException;
import com.kefirkb.exceptions.CommandException;
import com.kefirkb.model.User;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.AuthService;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.MessageService;
import com.kefirkb.services.UserService;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class LogonProcessor implements CommandProcessor {
    private final String commandName = "/logon";
    private final AuthService authService;
    private final MessageService messageService;

    public LogonProcessor(@Nonnull AuthService authService, @Nonnull MessageService messageService) {
        this.authService = requireNonNull(authService, "authService");
        this.messageService = requireNonNull(messageService, "messageService");
    }

    @Override
    public void process(@Nonnull String[] args, @Nonnull Channel channel) throws CommandException {
        requireNonNull(args, "args");
        requireNonNull(channel, "channel");

        if (authService.isLogged(channel.id())) {
            throw new CommandException("You are already logged.");
        }
        if (args.length != 2) {
            throw new CommandException("You have bad parameters.");
        }
        boolean logged;
        try {
            logged = authService.tryLogon(args[0], args[1], channel);
        } catch (AuthException ex) {
            throw new CommandException(ex.getMessage());
        }

        if (logged) {
            this.messageService.sendMessage(TelnetRequestHandler.SERVER_NAME, "Logged successfully!", channel);
            return;
        }

        throw new CommandException("Invalid password or username");
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
