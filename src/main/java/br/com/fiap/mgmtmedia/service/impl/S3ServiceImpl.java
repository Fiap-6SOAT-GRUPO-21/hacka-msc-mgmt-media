package br.com.fiap.mgmtmedia.service.impl;

import br.com.fiap.mgmtmedia.service.S3Service;
import br.com.fiap.mgmtmedia.storage.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Properties s3Properties;

    private final S3Client s3Client;

    @Override
    public void putObject(String key, byte[] file) {
        String bucketName = s3Properties.getBucketName();
        log.info("Uploading file to S3 bucket: {}", bucketName);
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        RequestBody requestBody = RequestBody.fromBytes(file);

        s3Client.putObject(objectRequest, requestBody);
        log.info("File uploaded successfully to S3 bucket: {}", bucketName);
    }

    @Override
    public byte[] getObject(String keyName) {
        String bucketName = s3Properties.getBucketName();
        log.info("Downloading file from S3 bucket: {}", bucketName);
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(objectRequest)) {
            log.info("File downloaded successfully from S3 bucket: {}", bucketName);
            return response.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Error downloading file from S3 bucket: " + bucketName, e);
        }
    }

    @Override
    public String generatePresignedUrl(String keyName) {
        log.info("Generating presigned URL for file: {}", keyName);
        try (S3Presigner presigner = S3Presigner.create()) {
            String bucketName = s3Properties.getBucketName();

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

            log.info("Presigned URL generated successfully for file: {}", keyName);

            return presignedRequest.url().toExternalForm();
        }
    }

    @Override
    public void deleteObject(String key) {
        String bucketName = s3Properties.getBucketName();
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

}
