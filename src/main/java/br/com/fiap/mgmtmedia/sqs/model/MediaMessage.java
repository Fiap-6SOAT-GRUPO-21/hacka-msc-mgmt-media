package br.com.fiap.mgmtmedia.sqs.model;

import br.com.fiap.mgmtmedia.enumerated.MediaStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MediaMessage(
        UUID mediaId,
        String storagePath,
        String userReference,
        MediaStatus status,
        String zippedPath
) {
}
