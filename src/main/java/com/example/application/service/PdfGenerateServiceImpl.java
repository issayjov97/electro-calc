package com.example.application.service;

import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;


@Service
public class PdfGenerateServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(PdfGenerateServiceImpl.class);

    private final TemplateEngine templateEngine;

    public PdfGenerateServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);
        try {
            String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
            FileOutputStream fileOutputStream = new FileOutputStream(outputFolder);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
        } catch (FileNotFoundException | DocumentException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public ByteArrayInputStream exportReceiptPdf(String templateName, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process(templateName, context);

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(byteArrayOutputStream, false);
            renderer.finishPDF();
            byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
        }
        return byteArrayInputStream;
    }
}
    