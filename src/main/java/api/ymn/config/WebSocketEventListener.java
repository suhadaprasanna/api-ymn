/**
 * 
 */
package api.ymn.config;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


/**
 * @author suhada
 *
 */
@Component
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		logger.debug("------>> start handleWebSocketConnectListener <<------");
		logger.debug("------>> end handleWebSocketConnectListener <<------");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		logger.debug("------>> start handleWebSocketDisconnectListener <<------");
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if (username != null) {
			HashMap<String, String> chatMessage = new HashMap();
			chatMessage.put("type","Leave");
			chatMessage.put("sender",username);
			messagingTemplate.convertAndSend("/topic/subject", chatMessage);
		}
		logger.debug("------>> end handleWebSocketDisconnectListener <<------");
	}

}
