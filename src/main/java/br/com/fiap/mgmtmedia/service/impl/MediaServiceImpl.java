package br.com.fiap.mgmtmedia.service.impl;

import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import br.com.fiap.mgmtmedia.enumerated.MediaStatus;
import br.com.fiap.mgmtmedia.exception.custom.MediaException;
import br.com.fiap.mgmtmedia.repository.MediaRepository;
import br.com.fiap.mgmtmedia.service.MediaService;
import br.com.fiap.mgmtmedia.service.S3Service;
import br.com.fiap.mgmtmedia.sqs.model.MediaMessage;
import br.com.fiap.mgmtmedia.sqs.producer.SQSProducer;
import br.com.fiap.mgmtmedia.utils.JwtParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    @Value("${variables.aws.bucket-name}")
    private String bucketName;

    private final MediaRepository mediaRepository;

    private final SQSProducer sqsProducer;

    private final S3Service s3Service;

    @Override
    public MediaMetadata uploadMediaFile(MultipartFile mediaFile) {
        log.debug("Uploading media file: {}", mediaFile.toString());

        try {
            String fileName = mediaFile.getOriginalFilename();

            s3Service.putObject(fileName, mediaFile.getBytes());

            final String userIdentifier = JwtParser.getUserIdentifier();

            MediaMetadata mediaMetadata = MediaMetadata.builder()
                    .status(MediaStatus.UPLOADED)
                    .storagePath(buildStoragePath(fileName))
                    .userReference(userIdentifier)
                    .build();

            final MediaMetadata storedMediaMetadata = mediaRepository.save(mediaMetadata);

            MediaMessage mediaMessage = MediaMessage.builder()
                    .mediaId(storedMediaMetadata.getMediaId())
                    .storagePath(fileName)
                    .userReference(userIdentifier)
                    .build();

            sqsProducer.publishMessage(mediaMessage);

            return storedMediaMetadata;
        } catch (IOException e) {
            log.error("Error uploading media file: {}", e.getMessage(), e);
            throw new MediaException("Error uploading media file");
        }
    }

    private String buildStoragePath(final String fileName) {
        return "s3://" + bucketName + "/" + fileName;
    }

    @Override
    public Page<MediaMetadata> getMediaMetadataByUser(Pageable pageable, String userReference) {
        return mediaRepository.findByUserReference(pageable, userReference);
    }
}
