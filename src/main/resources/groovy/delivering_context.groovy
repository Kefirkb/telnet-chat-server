import com.kefirkb.services.senders.BroadCastMessageSender
import com.kefirkb.services.senders.PersonalMessageSender

beans {
	personalMessageSender(PersonalMessageSender, 5)
	broadCastMessageSender(BroadCastMessageSender, 5)
}