package com.kefirkb;

import com.kefirkb.services.MessageQueuesHolder;
import com.kefirkb.services.senders.BroadCastMessageSender;
import com.kefirkb.services.senders.PersonalMessageSender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericGroovyApplicationContext;

import java.util.Objects;

import static com.kefirkb.registries.CommonRegistry.SERVER_NAME_PROPERTY;

public class Launcher {

    private static volatile TelnetServerInstance telnetServer;
    private static volatile PersonalMessageSender personalMessageSender;
    private static volatile BroadCastMessageSender broadCastMessageSender;

    public static void main(String[] args) throws Exception {
        validateSystemProperties();
        ApplicationContext context = new GenericGroovyApplicationContext("groovy/context.groovy");
        telnetServer = context.getBean(TelnetServerInstance.class);
        personalMessageSender = context.getBean(PersonalMessageSender.class);
        broadCastMessageSender = context.getBean(BroadCastMessageSender.class);

        personalMessageSender.start();
        broadCastMessageSender.start();
        telnetServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(Launcher::stop));
    }

    private static void validateSystemProperties() {
        Objects.requireNonNull(System.getProperty(SERVER_NAME_PROPERTY), SERVER_NAME_PROPERTY + " must be specified");
    }

    public static void stop() {
        MessageQueuesHolder.clearQueues();
        telnetServer.stop();
        personalMessageSender.stop();
        broadCastMessageSender.stop();
    }
}
