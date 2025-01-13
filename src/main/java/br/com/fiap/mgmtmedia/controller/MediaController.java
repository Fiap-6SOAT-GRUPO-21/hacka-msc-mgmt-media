package br.com.fiap.mgmtmedia.controller;

import br.com.fiap.mgmtmedia.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/medias")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadMediaFile(@RequestParam("media") MultipartFile mediaFile) {
        mediaService.uploadMediaFile(mediaFile);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<Void> getMediaMetadataByUser(
            Pageable pageable,
            @RequestParam("userReference") String userReference
    ) {
        mediaService.getMediaMetadataByUser(pageable, userReference);
        return ResponseEntity.ok().build();
    }

}
