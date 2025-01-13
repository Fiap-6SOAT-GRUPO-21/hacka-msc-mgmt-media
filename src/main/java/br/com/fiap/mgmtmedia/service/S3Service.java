package br.com.fiap.mgmtmedia.service;

public interface S3Service {

    void putObject(String key, byte[] file);

    byte[] getObject(String keyName);

    String generatePresignedUrl(String keyName);

    void deleteObject(String keyName);

}
