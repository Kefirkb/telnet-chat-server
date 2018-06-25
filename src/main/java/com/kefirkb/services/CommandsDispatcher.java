package com.kefirkb.services;

import com.kefirkb.processors.CommandProcessor;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandsDispatcher {
	private final Map<String, CommandProcessor> processorsByName;

	public CommandsDispatcher(Set<CommandProcessor> processors) {
		this.processorsByName = processors.stream().collect(Collectors.toMap(CommandProcessor::getName, Function.identity()));
	}

	public String dispatch(String commandStr, Channel channel) {
		String[] commandNameWithParams = commandStr.split("[ ]+");
		// Generate and write a response.

		if (commandStr.isEmpty()) {
			return "Try to enter something";
			// TODO should send message error to personal queue

		}
		String commandName = commandNameWithParams[0];

		CommandProcessor commandProcessor = processorsByName.get(commandName);

		if(commandProcessor == null) {
			return "Unknown command!";
		}

		try {
			return commandProcessor.process(Arrays.copyOfRange(commandNameWithParams,1, commandNameWithParams.length), channel);
		} catch (Exception e) {
			// TODO send personal message with error
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
