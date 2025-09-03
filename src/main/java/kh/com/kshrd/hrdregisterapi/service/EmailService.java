package kh.com.kshrd.hrdregisterapi.service;

import jakarta.mail.MessagingException;
import kh.com.kshrd.hrdregisterapi.model.entity.Candidate;

public interface EmailService {

    void sendApplicationForm(Candidate candidate, byte[] pdfBytes);

    void sendDonationForm(Candidate candidate, byte[] card) throws MessagingException;
}
