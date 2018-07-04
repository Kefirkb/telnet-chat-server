package com.kefirkb.processors;

import com.kefirkb.exceptions.CommandException;
import io.netty.channel.Channel;

import javax.annotation.Nonnull;

/**
 * Simple commands processor interface
 */
public interface CommandProcessor {

    /**
     * Processes incoming command
     * You should validate isEnsuredAuthorize before execution externally
     *
     * @param args    arguments for command
     * @param channel netty channel source of incoming command
     * @throws CommandException exception if something wrong
     * @see CommandProcessor#isEnsuredAuthorize()
     */
    void process(@Nonnull String[] args, @Nonnull Channel channel) throws CommandException;

    /**
     * Method returns specific name of command
     *
     * @return command name
     */
    @Nonnull
    String getName();

    /**
     * Marks that for execution command user should be authorized
     *
     * @return true if user it need to authorize before execution this command
     */
    boolean isEnsuredAuthorize();
}
