package com.talixmines.management.inventorymanager.controller;

import com.talixmines.management.inventorymanager.controller.api.CommandeClientApi;
import com.talixmines.management.inventorymanager.dto.ArticleDto;
import com.talixmines.management.inventorymanager.dto.CommandeClientDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeClientDto;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import com.talixmines.management.inventorymanager.services.CommandeClientService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandeClientController implements CommandeClientApi {

  private CommandeClientService commandeClientService;

  @Autowired
  public CommandeClientController(CommandeClientService commandeClientService) {
    this.commandeClientService = commandeClientService;
  }

  @Override
  public ResponseEntity<CommandeClientDto> save(CommandeClientDto dto) {
    return ResponseEntity.ok(commandeClientService.save(dto));
  }

  @Override
  public ResponseEntity<CommandeClientDto> updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
    return ResponseEntity.ok(commandeClientService.updateEtatCommande(idCommande, etatCommande));
  }

  @Override
  public ResponseEntity<CommandeClientDto> updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
    return ResponseEntity.ok(commandeClientService.updateQuantiteCommande(idCommande, idLigneCommande, quantite));
  }

  @Override
  public ResponseEntity<CommandeClientDto> updateClient(Integer idCommande, Integer idClient) {
    return ResponseEntity.ok(commandeClientService.updateClient(idCommande, idClient));
  }

  @Override
  public ResponseEntity<CommandeClientDto> updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
    return ResponseEntity.ok(commandeClientService.updateArticle(idCommande, idLigneCommande, idArticle));
  }

  @Override
  public ResponseEntity<CommandeClientDto> deleteArticle(Integer idCommande, Integer idLigneCommande) {
    return ResponseEntity.ok(commandeClientService.deleteArticle(idCommande, idLigneCommande));
  }

  @Override
  public ResponseEntity<CommandeClientDto> findById(Integer id) {
    return ResponseEntity.ok(commandeClientService.findById(id));
  }

  @Override
  public ResponseEntity<CommandeClientDto> findByCode(String code) {
    return ResponseEntity.ok(commandeClientService.findByCode(code));
  }

 /* @Override
  public ResponseEntity<List<CommandeClientDto>> findAll() {
    return ResponseEntity.ok(commandeClientService.findAll());
  }*/

  @Override
  public ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
    return ResponseEntity.ok(commandeClientService.findAllLignesCommandesClientByCommandeClientId(idCommande));
  }

  @Override
  public ResponseEntity<Void> delete(Integer id) {
    commandeClientService.delete(id);
    return ResponseEntity.ok().build();
  }

  @Override
  public Page<CommandeClientDto> readCustomerOrders(String code, Instant creationDateFrom, Instant creationDateTo, EtatCommande orderStatus,List<EtatCommande> exceptStatuses, String customerEmail, String customerFirstName, String customerLastName, String customerNumTel, String customerZipCode, String codeArticle, Integer companyId, Pageable pageable) {
    return commandeClientService.readOrdersByFilters(code, creationDateFrom, creationDateTo, orderStatus, exceptStatuses, customerEmail, customerFirstName, customerLastName, customerNumTel, customerZipCode, codeArticle, companyId, pageable);
  }
}
