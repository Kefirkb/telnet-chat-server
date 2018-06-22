package com.kefirkb.processors;

/**
 * Simple request processor interface
 */
public interface RequestProcessor {
	/**
	 * Processes incoming request
	 *
	 * @param request string request
	 */
	void process(String request);
}
