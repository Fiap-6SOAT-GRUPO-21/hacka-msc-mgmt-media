package br.com.fiap.mgmtmedia.sqs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MediaMessage(
        UUID mediaId,
        String storagePath,
        String userReference,
        String status,
        String zippedPath
) {
}
