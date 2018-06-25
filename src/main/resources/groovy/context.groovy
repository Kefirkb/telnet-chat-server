import com.kefirkb.TelnetServer
import com.kefirkb.TelnetServerHandler
import com.kefirkb.TelnetServerInitializer
import com.kefirkb.processors.CommandProcessor
import com.kefirkb.processors.impl.LogonProcessor
import com.kefirkb.services.impl.DummyAuthService

beans {
	authService(DummyAuthService) {}

	logonCommandProcessor(LogonProcessor, ref('authService'))

	commandProcessors(HashSet, Arrays.asList(ref('logonCommandProcessor')))

	commandsDispatcher(CommandProcessor, ref('commandProcessors'))

	telnetServerHandler(TelnetServerHandler, ref('authService'), ref('commandsDispatcher'))

	telnetServerInitializer(TelnetServerInitializer, ref('telnetServerHandler'), null)

	telnetServer(TelnetServer, ref('telnetServerInitializer'))
}