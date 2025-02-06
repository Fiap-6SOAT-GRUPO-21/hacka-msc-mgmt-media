package br.com.fiap.mgmtmedia.sqs.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MediaMessageTest {

    @Test
    void testNoArgsConstructor() {
        MediaMessage mediaMessage = new MediaMessage();

        assertNotNull(mediaMessage);
        assertNull(mediaMessage.getMediaId());
        assertNull(mediaMessage.getStoragePath());
        assertNull(mediaMessage.getUserReference());
        assertNull(mediaMessage.getPhoneNumber());
        assertNull(mediaMessage.getStatus());
        assertNull(mediaMessage.getZippedPath());
    }

    @Test
    void testAllArgsConstructor() {
        UUID mediaId = UUID.randomUUID();
        String storagePath = "/path/to/storage";
        String userReference = "user123";
        String phoneNumber = "1234567890";
        String status = "PROCESSING";
        String zippedPath = "/path/to/zipped";

        MediaMessage mediaMessage = new MediaMessage(mediaId, storagePath, userReference, phoneNumber, status, zippedPath);

        assertNotNull(mediaMessage);
        assertEquals(mediaId, mediaMessage.getMediaId());
        assertEquals(storagePath, mediaMessage.getStoragePath());
        assertEquals(userReference, mediaMessage.getUserReference());
        assertEquals(phoneNumber, mediaMessage.getPhoneNumber());
        assertEquals(status, mediaMessage.getStatus());
        assertEquals(zippedPath, mediaMessage.getZippedPath());
    }

    @Test
    void testBuilder() {
        UUID mediaId = UUID.randomUUID();
        String storagePath = "/path/to/storage";
        String userReference = "user123";
        String phoneNumber = "1234567890";
        String status = "PROCESSING";
        String zippedPath = "/path/to/zipped";

        MediaMessage mediaMessage = MediaMessage.builder()
                .mediaId(mediaId)
                .storagePath(storagePath)
                .userReference(userReference)
                .phoneNumber(phoneNumber)
                .status(status)
                .zippedPath(zippedPath)
                .build();

        assertNotNull(mediaMessage);
        assertEquals(mediaId, mediaMessage.getMediaId());
        assertEquals(storagePath, mediaMessage.getStoragePath());
        assertEquals(userReference, mediaMessage.getUserReference());
        assertEquals(phoneNumber, mediaMessage.getPhoneNumber());
        assertEquals(status, mediaMessage.getStatus());
        assertEquals(zippedPath, mediaMessage.getZippedPath());
    }

    @Test
    void testSettersAndGetters() {
        MediaMessage mediaMessage = new MediaMessage();

        UUID mediaId = UUID.randomUUID();
        String storagePath = "/path/to/storage";
        String userReference = "user123";
        String phoneNumber = "1234567890";
        String status = "PROCESSING";
        String zippedPath = "/path/to/zipped";

        mediaMessage.setMediaId(mediaId);
        mediaMessage.setStoragePath(storagePath);
        mediaMessage.setUserReference(userReference);
        mediaMessage.setPhoneNumber(phoneNumber);
        mediaMessage.setStatus(status);
        mediaMessage.setZippedPath(zippedPath);

        assertEquals(mediaId, mediaMessage.getMediaId());
        assertEquals(storagePath, mediaMessage.getStoragePath());
        assertEquals(userReference, mediaMessage.getUserReference());
        assertEquals(phoneNumber, mediaMessage.getPhoneNumber());
        assertEquals(status, mediaMessage.getStatus());
        assertEquals(zippedPath, mediaMessage.getZippedPath());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID mediaId = UUID.randomUUID();
        String storagePath = "/path/to/storage";
        String userReference = "user123";
        String phoneNumber = "1234567890";
        String status = "PROCESSING";
        String zippedPath = "/path/to/zipped";

        MediaMessage mediaMessage1 = MediaMessage.builder()
                .mediaId(mediaId)
                .storagePath(storagePath)
                .userReference(userReference)
                .phoneNumber(phoneNumber)
                .status(status)
                .zippedPath(zippedPath)
                .build();

        MediaMessage mediaMessage2 = MediaMessage.builder()
                .mediaId(mediaId)
                .storagePath(storagePath)
                .userReference(userReference)
                .phoneNumber(phoneNumber)
                .status(status)
                .zippedPath(zippedPath)
                .build();

        assertEquals(mediaMessage1, mediaMessage2);
        assertEquals(mediaMessage1.hashCode(), mediaMessage2.hashCode());
    }

    @Test
    void testToString() {
        UUID mediaId = UUID.randomUUID();
        String storagePath = "/path/to/storage";
        String userReference = "user123";
        String phoneNumber = "1234567890";
        String status = "PROCESSING";
        String zippedPath = "/path/to/zipped";

        MediaMessage mediaMessage = MediaMessage.builder()
                .mediaId(mediaId)
                .storagePath(storagePath)
                .userReference(userReference)
                .phoneNumber(phoneNumber)
                .status(status)
                .zippedPath(zippedPath)
                .build();

        String expectedToString = "MediaMessage(mediaId=" + mediaId +
                ", storagePath=" + storagePath +
                ", userReference=" + userReference +
                ", phoneNumber=" + phoneNumber +
                ", status=" + status +
                ", zippedPath=" + zippedPath + ")";

        assertEquals(expectedToString, mediaMessage.toString());
    }
}