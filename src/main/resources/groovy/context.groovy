import com.kefirkb.TelnetServerInstance
import com.kefirkb.TelnetRequestHandler
import com.kefirkb.TelnetServerInitializer
import com.kefirkb.services.MessageServiceImpl
import com.kefirkb.services.impl.DummyAuthService
import com.kefirkb.services.impl.DummyChatChannelService
import com.kefirkb.services.impl.DummyUserService

beans {
	importBeans("classpath:groovy/delivering_context.groovy")
	importBeans("classpath:groovy/commands_context.groovy")

	authService(DummyAuthService) {}
	userService(DummyUserService) {}
	chatChannelService(DummyChatChannelService) {}

	messageService(MessageServiceImpl) {}

	telnetServerHandler(TelnetRequestHandler, ref('authService'), ref('commandsDispatcher'))

	telnetServerInitializer(TelnetServerInitializer, ref('telnetServerHandler'), null)

	telnetServer(TelnetServerInstance, ref('telnetServerInitializer'))
}