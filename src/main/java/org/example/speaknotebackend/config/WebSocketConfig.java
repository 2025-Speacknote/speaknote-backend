package org.example.speaknotebackend.config;

import org.example.speaknotebackend.websocket.AnnotationWebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final AnnotationWebSocketHandler annotationWebSocketHandler;

    @Value("${custom.cors.allowed-origin}")
    private String allowedOrigin;

    public WebSocketConfig(AnnotationWebSocketHandler annotationWebSocketHandler) {
        this.annotationWebSocketHandler = annotationWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {



        registry.addHandler(annotationWebSocketHandler, "/ws/annotation")
                .setAllowedOrigins(allowedOrigin);

    }
}
