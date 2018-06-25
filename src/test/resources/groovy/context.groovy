import com.kefirkb.TelnetServer
import com.kefirkb.TelnetServerHandler
import com.kefirkb.TelnetServerInitializer
import com.kefirkb.processors.CommandProcessor
import com.kefirkb.processors.impl.LogonProcessor
import com.kefirkb.services.MessageServiceImpl
import com.kefirkb.services.impl.DummyAuthService

beans {
	importBeans("classpath:groovy/delivering_context.groovy")
	importBeans("classpath:groovy/commands_context.groovy")

	authService(DummyAuthService) {}

	messageService(MessageServiceImpl) {}

	telnetServerHandler(TelnetServerHandler, ref('authService'), ref('commandsDispatcher'))

	telnetServerInitializer(TelnetServerInitializer, ref('telnetServerHandler'), null)

	telnetServer(TelnetServer, ref('telnetServerInitializer'))
}