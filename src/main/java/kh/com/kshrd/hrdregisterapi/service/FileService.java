package kh.com.kshrd.hrdregisterapi.service;

import kh.com.kshrd.hrdregisterapi.model.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.UUID;

public interface FileService {

    FileMetadata uploadImage(MultipartFile file);

    void uploadPdf(MultipartFile file);

    InputStream getFileByFileName(String fileName);

    void deleteImage(String fileName);
}
