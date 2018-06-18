package com.kefirkb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

public class Launcher {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new GenericGroovyApplicationContext("groovy/context.groovy");
		TelnetServer telnetServer = context.getBean(TelnetServer.class);
		telnetServer.start();
	}
}
