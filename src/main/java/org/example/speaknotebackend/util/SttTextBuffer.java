package org.example.speaknotebackend.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SttTextBuffer {

    private final StringBuilder context = new StringBuilder();
    private String lastText = "";

    /**
     * 중복되거나 누적된 텍스트를 제거하고 새로 추가된 부분만 context에 누적하여 반환합니다.
     * 완전히 중복된 경우 null 반환.
     */
    public String appendAndGetNewContent(String newText) {
        if (newText == null || newText.isBlank()) {
            return null;
        }

        // 완전히 같으면 중복
        if (newText.equals(lastText)) {
            return null;
        }

        // 이전 텍스트가 새 텍스트의 접두사라면 나머지만 누적
        if (newText.startsWith(lastText)) {
            String delta = newText.substring(lastText.length());
            context.append(delta);
            lastText = newText;
            return delta;
        }

        // 가장 긴 접미사-접두사 중복 구간 찾아 제거
        String suffix = getSuffixWithoutOverlap(lastText, newText);
        if (!suffix.isEmpty()) {
            context.append(suffix);
            lastText = newText;
            return suffix;
        }

        // 겹치는 부분이 전혀 없으면 전체 추가
        context.append(newText);
        lastText = newText;
        return newText;
    }

    /**
     * 이전 텍스트와 현재 텍스트 사이에서 가장 많이 겹치는 접미사-접두사 구간을 찾아 해당 부분을 제거한 후 반환
     */
    private String getSuffixWithoutOverlap(String prev, String current) {
        int maxOverlap = 0;
        int min = Math.min(prev.length(), current.length());

        for (int i = 1; i <= min; i++) {
            String suffix = prev.substring(prev.length() - i);
            String prefix = current.substring(0, i);
            if (suffix.equals(prefix)) {
                maxOverlap = i;
            }
        }

        return current.substring(maxOverlap);
    }

    public String getAccumulatedContext() {
        return context.toString();
    }

    public void clear() {
        context.setLength(0);
        lastText = "";
    }
}
