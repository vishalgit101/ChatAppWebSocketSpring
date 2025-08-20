package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import enums.MessageType;
import lombok.RequiredArgsConstructor;
import model.ChatMessage;

@Component
@RequiredArgsConstructor
// can also add lombok logger
public class WebSocketEventListener {
	
	private final SimpMessageSendingOperations messageTemplate;

	@Autowired
	public WebSocketEventListener(SimpMessageSendingOperations messageTemplate) {
		super();
		this.messageTemplate = messageTemplate;
	}

	// will log information like user leaving the chat
	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		
		if(username != null) {
			logger.info("User disconnected: {}", username);
			ChatMessage chatMessage = new ChatMessage();
			
			chatMessage.setType(MessageType.LEAVE);
			chatMessage.setSender(username);
			
			messageTemplate.convertAndSend("/topic/public", chatMessage);
			// this is the topic of the channel or the queue that everyone or every user is listening on
			// chat message will info all the users that this user has left the chat
		}
	}
	
}
