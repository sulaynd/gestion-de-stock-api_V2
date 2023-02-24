package com.talixmines.management.inventorymanager.repository.custom.impl;

//import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.BooleanBuilder;
import com.talixmines.management.inventorymanager.model.Article;
//import com.talixmines.management.inventorymanager.model.QArticle;
import com.talixmines.management.inventorymanager.model.QArticle;
import com.talixmines.management.inventorymanager.repository.ArticleRepository;
import com.talixmines.management.inventorymanager.repository.custom.ArticleRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public Page<Article> findByFilter(String designation, Integer categoryId, Instant since, Instant until, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if(designation != null) {
            builder.and(QArticle.article.designation.containsIgnoreCase(designation));
        }

        if(categoryId != null) {
            builder.and(QArticle.article.category.id.eq(categoryId));
        }

        if (since != null && until != null) {
            builder.and(QArticle.article.creationDate.between(since, until));
        } else if (since != null) {
            builder.and(QArticle.article.creationDate.goe(since));
        } else if (until != null) {
            builder.and(QArticle.article.creationDate.loe(until));
        }

        return articleRepository.findAll(builder, pageable);
    }
}
