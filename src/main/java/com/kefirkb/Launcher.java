package com.kefirkb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

public class Launcher {

	private static volatile TelnetServer telnetServer;

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new GenericGroovyApplicationContext("groovy/context.groovy");
		telnetServer = context.getBean(TelnetServer.class);
		telnetServer.start();
		Runtime.getRuntime().addShutdownHook(new Thread(Launcher::stop));
	}

	public static void stop() {
		telnetServer.stop();
	}
}
