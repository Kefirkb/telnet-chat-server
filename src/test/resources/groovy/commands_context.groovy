import com.kefirkb.processors.impl.LogonProcessor
import com.kefirkb.services.CommandsDispatcher

beans {
	logonCommandProcessor(LogonProcessor, ref('authService'))

	commandProcessors(HashSet, Arrays.asList(ref('logonCommandProcessor')))

	commandsDispatcher(CommandsDispatcher, ref('commandProcessors'), ref('messageService'))
}