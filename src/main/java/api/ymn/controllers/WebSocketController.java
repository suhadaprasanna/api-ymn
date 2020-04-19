/**
 * 
 */
package api.ymn.controllers;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import api.ymn.common.form.TestForm;

/**
 * @author suhada
 *
 */
@Controller
public class WebSocketController {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

	private SimpMessagingTemplate template;

	@Inject
	  public WebSocketController(SimpMessagingTemplate template) {
	    this.template = template;
	  }

	@MessageMapping("/chat")
	@SendTo("/ymn/topic/messages")
	public Object greeting(@Payload TestForm form, Message<Object> message,SimpMessageHeaderAccessor header){
		try {
			logger.warn("------>>Start greeting<<------");
			form.setDate(new Date());
			header.getSessionAttributes().put("username", form.getFrom());
			logger.debug("form: " + form.toString());
			logger.debug("message: " + message.getHeaders());
			logger.debug("------>>End greeting<<------");
			return form;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return "{ERROR:ERROR}";
		}
	}

}
