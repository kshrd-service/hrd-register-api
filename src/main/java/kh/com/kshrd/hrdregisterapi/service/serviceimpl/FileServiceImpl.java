package kh.com.kshrd.hrdregisterapi.service.serviceimpl;

import io.minio.*;
import io.minio.messages.DeleteObject;
import kh.com.kshrd.hrdregisterapi.exception.BadRequestException;
import kh.com.kshrd.hrdregisterapi.model.entity.FileMetadata;
import kh.com.kshrd.hrdregisterapi.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${minio.bucket}")
    private String bucketName;

    private final MinioClient minioClient;

    @SneakyThrows
    @Override
    public FileMetadata uploadImage(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Only image files are allowed.");
        }

        boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String originalFileName = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String fileName = UUID.randomUUID() + (extension != null ? "." + extension : "");

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .contentType(contentType)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build()
        );

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/preview-file/" + fileName)
                .toUriString();

        return FileMetadata.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(contentType)
                .fileSize(file.getSize())
                .build();
    }

    @Override
    @SneakyThrows
    public void uploadPdf(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null ||
            !(contentType.equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE))) {
            throw new BadRequestException("Only PDF files are allowed.");
        }

        boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String originalFileName = file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(originalFileName)
                        .contentType(contentType)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build()
        );
    }


    @SneakyThrows
    @Override
    public InputStream getFileByFileName(String fileName) {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }

    @SneakyThrows
    @Override
    public void deleteImage(String fileName) {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }

}
