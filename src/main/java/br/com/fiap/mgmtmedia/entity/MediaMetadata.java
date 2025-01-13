package br.com.fiap.mgmtmedia.entity;

import br.com.fiap.mgmtmedia.enumerated.MediaStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MediaMetadata extends AuditData {

    @Id
    @UuidGenerator
    private UUID mediaId;

    private String storagePath;

    private MediaStatus status;

    private String userReference;

    private String zippedFolderPath;

}
