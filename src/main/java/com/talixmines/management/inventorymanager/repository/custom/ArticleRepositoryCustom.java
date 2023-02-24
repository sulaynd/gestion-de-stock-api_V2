package com.talixmines.management.inventorymanager.repository.custom;

import com.talixmines.management.inventorymanager.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface ArticleRepositoryCustom {
    Page<Article> findByFilter(
            String designation,
            Integer categoryId,
            Instant since,
            Instant until,
            Pageable pageable);

}
