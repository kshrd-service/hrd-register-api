package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.entity.Candidate;

public interface PdfService {

    byte[] generatePdf(Candidate candidate);

}
