import com.kefirkb.TelnetServer
import com.kefirkb.TelnetServerHandler
import com.kefirkb.TelnetServerInitializer

beans {
	telnetServerHandler(TelnetServerHandler) {}

	telnetServerInitializer(TelnetServerInitializer, ref('telnetServerHandler'), null)

	telnetServer(TelnetServer, ref('telnetServerInitializer'))
}