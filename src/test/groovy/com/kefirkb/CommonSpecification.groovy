package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient
import spock.lang.Ignore
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Ignore
class CommonSpecification extends Specification {

    def setupSpec() {
        System.setProperty("server.name", "TEST_SERVER")
    }

    def setup() {
        ExecutorService executorService = Executors.newSingleThreadExecutor()
        executorService.execute({
            Launcher.main(new String[0])
        })
        Thread.sleep(2000)
    }

    def cleanup() {
        Launcher.stop()
    }

    static void sendMessage(TelnetClient client, String message) {
        client.getOutputStream().write((message + System.lineSeparator()).getBytes())
        client.getOutputStream().flush()
    }
}
