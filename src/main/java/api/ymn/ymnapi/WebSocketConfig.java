/**
 * 
 */
package api.ymn.ymnapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.ReactorNettyTcpStompClient;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import api.ymn.controllers.WebSocketController;
import io.netty.bootstrap.Bootstrap;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

/**
 * @author suhada
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
	
	@Autowired
	Environment env;
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	config.setApplicationDestinationPrefixes("/ymn/cross");
    	try {
    		
//    		 ReactorNettyTcpClient<byte[]> client = new ReactorNettyTcpClient<>(tcpClient -> tcpClient
//    	                .host(env.getProperty("gdc.msgbroker.rabbitmq.host"))
//    	                .port(Integer.parseInt(env.getProperty("gdc.msgbroker.rabbitmq.port")))
//    	                //.secure(SslProvider.defaultClientProvider())
//    	                , new StompReactorNettyCodec());
    		
	    	config.enableStompBrokerRelay("/ymn/topic")
	    	.setAutoStartup(true)
	    	.setRelayHost(env.getProperty("gdc.msgbroker.rabbitmq.host"))
	    	.setRelayPort(Integer.parseInt(env.getProperty("gdc.msgbroker.rabbitmq.port")))
	    	.setClientLogin(env.getProperty("gdc.msgbroker.rabbitmq.username"))
			.setClientPasscode(env.getProperty("gdc.msgbroker.rabbitmq.password"));
			//.setTcpClient(client);
	    	
    	}catch(Exception e) {
    		logger.error("------>>ERROR: "+e);
    		config.enableSimpleBroker("/ymn/topic");
    	}
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ymn/bridge").setAllowedOrigins("*").withSockJS();
    }

}