package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import kh.com.kshrd.hrdregisterapi.exception.BadRequestException;
import kh.com.kshrd.hrdregisterapi.model.entity.Candidate;
import kh.com.kshrd.hrdregisterapi.model.entity.InMemoryMultipartFile;
import kh.com.kshrd.hrdregisterapi.service.FileService;
import kh.com.kshrd.hrdregisterapi.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;


@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final TemplateEngine templateEngine;
    private final FileService fileService;

    @Override
    public byte[] generatePdf(Candidate candidate) {
        Context ctx = new Context();
        ctx.setVariable("candidate", candidate);

        String html = templateEngine.process("pdf-template/index", ctx);

        ITextRenderer renderer = new ITextRenderer();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos);

            byte[] pdfBytes = baos.toByteArray();

            MultipartFile multipartFile = new InMemoryMultipartFile(
                    "file",
                    candidate.getCandidateId() + ".pdf",
                    "application/pdf",
                    pdfBytes
            );

            fileService.uploadPdf(multipartFile);

            return pdfBytes;

        } catch (Exception e) {
            throw new BadRequestException("Failed generating PDF");
        }
    }
}
