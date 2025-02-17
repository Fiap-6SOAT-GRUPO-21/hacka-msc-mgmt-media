package br.com.fiap.mgmtmedia.repository;

import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MediaRepository extends JpaRepository<MediaMetadata, UUID> {

    List<MediaMetadata> findByUserReference(String userReference);
}
