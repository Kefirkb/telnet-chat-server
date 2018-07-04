package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient

class LeftChannelSpecification extends CommonSpecification {

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
		reader1.readLine() == "Welcome to TEST_SERVER!"
		reader2.readLine() == "Welcome to TEST_SERVER!"
		reader3.readLine() == "Welcome to TEST_SERVER!"

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

}
