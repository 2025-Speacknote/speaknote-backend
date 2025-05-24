package org.example.speaknotebackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextRefineService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * AI 정제 서버에 원본 텍스트를 전송하고, 성공 여부 판단하는 메서드.
     * @param originalText Google STT로부터 받은 원본 텍스트
     * @return 정제 요청 결과 상태 문자열: [정제 성공], [정제 실패], [정제 오류]
     */
    public String refine(String originalText) {
        String url = "http://localhost:8000/refine";

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("text", originalText), headers
        );

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) { // 200
                return "[정제 성공]";
            } else {
                log.warn("AI 서버 응답 실패: {}", response.getStatusCode()); // 400, 500 등
                return "[정제 실패]";
            }
        } catch (Exception e) { // AI 서버가 꺼져있거나 예외가 발생한 경우
            log.error("AI 서버 요청 중 오류", e);
            return "[정제 오류]";
        }
    }
}
