package br.com.fiap.mgmtmedia.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

@Data
@Configuration("s3Properties")
@ConfigurationProperties(prefix = "storage.s3")
public class S3Properties {

    private String region;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    public AwsBasicCredentials getCredentialsProvider() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }
}
