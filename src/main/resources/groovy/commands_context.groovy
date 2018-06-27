import com.kefirkb.processors.impl.JoinChannelCommandProcessor
import com.kefirkb.processors.impl.LogonProcessor
import com.kefirkb.services.CommandsDispatcher

beans {
	logonCommandProcessor(LogonProcessor, ref('authService'))
	joinChannelCommandProcessor(JoinChannelCommandProcessor, ref('chatChannelService'), ref('userService'))

	commandProcessors(HashSet, Arrays.asList(ref('logonCommandProcessor'), ref('joinChannelCommandProcessor')))

	commandsDispatcher(CommandsDispatcher, ref('commandProcessors'), ref('messageService'), ref('authService'))
}