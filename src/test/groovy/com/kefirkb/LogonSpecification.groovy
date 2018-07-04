package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient

class LogonSpecification extends CommonSpecification {

	def "Test login simple client"() {
		setup:
		TelnetClient telnetClient = new TelnetClient()
		telnetClient.connect("localhost",23)
		BufferedReader reader = new BufferedReader(new InputStreamReader(telnetClient.inputStream))

		expect:
		reader.readLine() == "Welcome to TEST_SERVER!"

		when:
		telnetClient.getOutputStream().write(("/logon dummy dummy" + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()

		then:
		reader.readLine() == "TEST_SERVER: Invalid password or username"

		when:
		telnetClient.getOutputStream().write(("/logon dummy dummy dummy " + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()

		then:
		reader.readLine() == "TEST_SERVER: You have bad parameters."

		when:
		telnetClient.getOutputStream().write(("/logon user1 user1" + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()

		then:
		reader.readLine() == "TEST_SERVER: Logged successfully!"

		when:
		telnetClient.getOutputStream().write(("/logon user2 user2" + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()

		then:
		reader.readLine() == "TEST_SERVER: You are already logged."
	}

}
