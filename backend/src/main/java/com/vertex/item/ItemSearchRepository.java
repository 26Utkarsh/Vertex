package com.vertex.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemSearchRepository {
    Page<Item> search(String query, SourceType source, Pageable pageable);
}

