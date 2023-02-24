package com.talixmines.management.inventorymanager.repository;

import com.talixmines.management.inventorymanager.model.Article;
import java.util.List;
import java.util.Optional;

import com.talixmines.management.inventorymanager.repository.custom.ArticleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> , ArticleRepositoryCustom, QuerydslPredicateExecutor<Article>
//public interface ArticleRepository extends JpaRepository<Article, Integer>
{
  Optional<Article> findArticleByCodeArticle(String codeArticle);

  List<Article> findAllByCategoryId(Integer idCategory);


  boolean existsByCodeArticle(String codeArticle);
}
