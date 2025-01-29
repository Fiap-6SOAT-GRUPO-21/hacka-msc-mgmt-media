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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private static final String PENDING_PROCESS_FOLDER = "pending-process";

    private static final String PROCESSED_FOLDER = "processed";

    private final MediaRepository mediaRepository;

    private final SQSProducer sqsProducer;

    private final S3Service s3Service;

    @Override
    public MediaMetadata uploadMediaFile(MultipartFile mediaFile) {
        validateMediaFile(mediaFile);

        log.info("Starting upload of media file: {}", mediaFile.getOriginalFilename());

        try {
            String userIdentifier = JwtParser.getUserIdentifier();

            MediaMetadata mediaMetadata = initializeMediaMetadata(userIdentifier);

            String storagePath = buildObjectKey(userIdentifier, mediaMetadata.getMediaId().toString());
            mediaMetadata.setStoragePath(storagePath);

            mediaMetadata = mediaRepository.save(mediaMetadata);

            saveFileToS3(mediaFile, storagePath);

            sendMediaMessage(mediaMetadata);

            log.info("Media file uploaded successfully: {}", mediaMetadata);

            return mediaMetadata;
        } catch (IOException e) {
            log.error("Error uploading media file: {}", e.getMessage(), e);
            throw new MediaException("Error uploading media file", e);
        }
    }

    private void validateMediaFile(MultipartFile mediaFile) {
        if (mediaFile == null || mediaFile.isEmpty()) {
            throw new MediaException("Invalid media file. File is null or empty.");
        }
    }

    private MediaMetadata initializeMediaMetadata(String userIdentifier) {
        return mediaRepository.save(
                MediaMetadata.builder()
                        .status(MediaStatus.UPLOADED)
                        .userReference(userIdentifier)
                        .storagePath("")
                        .build()
        );
    }

    private String buildObjectKey(String userIdentifier, String mediaId) {
        return String.format("%s/%s/%s", PENDING_PROCESS_FOLDER, userIdentifier, mediaId);
    }

    private void saveFileToS3(MultipartFile mediaFile, String objectKey) throws IOException {
        log.debug("Saving file to S3 with object key: {}", objectKey);
        s3Service.putObject(objectKey, mediaFile.getBytes());
    }

    private void sendMediaMessage(MediaMetadata mediaMetadata) {
        MediaMessage mediaMessage = MediaMessage.builder()
                .mediaId(mediaMetadata.getMediaId())
                .storagePath(mediaMetadata.getStoragePath())
                .userReference(mediaMetadata.getUserReference())
                .phoneNumber(JwtParser.getPhoneNumber())
                .status(mediaMetadata.getStatus().name())
                .build();

        log.debug("Sending message to SQS for media ID: {}", mediaMetadata.getMediaId());
        sqsProducer.publishMessage(mediaMessage);
    }

    @Override
    public Page<MediaMetadata> getMediaMetadataByUser(Pageable pageable, String userReference) {
        log.debug("Fetching media metadata for user: {}", userReference);
        return mediaRepository.findByUserReference(pageable, userReference);
    }
}