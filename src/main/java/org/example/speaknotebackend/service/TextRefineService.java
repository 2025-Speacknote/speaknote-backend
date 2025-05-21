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

    public String refine(String originalText) {
        String url = "http://localhost:8000/refine";

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("text", originalText), headers
        );

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return (String) response.getBody().get("refinedText");
            } else {
                log.warn("AI 서버 응답 실패: {}", response.getStatusCode());
                return "[정제 실패]";
            }
        } catch (Exception e) {
            log.error("AI 서버 요청 중 오류", e);
            return "[정제 오류]";
        }
    }
}
