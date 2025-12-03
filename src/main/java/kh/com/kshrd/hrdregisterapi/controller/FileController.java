package kh.com.kshrd.hrdregisterapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kh.com.kshrd.hrdregisterapi.model.dto.response.APIResponse;
import kh.com.kshrd.hrdregisterapi.model.dto.response.CandidateResponseAdmin;
import kh.com.kshrd.hrdregisterapi.model.entity.FileMetadata;
import kh.com.kshrd.hrdregisterapi.service.CandidateService;
import kh.com.kshrd.hrdregisterapi.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static kh.com.kshrd.hrdregisterapi.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final CandidateService candidateService;

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a image",
            description = "Accepts a multipart file and stores it",
            tags = {"File"}
    )
    public ResponseEntity<APIResponse<FileMetadata>> uploadImage(@RequestParam MultipartFile file) {
        return buildResponse(
                "Image uploaded successfully",
                fileService.uploadImage(file),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(value = "/delete-image/{file-name}")
    @Operation(
            summary = "",
            description = "",
            tags = {"File"}
    )
    public ResponseEntity<APIResponse<Void>> deleteImage(@PathVariable("file-name") String fileName) {
        fileService.deleteImage(fileName);
        return buildResponse(
                "",
                null,
                HttpStatus.OK
        );
    }

    @GetMapping("/preview-file/{file-name}")
    @Operation(
            summary = "Preview file (PDF or Image)",
            description = "Streams PDF or image inline based on detected content type",
            tags = {"File"}
    )
    public ResponseEntity<byte[]> getFileByFileName(@PathVariable("file-name") String fileName) throws IOException {
        try (InputStream inputStream = fileService.getFileByFileName(fileName)) {
            byte[] bytes = inputStream.readAllBytes();

            String contentType = URLConnection.guessContentTypeFromName(fileName);

            if (contentType == null) {
                contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(bytes));
            }

            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));

            headers.setContentDisposition(
                    ContentDisposition.inline()
                            .filename(fileName, StandardCharsets.UTF_8)
                            .build()
            );

            headers.setCacheControl(CacheControl.maxAge(Duration.ofMinutes(10)).cachePublic());

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        }
    }

    @GetMapping("/download-file/{file-name}")
    @Operation(
            summary = "Download file (PDF or Image)",
            description = "Forces download of the file instead of preview",
            tags = {"File"}
    )
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("file-name") String fileName) throws IOException {
        try (InputStream inputStream = fileService.getFileByFileName(fileName)) {
            byte[] bytes = inputStream.readAllBytes();

            String contentType = URLConnection.guessContentTypeFromName(fileName);
            if (contentType == null) {
                contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(bytes));
            }
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));

            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename(fileName, StandardCharsets.UTF_8)
                            .build()
            );

            headers.setCacheControl(CacheControl.noCache());

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        }
    }

    @GetMapping("/download-all-pdfs")
    @Operation(
            summary = "Download all PDF files as ZIP",
            description = "Downloads all PDF files in a single ZIP archive",
            tags = {"File"}
    )
    @SecurityRequirement(name = "hrd")
    public ResponseEntity<byte[]> downloadAllPdfFiles(@RequestParam String zipFileName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            List<String> pdfFiles = fileService.getAllPdfFileNames();

            for (String pdfFileName : pdfFiles) {
                try (InputStream inputStream = fileService.getFileByFileName(pdfFileName)) {
                    String fileNameWithoutExtension = pdfFileName.substring(0, pdfFileName.lastIndexOf('.'));
                    CandidateResponseAdmin candidateResponseAdmin = candidateService.getCandidateById(UUID.fromString(fileNameWithoutExtension));
                    ZipEntry zipEntry = new ZipEntry(candidateResponseAdmin.getFullName() + ".pdf");
                    zos.putNextEntry(zipEntry);
                    inputStream.transferTo(zos);
                    zos.closeEntry();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to download PDF file: " + pdfFileName, e);
                }
            }
        }

        byte[] zipBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/zip"));

        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(zipFileName + ".zip", StandardCharsets.UTF_8)
                        .build()
        );

        headers.setCacheControl(CacheControl.noCache());

        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }

}
