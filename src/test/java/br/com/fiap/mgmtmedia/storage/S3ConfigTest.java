package br.com.fiap.mgmtmedia.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class S3ConfigTest {

    @InjectMocks
    private S3Config s3Config;

    @Mock
    private S3Client s3ClientMock;

    @Value("${variables.aws.region}")
    private String region;

    @Value("${variables.aws.access-key}")
    private String accessKey;

    @Value("${variables.aws.secret-key}")
    private String secretKey;

    @Value("${variables.aws.session-token}")
    private String sessionToken;

    @Value("${variables.env}")
    private String env;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Config, "region", "us-east-1");
        ReflectionTestUtils.setField(s3Config, "accessKey", "access-key");
        ReflectionTestUtils.setField(s3Config, "secretKey", "secret-key");
        ReflectionTestUtils.setField(s3Config, "sessionToken", "session-token");
    }

    @Test
    void testS3Client_localEnvironment() {
        ReflectionTestUtils.setField(s3Config, "env", "local");

        S3Client s3Client = s3Config.s3Client();

        assertNotNull(s3Client);
    }

    @Test
    void testS3Client_awsEnvironment() {
        ReflectionTestUtils.setField(s3Config, "env", "aws");

        S3Client s3Client = s3Config.s3Client();

        assertNotNull(s3Client);
    }

}