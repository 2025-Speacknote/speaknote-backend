package org.example.speaknotebackend.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.speaknotebackend.service.GoogleSpeechService;
import org.example.speaknotebackend.service.TextRefineService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    private final TextRefineService textRefineService;
    private final GoogleSpeechService googleSpeechService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("클라이언트 WebSocket 연결됨: {}", session.getId());

        // STT 스트리밍 시작
        googleSpeechService.startStreaming();
    }

    @Override
    protected void handleBinaryMessage(
            WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer payload = message.getPayload();
        byte[] audioBytes = new byte[payload.remaining()];
        payload.get(audioBytes);

//        log.info("오디오 chunk 수신 ({} bytes)", audioBytes.length);

        // Google STT로 오디오 전송
        googleSpeechService.sendAudioChunk(audioBytes);
    }

    // TODO 사용자가 녹음 중지 누르면 종료 해야 함
    public void afterConnectionClosed(
            WebSocketSession session, CloseStatus status) {
        log.info("WebSocket 연결 종료 {}", session.getId());

        // STT 종료
        googleSpeechService.stopStreaming();
    }
}
