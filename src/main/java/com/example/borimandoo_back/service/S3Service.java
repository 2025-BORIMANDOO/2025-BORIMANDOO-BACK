package com.example.borimandoo_back.service;

import com.example.borimandoo_back.domain.LicenseImage;
import com.example.borimandoo_back.domain.Vet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public LicenseImage uploadFile(MultipartFile licenseImage) {
        try {
            String originalFilename = licenseImage.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = "licenses/" + UUID.randomUUID() + "_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ext;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(uniqueFileName)
                    .contentType(licenseImage.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                    licenseImage.getInputStream(), licenseImage.getSize()));

            String encodedFileName = URLEncoder.encode(uniqueFileName, StandardCharsets.UTF_8);
            String url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, encodedFileName);

            return LicenseImage.builder()
                    .imageUrl(url)
                    .build();

        } catch (IOException | AwsServiceException e) {
            throw new RuntimeException("S3 파일 업로드 실패: " + e.getMessage(), e);
        }
    }
}
