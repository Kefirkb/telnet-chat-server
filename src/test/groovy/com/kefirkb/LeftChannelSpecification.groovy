package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LeftChannelSpecification extends Specification {

	def setup() {
		ExecutorService executorService = Executors.newSingleThreadExecutor()
		executorService.execute({
			Launcher.main(new String[0])
		})
		Thread.sleep(4000)
	}

	def cleanup() {
		Launcher.stop()
		Thread.sleep(2000)
	}

	def "Test left channel "() {
		setup:
		TelnetClient telnetClient1 = new TelnetClient()
		telnetClient1.connect("localhost",23)
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(telnetClient1.inputStream))

		TelnetClient telnetClient2 = new TelnetClient()
		telnetClient2.connect("localhost",23)
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(telnetClient2.inputStream))

		TelnetClient telnetClient3 = new TelnetClient()
		telnetClient3.connect("localhost",23)
		BufferedReader reader3 = new BufferedReader(new InputStreamReader(telnetClient3.inputStream))

		expect:
		reader1.readLine() == "Welcome to DUMMY_SERVER!"
		reader2.readLine() == "Welcome to DUMMY_SERVER!"
		reader3.readLine() == "Welcome to DUMMY_SERVER!"

		when:
		telnetClient1.getOutputStream().write(("/logon user1 user1" + System.lineSeparator()).getBytes())
		telnetClient1.getOutputStream().flush()
		reader1.readLine()
		telnetClient1.getOutputStream().write(("/join channelSome" + System.lineSeparator()).getBytes())
		telnetClient1.getOutputStream().flush()
		reader1.readLine()
		reader1.readLine()

		sendMessage(telnetClient2, "/logon user2 user2")
		reader2.readLine()
		sendMessage(telnetClient2, "/join channelSome")
		reader2.readLine()
		reader1.readLine()
		sendMessage(telnetClient2, "/leave")

		then:
		reader1.readLine() == "user2: left channelSome"

	}

	static void sendMessage(TelnetClient client, String message) {
		client.getOutputStream().write((message + System.lineSeparator()).getBytes())
		client.getOutputStream().flush()
	}

}
