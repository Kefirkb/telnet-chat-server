package com.kefirkb;

import com.kefirkb.services.senders.PersonalMessageSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

public class Launcher {

	private static volatile TelnetServer telnetServer;
	private static volatile PersonalMessageSender personalMessageSender;

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new GenericGroovyApplicationContext("groovy/context.groovy");
		telnetServer = context.getBean(TelnetServer.class);
		personalMessageSender = context.getBean(PersonalMessageSender.class);

		personalMessageSender.start();
		telnetServer.start();

		Runtime.getRuntime().addShutdownHook(new Thread(Launcher::stop));
	}

	public static void stop() {
		personalMessageSender.stop();
		telnetServer.stop();
	}
}
