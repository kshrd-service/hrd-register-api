package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kh.com.kshrd.hrdregisterapi.exception.BadRequestException;
import kh.com.kshrd.hrdregisterapi.model.entity.Candidate;
import kh.com.kshrd.hrdregisterapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @SneakyThrows
    @Override
    public void sendApplicationForm(Candidate candidate, byte[] pdfBytes) {
        Context context = new Context();
        context.setVariable("candidate", candidate);

        String process = templateEngine.process("application-form/index", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setSubject("Application Form for "
                                     + candidate.getGeneration().getGeneration() + " Entrance Exam");
        mimeMessageHelper.setText(process, true);
        mimeMessageHelper.setTo(candidate.getEmail());

        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new BadRequestException("Could not fetch PDF");
        }

        String attachmentName = candidate.getFullName() + ".pdf";
        mimeMessageHelper.addAttachment(attachmentName, new ByteArrayResource(pdfBytes), MediaType.APPLICATION_PDF_VALUE);

        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendDonationForm(Candidate candidate, byte[] card) throws MessagingException {
        Context context = new Context();
        context.setVariable("candidate", candidate);

        String process = templateEngine.process("qr-template/index", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setSubject("Donation Form for "
                                     + candidate.getGeneration().getGeneration() + " Entrance Exam");
        mimeMessageHelper.setText(process, true);
        mimeMessageHelper.setTo(candidate.getEmail());

        if (card == null || card.length == 0) {
            throw new BadRequestException("Could not fetch png");
        }

        String attachmentName = candidate.getFullName() + ".png";

        ByteArrayResource qrArrayResource = new ByteArrayResource(card);

        ClassPathResource kshrdResource = new ClassPathResource("static/images/KSHRD-Logo.png");

        mimeMessageHelper.addInline("hrd-logo", kshrdResource, "image/png");
        mimeMessageHelper.addInline("qrcode-logo", qrArrayResource, "image/png");

        mimeMessageHelper.addAttachment(attachmentName, qrArrayResource, MediaType.IMAGE_PNG_VALUE);

        javaMailSender.send(mimeMessage);
    }


}
