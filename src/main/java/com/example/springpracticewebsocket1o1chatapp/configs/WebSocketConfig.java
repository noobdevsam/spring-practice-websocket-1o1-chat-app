package com.example.springpracticewebsocket1o1chatapp.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

/**
 * Configures WebSocket for the application.
 * Enables WebSocket message brokering and sets up STOMP endpoints and a message broker.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers STOMP endpoints, allowing clients to connect to the WebSocket server.
     * The endpoint "/ws" is registered, and SockJS is enabled as a fallback option.
     *
     * @param registry the registry to register the STOMP endpoints with
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * Configures the message broker to route messages from one client to another.
     * A simple in-memory message broker is enabled with the prefix "/user".
     * Application destination prefixes are set to "/app", and the user destination prefix is set to "/user".
     * @param registry the registry to configure the message broker with
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/user");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Configures message converters for serializing and deserializing messages.
     * A Jackson message converter is configured with JSON as the default content type.
     * @param messageConverters the list of message converters to configure
     * @return false to indicate that the default converters should be used as well
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        var resolver = new DefaultContentTypeResolver();
        var converter = new MappingJackson2MessageConverter();

        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);

        messageConverters.add(converter);

        return false;
    }
}
