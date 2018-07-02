import com.kefirkb.processors.impl.JoinChannelCommandProcessor
import com.kefirkb.processors.impl.LeftChannelCommandProcessor
import com.kefirkb.processors.impl.LogonProcessor
import com.kefirkb.services.CommandsDispatcher

beans {
	logonCommandProcessor(LogonProcessor, ref('authService'))
	joinChannelCommandProcessor(JoinChannelCommandProcessor, ref('chatChannelService'), ref('userService'), ref('messageService'))
	leftChannelCommandProcessor(LeftChannelCommandProcessor, ref('chatChannelService'), ref('userService'))

	commandProcessors(HashSet, Arrays.asList(ref('logonCommandProcessor'), ref('joinChannelCommandProcessor'), ref('leftChannelCommandProcessor')))

	commandsDispatcher(CommandsDispatcher, ref('commandProcessors'), ref('messageService'), ref('authService'))
}