package com.talixmines.management.inventorymanager.services.impl;

import com.talixmines.management.exception.*;
import com.talixmines.management.inventorymanager.dto.*;
import com.talixmines.management.inventorymanager.model.LigneCommandeClient;
import com.talixmines.management.inventorymanager.model.LigneCommandeFournisseur;
import com.talixmines.management.inventorymanager.model.LigneVente;
import com.talixmines.management.inventorymanager.repository.ArticleRepository;
import com.talixmines.management.inventorymanager.repository.LigneCommandeClientRepository;
import com.talixmines.management.inventorymanager.repository.LigneCommandeFournisseurRepository;
import com.talixmines.management.inventorymanager.repository.LigneVenteRepository;
import com.talixmines.management.inventorymanager.services.ArticleService;
import com.talixmines.management.inventorymanager.validator.ArticleValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ArticleServiceImpl implements ArticleService {
  static final String ITEM_NOT_FOUND_MESSAGE = "Item not found with the id: {0}";
  static final String ITEM_CODE_ALREADY_EXISTS = "Item already exists with the code: {0}";
  static final String ITEM_CODE_NOT_FOUND_MESSAGE = "Item not found with the code: {0}";
  static final String ITEM_NOT_VALIDE_MESSAGE = "Item is not valid";
  static final String ITEM_CUSTOMER_ORDER_REMOVAL_UNAUTHORIZED_MESSAGE = "Unable to delete an item already used in customer orders";
  static final String ITEM_SUPPLIER_ORDER_REMOVAL_UNAUTHORIZED_MESSAGE = "Unable to delete an item already used in supplier orders";
  static final String ITEM_SALES_ORDER_REMOVAL_UNAUTHORIZED_MESSAGE = "Unable to delete an item already used in sales";

  private ArticleRepository articleRepository;
  private LigneVenteRepository venteRepository;
  private LigneCommandeFournisseurRepository commandeFournisseurRepository;
  private LigneCommandeClientRepository commandeClientRepository;

  @Autowired
  public ArticleServiceImpl(
          ArticleRepository articleRepository,
          LigneVenteRepository venteRepository, LigneCommandeFournisseurRepository commandeFournisseurRepository,
          LigneCommandeClientRepository commandeClientRepository) {
    this.articleRepository = articleRepository;
    this.venteRepository = venteRepository;
    this.commandeFournisseurRepository = commandeFournisseurRepository;
    this.commandeClientRepository = commandeClientRepository;
  }

  @Override
  public ArticleDto save(ArticleDto dto) {
    /* Checking if codeArticle exists
    if (articleRepository.existsByCodeArticle(dto.getCodeArticle())) {
      throw new ResourceAlreadyExistException(MessageFormat.format(ITEM_CODE_ALREADY_EXISTS, dto.getCodeArticle()));
    }
*/
    List<String> errors = ArticleValidator.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Article is not valid {}", dto);
      throw new InvalidEntityException(ITEM_NOT_VALIDE_MESSAGE, ErrorCodes.ARTICLE_NOT_VALID, errors);
    }

    var savedArticle = ArticleDto.fromEntity(
        articleRepository.save(
            ArticleDto.toEntity(dto)
        )
    );

    log.info("savedArticle end ok - articleId: {}", savedArticle.getId());
    log.trace("savedArticle end ok - article: {}", savedArticle);

    return savedArticle;
  }

  @Override
  public ArticleDto findById(Integer id) {
    if (id == null) {
      log.error("Article ID is null");
      return null;
    }

    ArticleDto article = articleRepository.findById(id).map(ArticleDto::fromEntity).orElseThrow(() ->
        new EntityNotFoundException(
            MessageFormat.format(ITEM_NOT_FOUND_MESSAGE, id),
            ErrorCodes.ARTICLE_NOT_FOUND)
    );

    log.info("readArticleById end ok - articleId: {}", id);
    log.trace("readArticleById end ok - article: {}", article);

    return article;
  }

  @Override
  public ArticleDto findByCodeArticle(String codeArticle) {
    if (!StringUtils.hasLength(codeArticle)) {
      log.error("Article CODE is null");
      return null;
    }

    ArticleDto article = articleRepository.findArticleByCodeArticle(codeArticle)
        .map(ArticleDto::fromEntity)
        .orElseThrow(() ->
            new EntityNotFoundException(
                MessageFormat.format(ITEM_CODE_NOT_FOUND_MESSAGE, codeArticle),
                ErrorCodes.ARTICLE_NOT_FOUND)
        );

    log.info("readArticleByCode end ok - code: {}", codeArticle);
    log.trace("readArticleByCode end ok - article: {}", article);

    return article;
  }

  @Override
  public List<ArticleDto> findAll() {
    return articleRepository.findAll().stream()
        .map(ArticleDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
    return venteRepository.findAllByArticleId(idArticle).stream()
        .map(LigneVenteDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<LigneCommandeClientDto> findHistoriaueCommandeClient(Integer idArticle) {
    return commandeClientRepository.findAllByArticleId(idArticle).stream()
        .map(LigneCommandeClientDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
    return commandeFournisseurRepository.findAllByArticleId(idArticle).stream()
        .map(LigneCommandeFournisseurDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory) {
    return articleRepository.findAllByCategoryId(idCategory).stream()
        .map(ArticleDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public Page<ArticleDto> readArticlesByFilters(String designation, Integer categoryId, Instant since, Instant until, Pageable pageable) {

    /* Getting articles */
    Page<ArticleDto> articles = articleRepository.findByFilter(
                    designation,
                    categoryId,
                    since,
                    until,
                    pageable)
                    .map(ArticleDto::fromEntity);

    log.info(
            "readArticlesByFilters end ok - categoryId: {} - designation: {}  - since: {} - until: {} ",
            categoryId, designation,  since, until);


    log.trace("readArticlesByFilters end ok - articles: {}", articles);

    return articles;
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Article ID is null");
      return;
    }
    ArticleDto articleDto = findById(id);

    List<LigneCommandeClient> ligneCommandeClients = commandeClientRepository.findAllByArticleId(id);
    if (!ligneCommandeClients.isEmpty()) {
      throw new InvalidOperationException(ITEM_CUSTOMER_ORDER_REMOVAL_UNAUTHORIZED_MESSAGE, ErrorCodes.ARTICLE_ALREADY_IN_USE);
    }
    List<LigneCommandeFournisseur> ligneCommandeFournisseurs = commandeFournisseurRepository.findAllByArticleId(id);
    if (!ligneCommandeFournisseurs.isEmpty()) {
      throw new InvalidOperationException(ITEM_SUPPLIER_ORDER_REMOVAL_UNAUTHORIZED_MESSAGE,
          ErrorCodes.ARTICLE_ALREADY_IN_USE);
    }
    List<LigneVente> ligneVentes = venteRepository.findAllByArticleId(id);
    if (!ligneVentes.isEmpty()) {
      throw new InvalidOperationException(ITEM_SALES_ORDER_REMOVAL_UNAUTHORIZED_MESSAGE,
          ErrorCodes.ARTICLE_ALREADY_IN_USE);
    }
    articleRepository.deleteById(id);

    log.info("deleteArticle end ok - articleId: {}", id);
    log.info("deleteArticle end ok - article: {}", articleDto);
  }
}
