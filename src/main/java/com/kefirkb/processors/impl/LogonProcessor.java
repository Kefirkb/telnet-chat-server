package com.kefirkb.processors.impl;

import com.kefirkb.processors.CommandProcessor;
import com.kefirkb.services.AuthService;
import com.kefirkb.services.ChatChannelService;
import com.kefirkb.services.MessageService;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

public class LogonProcessor implements CommandProcessor {
	private final AuthService authService;

	private final String commandName = "/logon";

	public LogonProcessor(@Nonnull  AuthService authService) {
		this.authService = authService;
	}

	@Nonnull
	@Override
	public String process(@Nonnull String[] args, @Nonnull Channel channel) throws Exception {

		if (authService.isLogged(channel.id())) {
			return "You are already logged.";
		}
		if (args.length != 2) {
			return "You have bad parameters.";
		}
		boolean logged = authService.tryLogon(args[0], args[1], channel);

		if (logged) {
			return "Logged successfully!";
		}
		return "Invalid password or username";
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
