package com.kefirkb;

import com.kefirkb.services.senders.BroadCastMessageSender;
import com.kefirkb.services.senders.PersonalMessageSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

public class Launcher {

	private static volatile TelnetServerInstance telnetServer;
	private static volatile PersonalMessageSender personalMessageSender;
	private static volatile BroadCastMessageSender broadCastMessageSender;

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new GenericGroovyApplicationContext("groovy/context.groovy");
		telnetServer = context.getBean(TelnetServerInstance.class);
		personalMessageSender = context.getBean(PersonalMessageSender.class);
		broadCastMessageSender = context.getBean(BroadCastMessageSender.class);

		personalMessageSender.start();
		broadCastMessageSender.start();
		telnetServer.start();

		Runtime.getRuntime().addShutdownHook(new Thread(Launcher::stop));
	}

	public static void stop() {
		personalMessageSender.stop();
		broadCastMessageSender.stop();
		telnetServer.stop();
	}
}
