package br.com.fiap.mgmtmedia.sqs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaMessage {

    private UUID mediaId;
    private String storagePath;
    private String userReference;
    private String phoneNumber;
    private String status;
    private String zippedPath;
}
