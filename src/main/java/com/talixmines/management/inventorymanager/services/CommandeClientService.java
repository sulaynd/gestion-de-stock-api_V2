package com.talixmines.management.inventorymanager.services;

import com.talixmines.management.inventorymanager.dto.CommandeClientDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeClientDto;
import com.talixmines.management.inventorymanager.model.CommandeClient;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import com.talixmines.management.inventorymanager.model.LigneCommandeClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CommandeClientService {

  CommandeClientDto save(CommandeClientDto dto);

  CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

  CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

  CommandeClientDto updateClient(Integer idCommande, Integer idClient);

  CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

  // Delete article ==> delete LigneCommandeClient
  CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande);

  CommandeClientDto findById(Integer id);

  CommandeClientDto findByCode(String code);

  List<CommandeClientDto> findAll();

  List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande);

  void delete(Integer id);

  Optional<LigneCommandeClient> findLigneCommandeClient(Integer idLigneCommande);

  Page<CommandeClientDto> readOrdersByFilters(
          String code,
          Instant fromCreationDate,
          Instant toCreationDate,
          EtatCommande orderStatus,
          List<EtatCommande> exceptStatuses,
          String customerEmail,
          String customerFirstName,
          String customerLastName,
          String customerNumTel,
          String customerZipCode,
          String codeArticle,
          Integer idCompany,
          Pageable pageable);
}
