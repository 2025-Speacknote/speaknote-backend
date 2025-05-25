package org.example.speaknotebackend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    private final TextRefineService textRefineService;
    private final GoogleSpeechService googleSpeechService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("클라이언트 WebSocket 연결됨: {}", session.getId());

        // STT 스트리밍 시작 및 콜백 등록
        googleSpeechService.startStreaming(originalText -> {
            log.info("인식된 텍스트: {}", originalText);

            // AI 서버에 인식한 텍스트 전송
            // AI 서버 켜고 활성화하면 됨

            //
            CompletableFuture.runAsync(() -> {
                try {
                    Map<String, Object> aiResponse = textRefineService.refine(originalText);
                    log.info("AI 서버에 original text 전송 결과: {}", originalText);
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("refinedText", aiResponse.get("refinedText"));
                    payload.put("refinedMarkdown", aiResponse.get("refinedMarkdown"));

                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(payload);
                    session.sendMessage(new TextMessage(json));

                    log.info("정제된 결과 WebSocket 전송 완료");
                    log.info("AI 응답 내용: refinedText={}, refinedMarkdown={}",
                            aiResponse.get("refinedText"), aiResponse.get("refinedMarkdown"));

                } catch (Exception e) {
                    log.error("AI 정제 및 전송 중 오류", e);
                }
            });
        });
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
