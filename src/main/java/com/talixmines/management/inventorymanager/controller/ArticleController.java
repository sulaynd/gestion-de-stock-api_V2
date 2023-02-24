package com.talixmines.management.inventorymanager.controller;

import com.talixmines.management.inventorymanager.controller.api.ArticleApi;
import com.talixmines.management.inventorymanager.dto.ArticleDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeClientDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeFournisseurDto;
import com.talixmines.management.inventorymanager.dto.LigneVenteDto;
import com.talixmines.management.inventorymanager.services.ArticleService;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController implements ArticleApi {

  private ArticleService articleService;

  @Autowired
  public ArticleController(
      ArticleService articleService
  ) {
    this.articleService = articleService;
  }

  @Override
  public ArticleDto save(ArticleDto dto) {
    return articleService.save(dto);
  }

  @Override
  public ArticleDto findById(Integer id) {
    return articleService.findById(id);
  }

  @Override
  public ArticleDto findByCodeArticle(String codeArticle) {
    return articleService.findByCodeArticle(codeArticle);
  }
/*
  @Override
  public List<ArticleDto> findAll() {
    return articleService.findAll();
  }
*/
  @Override
  public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
    return articleService.findHistoriqueVentes(idArticle);
  }

  @Override
  public List<LigneCommandeClientDto> findHistoriaueCommandeClient(Integer idArticle) {
    return articleService.findHistoriaueCommandeClient(idArticle);
  }

  @Override
  public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
    return articleService.findHistoriqueCommandeFournisseur(idArticle);
  }

  @Override
  public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory) {
    return articleService.findAllArticleByIdCategory(idCategory);
  }

  @Override
  public void delete(Integer id) {
    articleService.delete(id);
  }

  @Override
  public Page<ArticleDto> readArticles(String designation, Integer categoryId, Instant fromCreationDate, Instant toCreationDate, Pageable pageable) {
    return articleService.readArticlesByFilters(designation,categoryId,fromCreationDate,toCreationDate,pageable);
  }

  @Override
  public Page<ArticleDto> readArticlesByCategoryCode(Integer categoryId, Pageable pageable) {
    return articleService.readArticlesByFilters(null, categoryId,null,null, pageable);
  }
}
