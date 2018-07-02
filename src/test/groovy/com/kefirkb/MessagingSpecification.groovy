package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MessagingSpecification extends Specification {

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

	def "Test messaging "() {
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

		TelnetClient telnetClient4 = new TelnetClient()
		telnetClient4.connect("localhost",23)
		BufferedReader reader4 = new BufferedReader(new InputStreamReader(telnetClient4.inputStream))

		TelnetClient telnetClient5 = new TelnetClient()
		telnetClient5.connect("localhost",23)
		BufferedReader reader5 = new BufferedReader(new InputStreamReader(telnetClient5.inputStream))

		TelnetClient telnetClient6 = new TelnetClient()
		telnetClient6.connect("localhost",23)
		BufferedReader reader6 = new BufferedReader(new InputStreamReader(telnetClient6.inputStream))

		expect:
		reader1.readLine() == "Welcome to DUMMY_SERVER!"
		reader2.readLine() == "Welcome to DUMMY_SERVER!"
		reader3.readLine() == "Welcome to DUMMY_SERVER!"
		reader4.readLine() == "Welcome to DUMMY_SERVER!"
		reader5.readLine() == "Welcome to DUMMY_SERVER!"
		reader6.readLine() == "Welcome to DUMMY_SERVER!"


		when:
        sendMessage(telnetClient1, "/logon user1 user1")
		reader1.readLine()// == "DUMMY_SERVER: Logged successfully!"
        sendMessage(telnetClient1, "/join channelSome" )
		reader1.readLine()// == "DUMMY_SERVER: Channel was created: channelSome"
		reader1.readLine()// == "user1: joined to channelSome"

		sendMessage(telnetClient4, "/logon user4 user4")
		reader4.readLine()// == "DUMMY_SERVER: Logged successfully!"
		sendMessage(telnetClient4, "/join channelSome1" )
		reader4.readLine()// == "DUMMY_SERVER: Channel was created: channelSome"
		reader4.readLine()// == "user1: joined to channelSome"

        sendMessage(telnetClient2, "/logon user2 user2")
		reader2.readLine()// == "DUMMY_SERVER: Logged successfully!"
		sendMessage(telnetClient3, "/logon user3 user3")
		reader3.readLine()// == "DUMMY_SERVER: Logged successfully!"
		sendMessage(telnetClient5, "/logon user5 user5")
		reader5.readLine()// == "DUMMY_SERVER: Logged successfully!"
		sendMessage(telnetClient6, "/logon user6 user6")
		reader6.readLine()// == "DUMMY_SERVER: Logged successfully!"

        sendMessage(telnetClient2, "/join channelSome" )
		reader2.readLine()// == "user2: joined to channelSome"
		reader1.readLine()// == "user2: joined to channelSome"
		sendMessage(telnetClient3, "/join channelSome" )
		reader2.readLine()// == "user2: joined to channelSome"
		reader1.readLine()// == "user2: joined to channelSome"
		reader3.readLine()// == "user2: joined to channelSome"

		sendMessage(telnetClient5, "/join channelSome1" )
		reader5.readLine()// == "user2: joined to channelSome1"
		reader4.readLine()// == "user2: joined to channelSome1"
		sendMessage(telnetClient6, "/join channelSome1" )
		reader4.readLine()// == "user2: joined to channelSome1"
		reader5.readLine()// == "user2: joined to channelSome1"
		reader6.readLine()// == "user2: joined to channelSome1"

		sendMessage(telnetClient2, "Hello channel some")
        sendMessage(telnetClient6, "Hello channel some 1")

		then:
		reader1.readLine() == "user2: Hello channel some"
        reader2.readLine() == "user2: Hello channel some"
        reader3.readLine() == "user2: Hello channel some"

        reader4.readLine() == "user6: Hello channel some 1"
        reader5.readLine() == "user6: Hello channel some 1"
        reader6.readLine() == "user6: Hello channel some 1"
	}

	static void sendMessage(TelnetClient client, String message) {
		client.getOutputStream().write((message + System.lineSeparator()).getBytes())
		client.getOutputStream().flush()
	}

}
