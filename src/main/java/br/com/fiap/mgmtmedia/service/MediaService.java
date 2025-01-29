package br.com.fiap.mgmtmedia.service;

import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaService {

    MediaMetadata uploadMediaFile(MultipartFile mediaFile);

    String getURLMediaMetadataById(UUID id);

    Optional<MediaMetadata> getMediaMetadataById(UUID userReference);

    List<MediaMetadata> getMediaMetadataByUser(String userReference);

}
