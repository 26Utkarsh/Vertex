package com.vertex.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID>, ItemSearchRepository {
    Optional<Item> findBySourceAndUrl(SourceType source, String url);

    Page<Item> findAllBySource(SourceType source, Pageable pageable);

    Page<Item> findByCreatedAtAfter(Instant createdAt, Pageable pageable);
}
