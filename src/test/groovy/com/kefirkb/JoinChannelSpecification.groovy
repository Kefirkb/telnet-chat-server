package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class JoinChannelSpecification extends Specification {

	def setup() {

	}

	def cleanup() {

	}

	def setupSpec() {
		ExecutorService executorService = Executors.newSingleThreadExecutor()
		executorService.execute({
			Launcher.main(new String[0])
		})
		Thread.sleep(5000)
	}

	def cleanupSpec() {
		Launcher.stop()
	}

	def "Test join to channel "() {
		setup:
		TelnetClient telnetClient1 = new TelnetClient()
		telnetClient1.connect("localhost",23)
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(telnetClient1.inputStream))

		TelnetClient telnetClient2 = new TelnetClient()
		telnetClient2.connect("localhost",23)
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(telnetClient2.inputStream))

		expect:
		reader1.readLine() == "Welcome to DUMMY_SERVER!"
		reader2.readLine() == "Welcome to DUMMY_SERVER!"

		when:
		telnetClient1.getOutputStream().write(("/join channelSome" + System.lineSeparator()).getBytes())
		telnetClient1.getOutputStream().flush()

		then:
		reader1.readLine() == "DUMMY_SERVER: You should be authorized!"

		when:
		telnetClient1.getOutputStream().write(("/logon user1 user1" + System.lineSeparator()).getBytes())
		telnetClient1.getOutputStream().flush()
		reader1.readLine()// == "DUMMY_SERVER: Logged successfully!"
		telnetClient1.getOutputStream().write(("/join channelSome" + System.lineSeparator()).getBytes())
		telnetClient1.getOutputStream().flush()

		then:
		reader1.readLine() == "DUMMY_SERVER: Channel was created: channelSome"

		when:
		telnetClient1.getOutputStream().write(("/join channelSome" + System.lineSeparator()).getBytes())
		telnetClient1.getOutputStream().flush()

		then:
		reader1.readLine() == "DUMMY_SERVER: user1 is already in channelSome"


		when:
		telnetClient2.getOutputStream().write(("/logon user2 user2" + System.lineSeparator()).getBytes())
		telnetClient2.getOutputStream().flush()
		reader2.readLine()// == "DUMMY_SERVER: Logged successfully!"
		telnetClient2.getOutputStream().write(("/join channelSome" + System.lineSeparator()).getBytes())
		telnetClient2.getOutputStream().flush()

		then:
		reader2.readLine() == "DUMMY_SERVER: user2 joined to channelSome"

		when:
		telnetClient2.getOutputStream().write(("/left" + System.lineSeparator()).getBytes())
		telnetClient2.getOutputStream().flush()

		then:
		reader2.readLine() == "DUMMY_SERVER: You have left channelSome"

		when:
		telnetClient2.getOutputStream().write(("/left" + System.lineSeparator()).getBytes())
		telnetClient2.getOutputStream().flush()

		then:
		reader2.readLine() == "DUMMY_SERVER: You are not in any channel!"

		when:
		telnetClient2.getOutputStream().write(("/join channelSome" + System.lineSeparator()).getBytes())
		telnetClient2.getOutputStream().flush()

		then:
		reader2.readLine() == "DUMMY_SERVER: user2 joined to channelSome"

	}

}
