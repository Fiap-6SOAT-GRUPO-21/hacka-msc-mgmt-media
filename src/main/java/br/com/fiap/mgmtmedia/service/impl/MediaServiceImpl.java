package br.com.fiap.mgmtmedia.service.impl;

import br.com.fiap.mgmtmedia.amqp.model.MediaMessage;
import br.com.fiap.mgmtmedia.amqp.producer.RabbitProducer;
import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import br.com.fiap.mgmtmedia.enumerated.MediaStatus;
import br.com.fiap.mgmtmedia.repository.MediaRepository;
import br.com.fiap.mgmtmedia.service.MediaService;
import br.com.fiap.mgmtmedia.service.S3Service;
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

    private final MediaRepository mediaRepository;

    private final RabbitProducer rabbitProducer;

    private final S3Service s3Service;

    @Override
    public MediaMetadata uploadMediaFile(MultipartFile mediaFile) {
        log.debug("Uploading media file: {}", mediaFile.toString());

        try {
            String fileName = mediaFile.getOriginalFilename();
            s3Service.putObject(fileName, mediaFile.getBytes());
            log.info("File uploaded successfully.");

            MediaMetadata mediaMetadata = MediaMetadata.builder()
                    .status(MediaStatus.UPLOADED)
                    .userReference("userReference")
                    .build();

            final MediaMetadata storedMediaMetadata = mediaRepository.save(mediaMetadata);

            MediaMessage mediaMessage = MediaMessage.builder()
                    .mediaId(storedMediaMetadata.getMediaId())
                    .storagePath(fileName)
                    .userReference("userReference")
                    .build();
            rabbitProducer.send(mediaMessage);

            return storedMediaMetadata;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<MediaMetadata> getMediaMetadataByUser(Pageable pageable, String userReference) {
        return mediaRepository.findByUserReference(pageable, userReference);
    }
}
