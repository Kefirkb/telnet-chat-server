import com.kefirkb.TelnetServerInstance
import com.kefirkb.TelnetServerHandler
import com.kefirkb.TelnetServerInitializer
import com.kefirkb.services.MessageServiceImpl
import com.kefirkb.services.impl.DummyAuthService

beans {
	importBeans("classpath:groovy/delivering_context.groovy")
	importBeans("classpath:groovy/commands_context.groovy")

	authService(DummyAuthService) {}

	messageService(MessageServiceImpl) {}

	telnetServerHandler(TelnetServerHandler, ref('authService'), ref('commandsDispatcher'))

	telnetServerInitializer(TelnetServerInitializer, ref('telnetServerHandler'), null)

	telnetServer(TelnetServerInstance, ref('telnetServerInitializer'))
}