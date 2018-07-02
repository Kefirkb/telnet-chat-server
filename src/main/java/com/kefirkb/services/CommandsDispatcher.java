package com.kefirkb.services;

import com.kefirkb.TelnetRequestHandler;
import com.kefirkb.processors.CommandProcessor;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Command dispatcher accepts incoming commands string, parses it and delegates execution to specified command processor
 *
 * @see CommandProcessor
 */
public class CommandsDispatcher {
    private final Map<String, CommandProcessor> processorsByName;
    private final MessageService messageService;
    private final AuthService authService;

    private final List<CommandProcessor> isEnsuredAuthCommandList;

    public CommandsDispatcher(Set<CommandProcessor> processors, MessageService messageService, AuthService authService) {
        this.processorsByName = processors.stream().collect(Collectors.toMap(CommandProcessor::getName, Function.identity()));
        this.messageService = messageService;
        this.authService = authService;
        this.isEnsuredAuthCommandList = processorsByName.values()
                .stream()
                .filter(CommandProcessor::isEnsuredAuthorize)
                .collect(Collectors.toList());
    }

    public void dispatch(String commandStr, Channel channel) {
        String[] commandNameWithParams = commandStr.split("[ ]+");

        if (commandStr.isEmpty()) {
            sendFromServer("Try to enter something", channel);
        }
        String commandName = commandNameWithParams[0];

        CommandProcessor commandProcessor = processorsByName.get(commandName);

        if (!authService.isLogged(channel.id()) && commandProcessor == null) {
            sendFromServer("Unknown command!", channel);
            return;
        }

        if (commandProcessor == null) {
            commandProcessor = processorsByName.get("/message");
            commandNameWithParams = new String[]{"/message", commandStr};
        }

        if (isEnsuredAuthCommandList.contains(commandProcessor) && !authService.isLogged(channel.id())) {
            sendFromServer("You should be authorized!", channel);
            return;
        }

        try {
            String result = commandProcessor.process(Arrays.copyOfRange(commandNameWithParams, 1, commandNameWithParams.length), channel);
            if (!result.isEmpty()) {
                sendFromServer(result, channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendFromServer("Internal error", channel);
        }
    }

    private void sendFromServer(String messageText, Channel channel) {
        messageService.sendMessage(TelnetRequestHandler.SERVER_NAME, messageText, channel);
    }
}
