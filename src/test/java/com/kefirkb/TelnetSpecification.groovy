package com.kefirkb

import org.apache.commons.net.telnet.TelnetClient
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TelnetSpecification extends Specification {

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

	def "Test simple client"() {
		setup:
		TelnetClient telnetClient = new TelnetClient()
		telnetClient.connect("localhost",23)
		when:
		telnetClient.getOutputStream().write(("Hello message" + System.lineSeparator()).getBytes())
		telnetClient.getOutputStream().flush()
		BufferedReader reader = new BufferedReader(new InputStreamReader(telnetClient.inputStream))
		then:
		reader.readLine() == "Welcome to snikiforov!"
	}

}
