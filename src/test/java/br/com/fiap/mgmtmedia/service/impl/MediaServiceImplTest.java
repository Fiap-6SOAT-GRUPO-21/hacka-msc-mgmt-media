package br.com.fiap.mgmtmedia.service.impl;

import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import br.com.fiap.mgmtmedia.enumerated.MediaStatus;
import br.com.fiap.mgmtmedia.exception.custom.MediaException;
import br.com.fiap.mgmtmedia.repository.MediaRepository;
import br.com.fiap.mgmtmedia.service.S3Service;
import br.com.fiap.mgmtmedia.sqs.producer.SQSProducer;
import br.com.fiap.mgmtmedia.utils.JwtParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaServiceImplTest {

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private SQSProducer sqsProducer;

    @Mock
    private MultipartFile mediaFile;

    @InjectMocks
    private MediaServiceImpl mediaService;

    private static final String userReference = "user123";
    private final UUID mediaId = UUID.randomUUID();
    private final String storagePath = "pending-process/user123/" + mediaId;

    @BeforeAll
    static void setUpAll() {
        Mockito.mockStatic(JwtParser.class);
        when(JwtParser.getUserIdentifier()).thenReturn(userReference);
        when(JwtParser.getPhoneNumber()).thenReturn("+5511999999999");
    }

    @Test
    void testUploadMediaFileSuccess() throws IOException {
        byte[] fileContent = "test content".getBytes();
        when(mediaFile.isEmpty()).thenReturn(false);
        when(mediaFile.getBytes()).thenReturn(fileContent);

        when(mediaRepository.save(any())).thenAnswer(invocation -> {
            MediaMetadata metadata = invocation.getArgument(0);
            metadata.setMediaId(UUID.randomUUID());
            return metadata;
        });


        doNothing().when(s3Service).putObject(anyString(), any());
        doNothing().when(sqsProducer).publishMessage(any());

        MediaMetadata result = mediaService.uploadMediaFile(mediaFile);

        assertNotNull(result);
        assertEquals(MediaStatus.UPLOADED, result.getStatus());
        assertEquals(userReference, result.getUserReference());
    }

    @Test
    void testUploadMediaFileThrowsExceptionWhenFileIsEmpty() {
        when(mediaFile.isEmpty()).thenReturn(true);

        assertThrows(MediaException.class, () -> mediaService.uploadMediaFile(mediaFile));
    }

    @Test
    void testGetURLMediaMetadataByIdSuccess() {
        MediaMetadata mediaMetadata = new MediaMetadata();
        mediaMetadata.setStoragePath(storagePath);
        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(mediaMetadata));
        when(s3Service.generatePresignedUrl(storagePath)).thenReturn("https://s3-url.com/media");

        String url = mediaService.getURLMediaMetadataById(mediaId);

        assertEquals("https://s3-url.com/media", url);
    }

    @Test
    void testGetURLMediaMetadataByIdReturnsNullWhenNotFound() {
        when(mediaRepository.findById(mediaId)).thenReturn(Optional.empty());

        String url = mediaService.getURLMediaMetadataById(mediaId);

        assertNull(url);
    }

    @Test
    void testGetMediaMetadataById() {
        MediaMetadata mediaMetadata = new MediaMetadata();
        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(mediaMetadata));

        Optional<MediaMetadata> result = mediaService.getMediaMetadataById(mediaId);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetMediaMetadataByUser() {
        MediaMetadata mediaMetadata = new MediaMetadata();
        when(mediaRepository.findByUserReference(userReference)).thenReturn(List.of(mediaMetadata));

        List<MediaMetadata> result = mediaService.getMediaMetadataByUser(userReference);

        assertEquals(1, result.size());
    }
}
