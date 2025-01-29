package br.com.fiap.mgmtmedia.controller;

import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import br.com.fiap.mgmtmedia.service.MediaService;
import br.com.fiap.mgmtmedia.utils.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/medias")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<MediaMetadata> uploadMediaFile(@RequestParam("media") MultipartFile mediaFile) {
        return ResponseEntity.ok(mediaService.uploadMediaFile(mediaFile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaMetadata> getMediaMetadataById(@PathVariable UUID id) {
        Optional<MediaMetadata> mediaMetadataById = mediaService.getMediaMetadataById(id);
        return mediaMetadataById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/user/all")
    public ResponseEntity<List<MediaMetadata>> getMediaMetadataByUser() {
        return ResponseEntity.ok(mediaService.getMediaMetadataByUser(JwtParser.getUserIdentifier()));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<String> getURLMediaMetadataById(@PathVariable UUID id) {
        return ResponseEntity.ok(mediaService.getURLMediaMetadataById(id));
    }

}
