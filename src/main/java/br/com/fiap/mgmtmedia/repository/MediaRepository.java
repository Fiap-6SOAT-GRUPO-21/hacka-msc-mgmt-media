package br.com.fiap.mgmtmedia.repository;

import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<MediaMetadata, String> {

    Page<MediaMetadata> findByUserReference(Pageable pageable, String userReference);

}
