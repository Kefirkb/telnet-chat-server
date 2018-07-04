package com.kefirkb.registries;

public final class ServerMessagesRegistry {

    private ServerMessagesRegistry() {
        throw new UnsupportedOperationException("Create instance unsupported");
    }

    public static final String BAD_PARAMETERS = "You have bad parameters.";
    public static final String INVALID_CHANNEL_NAME = "Channel name should be specified";
    public static final String CHANNEL_WAS_CREATED = "Channel was created: ";
    public static final String LEFT_CHANNEL = "left ";
    public static final String JOINED_TO = "joined to ";
    public static final String USER_IS_NOT_IN_ANY_CHANNEL = "You are not in any channel!";
    public static final String ALREADY_LOGGED = "You are already logged.";
    public static final String LOGGED_SUCCESSFULLY = "Logged successfully!";
    public static final String INVALID_CREDENTIALS = "Invalid password or username";
}
