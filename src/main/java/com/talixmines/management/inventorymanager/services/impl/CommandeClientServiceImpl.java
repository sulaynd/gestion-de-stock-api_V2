package com.talixmines.management.inventorymanager.services.impl;

import com.talixmines.management.inventorymanager.dto.ArticleDto;
import com.talixmines.management.inventorymanager.dto.ClientDto;
import com.talixmines.management.inventorymanager.dto.CommandeClientDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeClientDto;
import com.talixmines.management.inventorymanager.dto.MvtStkDto;
import com.talixmines.management.exception.EntityNotFoundException;
import com.talixmines.management.exception.ErrorCodes;
import com.talixmines.management.exception.InvalidEntityException;
import com.talixmines.management.exception.InvalidOperationException;
import com.talixmines.management.inventorymanager.model.Article;
import com.talixmines.management.inventorymanager.model.Client;
import com.talixmines.management.inventorymanager.model.CommandeClient;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import com.talixmines.management.inventorymanager.model.LigneCommandeClient;
import com.talixmines.management.inventorymanager.model.SourceMvtStk;
import com.talixmines.management.inventorymanager.model.TypeMvtStk;
import com.talixmines.management.inventorymanager.services.ArticleService;
import com.talixmines.management.inventorymanager.services.ClientService;
import com.talixmines.management.inventorymanager.services.CommandeClientService;
import com.talixmines.management.inventorymanager.services.MvtStkService;
import com.talixmines.management.inventorymanager.repository.ArticleRepository;
import com.talixmines.management.inventorymanager.repository.ClientRepository;
import com.talixmines.management.inventorymanager.repository.CommandeClientRepository;
import com.talixmines.management.inventorymanager.repository.LigneCommandeClientRepository;
import com.talixmines.management.inventorymanager.validator.ArticleValidator;
import com.talixmines.management.inventorymanager.validator.CommandeClientValidator;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@Transactional
public class CommandeClientServiceImpl implements CommandeClientService {

  static final String CUSTOMER_ORDER_NOT_VALIDE_MESSAGE = "Customer order is not valid";
  static final String UPDATE_CUSTOMER_ORDER_ALREADY_DELIVERED_MESSAGE = "Unable to update order already delivered";
  static final String UPDATE_CUSTOMER_ORDER_WITHOUT_STATUS_MESSAGE = "Unable to update order without status";

  static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer not found with the id: {0}";
  static final String CUSTOMER_ORDER_ID_NOT_FOUND_MESSAGE = "Customer order not found with the id: {0}";
  static final String CUSTOMER_ORDER_CODE_NOT_FOUND_MESSAGE = "Customer order not found with the code: {0}";
  static final String DELETE_CUSTOMER_ORDER_ALREADY_USED_MESSAGE = "Unable to delete a customer order already used";
  static final String ORDER_WITHOUT_ITEM_MESSAGE = "Unable to save an order without item";

  static final String ITEM_NOT_FOUND_MESSAGE = "Item not found with the id: {0}";
  static final String ITEM_NOT_FOUND_IN_DB_MESSAGE = "Item does not exist in the database";
  private CommandeClientRepository commandeClientRepository;
  private LigneCommandeClientRepository ligneCommandeClientRepository;
  private ClientRepository clientRepository;
  private ArticleRepository articleRepository;
  private MvtStkService mvtStkService;

  private ArticleService articleService;
  private ClientService clientService;

  @Autowired
  public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository,
      ClientRepository clientRepository, ArticleRepository articleRepository, LigneCommandeClientRepository ligneCommandeClientRepository,
      MvtStkService mvtStkService,ArticleService articleService,ClientService clientService) {
    this.commandeClientRepository = commandeClientRepository;
    this.ligneCommandeClientRepository = ligneCommandeClientRepository;
    this.clientRepository = clientRepository;
    this.articleRepository = articleRepository;
    this.mvtStkService = mvtStkService;
    this.articleService = articleService;
    this.clientService = clientService;
  }

  @Override
  public CommandeClientDto save(CommandeClientDto dto) {

    List<String> errors = CommandeClientValidator.validate(dto);

    if (!errors.isEmpty()) {
      log.error(CUSTOMER_ORDER_NOT_VALIDE_MESSAGE);
      throw new InvalidEntityException(CUSTOMER_ORDER_NOT_VALIDE_MESSAGE, ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
    }

    if (dto.getId() != null && dto.isCommandeLivree()) {
      throw new InvalidOperationException(UPDATE_CUSTOMER_ORDER_ALREADY_DELIVERED_MESSAGE, ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }

    Optional<Client> client = clientRepository.findById(dto.getClient().getId());
    if (client.isEmpty()) {
      log.warn(MessageFormat.format(CUSTOMER_NOT_FOUND_MESSAGE, dto.getClient().getId()));
      throw new EntityNotFoundException(MessageFormat.format(CUSTOMER_NOT_FOUND_MESSAGE, dto.getClient().getId()),
          ErrorCodes.CLIENT_NOT_FOUND);
    }

    List<String> articleErrors = new ArrayList<>();

    if (dto.getLigneCommandeClients() != null) {
      dto.getLigneCommandeClients().forEach(ligCmdClt -> {
        if (ligCmdClt.getArticle() != null) {
          Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
          if (article.isEmpty()) {
            articleErrors.add(MessageFormat.format(ITEM_NOT_FOUND_MESSAGE, ligCmdClt.getArticle().getId()));
          }
        } else {
          articleErrors.add(ORDER_WITHOUT_ITEM_MESSAGE);
        }
      });
    }

    if (!articleErrors.isEmpty()) {
      log.warn("");
      throw new InvalidEntityException(ITEM_NOT_FOUND_IN_DB_MESSAGE, ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
    }
    dto.setDateCommande(Instant.now());
    CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(dto));

    if (dto.getLigneCommandeClients() != null) {
      dto.getLigneCommandeClients().forEach(ligCmdClt -> {
        LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligCmdClt);
        ligneCommandeClient.setCommandeClient(savedCmdClt);
        ligneCommandeClient.setIdEntreprise(dto.getIdEntreprise());
        LigneCommandeClient savedLigneCmd = ligneCommandeClientRepository.save(ligneCommandeClient);

        effectuerSortie(savedLigneCmd);
      });
    }

    return CommandeClientDto.fromEntity(savedCmdClt);
  }

  @Override
  public CommandeClientDto findById(Integer id) {
    if (id == null) {
      log.error("Commande client ID is NULL");
      return null;
    }
    return commandeClientRepository.findById(id)
        .map(CommandeClientDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format(CUSTOMER_ORDER_ID_NOT_FOUND_MESSAGE, id), ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
        ));
  }

  @Override
  public CommandeClientDto findByCode(String code) {
    if (!StringUtils.hasLength(code)) {
      log.error("Commande client CODE is NULL");
      return null;
    }
    return commandeClientRepository.findCommandeClientByCode(code)
        .map(CommandeClientDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format(CUSTOMER_ORDER_CODE_NOT_FOUND_MESSAGE, code), ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
        ));
  }

  @Override
  public List<CommandeClientDto> findAll() {
    return commandeClientRepository.findAll().stream()
        .map(CommandeClientDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Commande client ID is NULL");
      return;
    }
    List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(id);
    if (!ligneCommandeClients.isEmpty()) {
      throw new InvalidOperationException(DELETE_CUSTOMER_ORDER_ALREADY_USED_MESSAGE,
          ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE);
    }
    commandeClientRepository.deleteById(id);
  }

  @Override
  public Optional<LigneCommandeClient> findLigneCommandeClient(Integer idLigneCommande) {
    Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);
    if (ligneCommandeClientOptional.isEmpty()) {
      throw new EntityNotFoundException(
              "Aucune ligne commande client n'a ete trouve avec l'ID " + idLigneCommande, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
    }
    return ligneCommandeClientOptional;
  }

  @Override
  public Page<CommandeClientDto> readOrdersByFilters(String code, Instant fromCreationDate, Instant toCreationDate, EtatCommande orderStatus,List<EtatCommande> exceptStatuses, String customerEmail, String customerFirstName, String customerLastName, String customerNumTel, String customerZipCode, String codeArticle, Integer idCompany, Pageable pageable) {

    Page<CommandeClientDto> orders = commandeClientRepository.findByFilter(code,
                                                              fromCreationDate,
                                                              toCreationDate,
                                                              orderStatus,
                                                              exceptStatuses,
                                                              customerEmail,
                                                              customerFirstName,
                                                              customerLastName,
                                                              customerNumTel,
                                                              customerZipCode,
                                                              codeArticle,
                                                              idCompany,
                                                              pageable)
                                                              .map(CommandeClientDto::fromEntity);

    log.info(
            "readOrdersByFilters end ok - code: {} - status: {} - email: {}  - since: {} - until: {} ",
            code, orderStatus,  customerEmail, fromCreationDate,toCreationDate);


    log.trace("readOrdersByFilters end ok - orders: {}", orders);

    return orders;
  }

  @Override
  public List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
    return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande).stream()
        .map(LigneCommandeClientDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
    checkIdCommande(idCommande);
    if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
      log.error("L'etat de la commande client is NULL");
      throw new InvalidOperationException(UPDATE_CUSTOMER_ORDER_WITHOUT_STATUS_MESSAGE,
          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }

    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
    /* Checking status */
    validEtatCommande(commandeClient.getEtatCommande(), etatCommande);

    commandeClient.setEtatCommande(etatCommande);

    CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient));
    if (commandeClient.isCommandeLivree()) {
      updateMvtStk(idCommande);
    }

    return CommandeClientDto.fromEntity(savedCmdClt);
  }

  private void validEtatCommande(EtatCommande originState, EtatCommande targetState) {

    if (originState != EtatCommande.VALIDEE && targetState == EtatCommande.LIVREE) {
      throw new InvalidOperationException("Veuillez valider la commande avant livraison",
              ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }

    if (targetState == EtatCommande.EN_PREPARATION) {
      throw new InvalidOperationException("Impossible de modifier la commande a l'etat EN_PREPARATION",
              ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
  }

  @Override
  public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {

    checkIdCommande(idCommande);

    checkIdLigneCommande(idLigneCommande);

    if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
      log.error("L'ID de la ligne commande is NULL");
      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }

    CommandeClientDto commandeClient = checkEtatCommande(idCommande);

    Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);

    LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();

    ligneCommandeClient.setQuantite(quantite);

    ligneCommandeClientRepository.save(ligneCommandeClient);

    return commandeClient;
  }

  @Override
  public CommandeClientDto updateClient(Integer idCommande, Integer idClient) {
    checkIdCommande(idCommande);
    if (idClient == null) {
      log.error("L'ID du client is NULL");
      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID client null",
          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
    var clientDto = clientService.findById(idClient);
   /*
    Optional<Client> clientOptional = clientRepository.findById(idClient);
    if (clientOptional.isEmpty()) {
      throw new EntityNotFoundException(
          "Aucun client n'a ete trouve avec l'ID " + idClient, ErrorCodes.CLIENT_NOT_FOUND);
    }
    commandeClient.setClient(ClientDto.fromEntity(clientOptional.get()));
    */
    commandeClient.setClient(clientDto);

    return CommandeClientDto.fromEntity(
        commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient))
    );
  }

  @Override
  public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
    checkIdCommande(idCommande);
    checkIdLigneCommande(idLigneCommande);
    checkIdArticle(idArticle, "nouvel");

    CommandeClientDto commandeClient = checkEtatCommande(idCommande);

    Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommandeClient(idLigneCommande);

    var articleDto = articleService.findById(idArticle);
    /*
    Optional<Article> articleOptional = articleRepository.findById(idArticle);

    if (articleOptional.isEmpty()) {
      throw new EntityNotFoundException(
          "Aucune article n'a ete trouve avec l'ID " + idArticle, ErrorCodes.ARTICLE_NOT_FOUND);
    }

    List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
    */

    List<String> errors = ArticleValidator.validate(articleDto);
    if (!errors.isEmpty()) {
      throw new InvalidEntityException("Article invalid", ErrorCodes.ARTICLE_NOT_VALID, errors);
    }

    LigneCommandeClient ligneCommandeClientToSaved = ligneCommandeClient.get();
    //ligneCommandeClientToSaved.setArticle(articleOptional.get());
    ligneCommandeClientToSaved.setArticle(ArticleDto.toEntity(articleDto));
    ligneCommandeClientRepository.save(ligneCommandeClientToSaved);

    return commandeClient;
  }

  @Override
  public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
    checkIdCommande(idCommande);
    checkIdLigneCommande(idLigneCommande);

    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
    // Just to check the LigneCommandeClient and inform the client in case it is absent
    findLigneCommandeClient(idLigneCommande);
    ligneCommandeClientRepository.deleteById(idLigneCommande);

    return commandeClient;
  }

  private CommandeClientDto checkEtatCommande(Integer idCommande) {

    CommandeClientDto commandeClient = findById(idCommande);

    if (commandeClient.isCommandeLivree()) {
      throw new InvalidOperationException(UPDATE_CUSTOMER_ORDER_ALREADY_DELIVERED_MESSAGE, ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }

    return commandeClient;
  }



  private void checkIdCommande(Integer idCommande) {
    if (idCommande == null) {
      log.error("Commande client ID is NULL");
      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
  }

  private void checkIdLigneCommande(Integer idLigneCommande) {
    if (idLigneCommande == null) {
      log.error("L'ID de la ligne commande is NULL");
      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
  }

  private void checkIdArticle(Integer idArticle, String msg) {
    if (idArticle == null) {
      log.error("L'ID de " + msg + " is NULL");
      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
  }

  private void updateMvtStk(Integer idCommande) {
    List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
    ligneCommandeClients.forEach(lig -> {
      effectuerSortie(lig);
    });
  }

  private void effectuerSortie(LigneCommandeClient lig) {
    MvtStkDto mvtStkDto = MvtStkDto.builder()
        .article(ArticleDto.fromEntity(lig.getArticle()))
        .dateMvt(Instant.now())
        .typeMvt(TypeMvtStk.SORTIE)
        .sourceMvt(SourceMvtStk.COMMANDE_CLIENT)
        .quantite(lig.getQuantite())
        .idEntreprise(lig.getIdEntreprise())
        .build();
    mvtStkService.sortieStock(mvtStkDto);
  }
}
