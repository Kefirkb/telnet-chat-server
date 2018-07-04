package com.kefirkb.exceptions;

/**
 * Exception if command execution is failed
 */
public class CommandException extends Exception {

	public CommandException(String message) {
		super(message);
	}
}
