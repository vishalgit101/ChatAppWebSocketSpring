package controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import model.ChatMessage;

@Controller
public class ChatController { // Message Broker Class
	
	
	@MessageMapping("/chat.sendMessage") // this mapping tells what is the url that want to use to invoke this send message method
	@SendTo("/topic/public") // means to which topic or queue we want to send this one
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) { // ChatMessage is a body class that will map the payload with our backend class
		// but in websockets we use @payload instead of @RequestBody
		chatMessage.setContent(chatMessage.getContent() + "F");
		return chatMessage;
	}
	
	// now each time we get a message and since its a payload it will be sent automatically to /topic/public 
	
	// This is send message method, we can use it with this endpoint /chat.sendMessage and then each time we receive a message/payload
	// it will be queued to /topic/public
	
	
	
	// Second Method
	@MessageMapping("/chat.addUser") // incase a new user joins
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		// this will allow us to established a connection between the user and the websocket
		
		// Add username in web socket session
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}
	
}
