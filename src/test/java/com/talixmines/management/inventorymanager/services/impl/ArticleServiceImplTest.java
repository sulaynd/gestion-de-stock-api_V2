package com.talixmines.management.inventorymanager.services.impl;

import com.talixmines.management.exception.*;
import com.talixmines.management.inventorymanager.dto.ArticleDto;
import com.talixmines.management.inventorymanager.dto.CategoryDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeClientDto;
import com.talixmines.management.inventorymanager.dto.LigneVenteDto;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import com.talixmines.management.inventorymanager.services.ArticleService;
import com.talixmines.management.inventorymanager.services.CategoryService;
import com.talixmines.management.utils.ConstantUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceImplTest {

  @Autowired
  private ArticleService articleService;

  @Autowired
  private CategoryService categoryService;

  @Test
  public void shouldSaveArticleWithSuccess() {

    var category = ConstantUtils.getCategory();
    category.setId(103);
    var articleDto = ConstantUtils.getArticle();
    var codeArticle = "ART-X";
    articleDto.setCodeArticle(codeArticle);
    articleDto.setCategory(category);

    var savedArticle = articleService.save(articleDto);

    assertNotNull(savedArticle);
    assertNotNull(savedArticle.getId());
    assertEquals(articleDto.getCodeArticle(), savedArticle.getCodeArticle());
    assertEquals(articleDto.getDesignation(), savedArticle.getDesignation());
    assertEquals(articleDto.getIdEntreprise(), savedArticle.getIdEntreprise());
    assertEquals(category.getId(), savedArticle.getCategory().getId());
  }

  @Test(expected = InvalidEntityException.class)
  public void testSaveArticleShouldThrowInvalidEntityException() {
    ArticleDto expectedArticle = ArticleDto.builder()
            .designation("Designation test")
            .build();

    articleService.save(expectedArticle);
  }

  @Test
  public final void testReadArticleById() throws Exception {

    /* Getting article */
    ArticleDto article = articleService.findById(100);

    assertNotNull(article,"The article should be existing");
    assertEquals(article.getIdEntreprise(),3);
  }

  @Test
  public final void testReadArticleByIdShouldThrowEntityNotFoundException(){
    /* Getting non existing Article */
    EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> articleService.findById(0));

    assertEquals(ErrorCodes.ARTICLE_NOT_FOUND, expectedException.getErrorCode());
  }


  @Test
  public final void testReadArticleByIdWithIdIsNull() throws Exception {

    /* Getting article */
    ArticleDto article = articleService.findById(null);

    assertNull(article);
  }

  @Test
  public final void testReadArticleByCodeArticle() throws Exception {

    /* Getting article */
    ArticleDto article = articleService.findByCodeArticle("ART-100");

    assertNotNull(article,"The article should be existing");
    assertEquals(article.getIdEntreprise(),3);
  }

  @Test
  public final void testReadArticleWithCodeIsEmpty() throws Exception {

    /* Getting article */
    ArticleDto article = articleService.findByCodeArticle("");

    assertNull(article);
  }

  @Test(expected = EntityNotFoundException.class)
  public final void testReadArticleByCodeShouldThrowEntityNotFoundException() throws Exception {
    /* Getting non existing CodeArticle */
    articleService.findByCodeArticle("0");
  }

  @Test
  public final void testReadArticles() throws Exception {

    /* Getting articles */
    List<ArticleDto> articles = articleService.findAll();

    assertNotNull(articles);
    assertTrue(articles.size() > 0);
  }


  @Test
  public final void testReadArticlesByFilters() throws Exception {

    /* Getting articles*/
    Page<ArticleDto> articles = articleService.readArticlesByFilters(null, 101,null, null, PageRequest.of(0, 3));

    assertNotNull(articles);
    assertEquals(articles.getContent().get(0).getId().intValue(), 100);
    assertEquals(articles.getTotalElements(), 5);
    assertEquals(articles.getTotalPages(), 2);

  }


  @Test
  public final void testReadHistoriqueVentes() throws Exception {

    /* Getting linesSales */
    List<LigneVenteDto> linesSales = articleService.findHistoriqueVentes(100);

    assertNotNull(linesSales);
    assertTrue(linesSales.size() > 0);
    assertEquals(linesSales.get(0).getArticle().getId(),100);
    assertEquals(linesSales.get(0).getArticle().getCodeArticle(),"ART-100");
    assertEquals(linesSales.get(0).getQuantite(), new BigDecimal("10.00"));
    assertEquals(linesSales.get(0).getPrixUnitaire(), new BigDecimal("5.00"));
  }

  @Test
  public final void testReadHistoriqueCommandeClient() throws Exception {

    /* Getting linesOrder */
    List<LigneCommandeClientDto> ligneCommandeClientDtos = articleService.findHistoriaueCommandeClient(102);

    assertNotNull(ligneCommandeClientDtos);
    assertTrue(ligneCommandeClientDtos.size() > 0);
    assertEquals(ligneCommandeClientDtos.get(0).getArticle().getId(),102);
    assertEquals(ligneCommandeClientDtos.get(0).getQuantite(),new BigDecimal("10.00"));
  }

  @Test(expected = EntityNotFoundException.class)
  public final void testDeleteArticle() throws Exception {
    /* Deleting article */
    articleService.delete(108);
    /* Getting article */
    articleService.findById(108);
  }


  public final void testDeleteArticleShouldThrowEntityNotFoundException() {
    /* Deleting article */
    EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> articleService.delete(0));

    assertEquals(ErrorCodes.ARTICLE_NOT_FOUND, expectedException.getErrorCode());

  }

  @Test
  public final void testDeleteCategoryWithEmptyId() throws Exception {
    /* Deleting article */
    articleService.delete(null);
  }

  @Test(expected = InvalidOperationException.class)
  public final void testDeleteCategoryContainingOrder() throws Exception {
    /* Deleting article contained in order */
    articleService.delete(102);
  }
}
