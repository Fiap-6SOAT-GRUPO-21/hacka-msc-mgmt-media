package br.com.fiap.mgmtmedia.controller;

import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import br.com.fiap.mgmtmedia.service.MediaService;
import br.com.fiap.mgmtmedia.utils.JwtParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaControllerTest {

    @Mock
    private MediaService mediaService;

    @Mock
    private JwtParser jwtParser;

    @InjectMocks
    private MediaController mediaController;

    private MediaMetadata mediaMetadata;
    private UUID mediaId;

    @BeforeEach
    void setUp() {
        mediaId = UUID.randomUUID();
        mediaMetadata = new MediaMetadata();
        mediaMetadata.setMediaId(mediaId);
    }

    @Test
    void testUploadMediaFile() {
        MultipartFile mediaFile = new MockMultipartFile("testfile.mp4", "testfile.mp4", "video/mp4", "test data".getBytes());

        when(mediaService.uploadMediaFile(any(MultipartFile.class))).thenReturn(mediaMetadata);

        ResponseEntity<MediaMetadata> response = mediaController.uploadMediaFile(mediaFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mediaMetadata, response.getBody());
    }

    @Test
    void testGetMediaMetadataById() {
        when(mediaService.getMediaMetadataById(mediaId)).thenReturn(Optional.of(mediaMetadata));

        ResponseEntity<MediaMetadata> response = mediaController.getMediaMetadataById(mediaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mediaMetadata, response.getBody());
    }

    @Test
    void testGetMediaMetadataByIdNotFound() {
        when(mediaService.getMediaMetadataById(mediaId)).thenReturn(Optional.empty());

        ResponseEntity<MediaMetadata> response = mediaController.getMediaMetadataById(mediaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetURLMediaMetadataById() {
        String downloadUrl = "https://example.com/media/" + mediaId;

        when(mediaService.getURLMediaMetadataById(mediaId)).thenReturn(downloadUrl);

        ResponseEntity<String> response = mediaController.getURLMediaMetadataById(mediaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(downloadUrl, response.getBody());
    }
}