package org.example.speaknotebackend.config;

import org.example.speaknotebackend.websocket.AnnotationWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final AnnotationWebSocketHandler annotationWebSocketHandler;

    public WebSocketConfig(AnnotationWebSocketHandler annotationWebSocketHandler) {
        this.annotationWebSocketHandler = annotationWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(annotationWebSocketHandler, "/ws/annotation")
                .setAllowedOrigins("*"); // 필요시 CORS 설정
    }
}
