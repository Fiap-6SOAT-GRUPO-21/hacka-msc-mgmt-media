package br.com.fiap.mgmtmedia.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public S3Client s3Client() {
        final Region region = Region.of(s3Properties.getRegion());

        AwsBasicCredentials awsBasicCredentials = s3Properties.getCredentialsProvider();

        return S3Client.builder()
                .region(region)
                .credentialsProvider(() -> awsBasicCredentials)
                .forcePathStyle(true)
                .build();
    }

}
