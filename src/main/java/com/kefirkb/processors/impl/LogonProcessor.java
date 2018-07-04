package com.kefirkb.processors.impl;

import com.kefirkb.exceptions.AuthException;
import com.kefirkb.exceptions.CommandException;
import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.registries.CommonRegistry;
import com.kefirkb.services.AuthService;
import com.kefirkb.services.MessageService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static com.kefirkb.registries.ServerMessagesRegistry.*;
import static java.util.Objects.requireNonNull;

public class LogonProcessor implements CommandProcessor {
    private static final Logger log = LoggerFactory.getLogger(LogonProcessor.class);

    private final String commandName = "/logon";
    private final AuthService authService;
    private final MessageService messageService;

    public LogonProcessor(@Nonnull AuthService authService, @Nonnull MessageService messageService) {
        this.authService = requireNonNull(authService, "authService");
        this.messageService = requireNonNull(messageService, "messageService");
    }

    @Override
    public void process(@Nonnull String[] args, @Nonnull Channel channel) throws CommandException {
        log.info("Start process command " + commandName + " " + Arrays.deepToString(args));
        requireNonNull(args, "args");
        requireNonNull(channel, "channel");

        if (authService.isLogged(channel.id())) {
            log.error(ALREADY_LOGGED);
            throw new CommandException(ALREADY_LOGGED);
        }
        if (args.length != 2) {
            log.error(BAD_PARAMETERS);
            throw new CommandException(BAD_PARAMETERS);
        }
        boolean logged;
        try {
            logged = authService.tryLogon(args[0], args[1], channel);
        } catch (AuthException ex) {
            log.error(ex.getMessage());
            throw new CommandException(ex.getMessage());
        }

        if (logged) {
            this.messageService.sendMessage(System.getProperty(CommonRegistry.SERVER_NAME_PROPERTY), LOGGED_SUCCESSFULLY, channel);
            log.info("End process command " + commandName + " " + Arrays.deepToString(args));
            return;
        }
        log.error(INVALID_CREDENTIALS);
        throw new CommandException(INVALID_CREDENTIALS);
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
