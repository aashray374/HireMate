package com.squirtle.hiremate.common.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileParser {

    private String extractTextFromPdf(byte[] file){
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse PDF", e);
        }
    }

    @Bean
    public String getCleanedText(byte[] file){
        String text = extractTextFromPdf(file);
        return text
                .replaceAll("\\s+", " ")
                .replaceAll("[^\\x00-\\x7F]", "")
                .trim();
    }
}
