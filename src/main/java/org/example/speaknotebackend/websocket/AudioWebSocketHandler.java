package org.example.speaknotebackend.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.nio.ByteBuffer;

@Slf4j
@Component
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("클라이언트 WebSocket 연결됨: {}", session.getId());
        // TODO: Google STT 연결 예정
    }

    @Override
    protected void handleBinaryMessage(
            WebSocketSession session,
            BinaryMessage message) throws Exception {
        ByteBuffer payload = message.getPayload();
        byte[] audioBytes = new byte[payload.remaining()];
        payload.get(audioBytes);

        log.info("오디오 chunk 수신 ({} bytes)", audioBytes.length);

        //  TODO: Google STT에 byte[] 전송
    }

    public void afterConnectionClosed(
            WebSocketSession session, CloseStatus status) {
        log.info("WebSocket 연결 종료 {}", session.getId());

        // STT 종료 예정
    }
}
