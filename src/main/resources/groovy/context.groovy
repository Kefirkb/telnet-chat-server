import com.kefirkb.TelnetServer
import com.kefirkb.TelnetServerHandler
import com.kefirkb.TelnetServerInitializer
import com.kefirkb.services.impl.DummyAuthService

beans {
	authService(DummyAuthService) {}

	telnetServerHandler(TelnetServerHandler, ref('authService'))

	telnetServerInitializer(TelnetServerInitializer, ref('telnetServerHandler'), null)

	telnetServer(TelnetServer, ref('telnetServerInitializer'))
}