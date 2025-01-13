package br.com.fiap.mgmtmedia.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Log4j2
@Configuration
public class S3Config {

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

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        // Se for ambiente local (usando o LocalStack)
        if (env.equals("local")) {
            log.info("Running in local environment, using LocalStack for S3");
            return S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(() -> awsBasicCredentials)
                    .endpointOverride(URI.create("http://localhost:4566"))
                    .forcePathStyle(true)
                    .build();
        }

        log.info("Running in AWS environment");
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(() -> awsBasicCredentials)
                .build();
    }

}
