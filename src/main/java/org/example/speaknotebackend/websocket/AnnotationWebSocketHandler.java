package org.example.speaknotebackend.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.speaknotebackend.service.AnnotationService;
import org.example.speaknotebackend.service.GptService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnotationWebSocketHandler extends TextWebSocketHandler {

    private final GptService gptService;
    private final AnnotationService annotationService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info(" WebSocket 연결됨: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String originalText = message.getPayload();
        String sessionId = session.getId();

        log.info("수신 메시지: {}", originalText);

        gptService.refineTextAsync(originalText)
                .thenAccept(refinedText -> {
                    try {
                        // MongoDB 저장
                        annotationService.saveAnnotation(sessionId, originalText, refinedText);
                        log.info("MongoDB 저장 완료");

                        // 클라이언트에 전송
                        session.sendMessage(new TextMessage(refinedText));
                        log.info("GPT 결과 전송 완료: {}", refinedText);

                    } catch (Exception e) {
                        log.error("WebSocket 처리 중 오류 발생", e);
                    }
                });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("WebSocket 연결 종료: {}", session.getId());
    }
}
