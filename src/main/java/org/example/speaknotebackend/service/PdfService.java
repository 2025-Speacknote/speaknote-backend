package org.example.speaknotebackend.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import lombok.RequiredArgsConstructor;
import org.example.speaknotebackend.domain.entity.AnnotationBlock;
import org.example.speaknotebackend.domain.repository.AnnotationRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final AnnotationRepository annotationRepository;

    public byte[] generatePdfWithAnnotations() throws Exception {
        List<AnnotationBlock> annotations = annotationRepository.findAll();

        // 페이지 수 계산
        int maxPage = annotations.stream()
                .mapToInt(AnnotationBlock::getPageNumber)
                .max()
                .orElse(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, PageSize.A4);

        // 페이지 먼저 생성
        for (int i = 0; i < maxPage; i++) {
            pdfDoc.addNewPage();
        }

        for (AnnotationBlock block : annotations) {
            PdfPage page = pdfDoc.getPage(block.getPageNumber());
            PdfCanvas canvas = new PdfCanvas(page);

            // 텍스트 스타일
            canvas.beginText();
            canvas.setFontAndSize(PdfFontFactory.createFont(), 10);
            canvas.setFillColor(ColorConstants.BLACK);

            // 좌표 기반 주석 출력
            canvas.moveText(block.getX(), block.getY());
            canvas.showText(block.getRefinedText());
            canvas.endText();
        }

        doc.close();
        return out.toByteArray();
    }
}
