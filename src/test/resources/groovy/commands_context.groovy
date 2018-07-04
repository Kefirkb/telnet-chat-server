import com.kefirkb.processors.impl.JoinChannelCommandProcessor
import com.kefirkb.processors.impl.LeaveChannelCommandProcessor
import com.kefirkb.processors.impl.LogonProcessor
import com.kefirkb.processors.impl.SendMessageProcessor
import com.kefirkb.processors.impl.UserListCommandProcessor
import com.kefirkb.services.CommandsDispatcher

beans {
    logonCommandProcessor(LogonProcessor, ref('authService'), ref('messageService'))
    joinChannelCommandProcessor(JoinChannelCommandProcessor, ref('chatChannelService'), ref('userService'), ref('messageService'))
    leaveChannelCommandProcessor(LeaveChannelCommandProcessor, ref('chatChannelService'), ref('userService'), ref('messageService'))
    userListCommandProcessor(UserListCommandProcessor, ref('userService'), ref('messageService'))
    messagingProcessor(SendMessageProcessor, ref('userService'), ref('messageService'))

    commandProcessors(HashSet,
            Arrays.asList(ref('logonCommandProcessor'),
                    ref('joinChannelCommandProcessor'),
                    ref('leaveChannelCommandProcessor'),
                    ref('userListCommandProcessor'),
                    ref('messagingProcessor')
            ))

    commandsDispatcher(CommandsDispatcher, ref('commandProcessors'), ref('messageService'), ref('authService'))
}