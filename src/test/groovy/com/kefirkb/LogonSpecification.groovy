package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LogonSpecification extends Specification {

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

	def "Test login simple client"() {
		setup:
		TelnetClient telnetClient = new TelnetClient()
		telnetClient.connect("localhost",23)
		BufferedReader reader = new BufferedReader(new InputStreamReader(telnetClient.inputStream))

		expect:
		reader.readLine() == "Welcome to DUMMY_SERVER!"

		when:
		telnetClient.getOutputStream().write(("/logon dummy dummy" + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()

		then:
		reader.readLine() == "DUMMY_SERVER: Invalid password or username"

		when:
		telnetClient.getOutputStream().write(("/logon dummy dummy dummy " + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()

		then:
		reader.readLine() == "DUMMY_SERVER: You have bad parameters."

		when:
		telnetClient.getOutputStream().write(("/logon user1 user1" + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()

		then:
		reader.readLine() == "DUMMY_SERVER: Logged successfully!"

		when:
		telnetClient.getOutputStream().write(("/logon user2 user2" + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()

		then:
		reader.readLine() == "DUMMY_SERVER: You are already logged."

	}

}
