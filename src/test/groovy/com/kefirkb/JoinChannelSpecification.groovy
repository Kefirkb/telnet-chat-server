package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class JoinChannelSpecification extends Specification {

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

	def "Test join to channel "() {
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
		sendMessage(telnetClient1, "/join channelSome")

		then:
		reader1.readLine() == "DUMMY_SERVER: You should be authorized!"

		when:
        sendMessage(telnetClient1, "/logon user1 user1")
		reader1.readLine()// == "DUMMY_SERVER: Logged successfully!"
        sendMessage(telnetClient1, "/join channelSome" )

		then:
		reader1.readLine() == "DUMMY_SERVER: Channel was created: channelSome"
		reader1.readLine() == "user1: joined to channelSome"

		when:
        sendMessage(telnetClient1, "/join channelSome")

		then:
		reader1.readLine() == "DUMMY_SERVER: user1 is already in channelSome"

		when:
        sendMessage(telnetClient2, "/logon user2 user2")
		reader2.readLine()// == "DUMMY_SERVER: Logged successfully!"
        sendMessage(telnetClient2, "/join channelSome" )

		then:
		reader2.readLine() == "user2: joined to channelSome"
		//reader2.readLine() == "DUMMY_SERVER: You joined to channelSome"
		reader1.readLine() == "user2: joined to channelSome"

		when:
		sendMessage(telnetClient3, "/logon user3 user3" )
		reader3.readLine()// == "DUMMY_SERVER: Logged successfully!"
		sendMessage(telnetClient3, "/join channelSome")

		then:
		reader3.readLine() == "user3: joined to channelSome"
		reader2.readLine() == "user3: joined to channelSome"
		reader1.readLine() == "user3: joined to channelSome"

		when:
		sendMessage(telnetClient1, "/join newChannel")
		then:
		reader2.readLine() == "user1: left channelSome"
		reader3.readLine() == "user1: left channelSome"

	}

	static void sendMessage(TelnetClient client, String message) {
		client.getOutputStream().write((message + System.lineSeparator()).getBytes())
		client.getOutputStream().flush()
	}

}
