package org.example.speaknotebackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.speaknotebackend.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;

    @PostMapping
    public ResponseEntity<byte[]> downloadPdf() throws Exception {
        byte[] pdfBytes = pdfService.generatePdfWithAnnotations();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=annotations.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
