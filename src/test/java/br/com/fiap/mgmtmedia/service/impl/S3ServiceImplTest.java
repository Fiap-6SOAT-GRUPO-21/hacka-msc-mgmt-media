package br.com.fiap.mgmtmedia.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceImplTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    @InjectMocks
    private S3ServiceImpl s3Service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testPutObject() {
        String key = "test-file.txt";
        byte[] fileContent = "file content".getBytes();

        s3Service.putObject(key, fileContent);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void testGetObject() throws IOException {
        String key = "test-file.txt";
        byte[] expectedContent = "file content".getBytes();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> mockResponse = mock(ResponseInputStream.class);
        when(mockResponse.readAllBytes()).thenReturn(expectedContent);
        when(s3Client.getObject(getObjectRequest)).thenReturn(mockResponse);

        byte[] result = s3Service.getObject(key);

        assertArrayEquals(expectedContent, result);
        verify(s3Client, times(1)).getObject(getObjectRequest);
    }

    @Test
    void testDeleteObject() {
        String key = "test-file.txt";

        s3Service.deleteObject(key);

        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

}
