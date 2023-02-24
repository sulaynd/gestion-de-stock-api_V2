package com.talixmines.management.inventorymanager.dto;

import com.talixmines.management.inventorymanager.model.LigneCommandeClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommandeClientDto {

  private Integer id;

  private ArticleDto article;

  @JsonIgnore
  private CommandeClientDto commandeClient;

  private BigDecimal quantite;

  private BigDecimal prixUnitaire;

  private Integer idEntreprise;

  public static LigneCommandeClientDto fromEntity(LigneCommandeClient ligneCommandeClient) {
    if (ligneCommandeClient == null) {
      return null;
    }
    return LigneCommandeClientDto.builder()
        .id(ligneCommandeClient.getId())
        .article(ArticleDto.fromEntity(ligneCommandeClient.getArticle()))
         //   .commandeClient(CommandeClientDto.fromEntity(ligneCommandeClient.getCommandeClient()))
        .quantite(ligneCommandeClient.getQuantite())
        .prixUnitaire(ligneCommandeClient.getPrixUnitaire())
        .idEntreprise(ligneCommandeClient.getIdEntreprise())
        .build();
  }

  public static LigneCommandeClient toEntity(LigneCommandeClientDto dto) {
    if (dto == null) {
      return null;
    }

    LigneCommandeClient ligneCommandeClient = new LigneCommandeClient();
    ligneCommandeClient.setId(dto.getId());
    ligneCommandeClient.setArticle(ArticleDto.toEntity(dto.getArticle()));
    ligneCommandeClient.setPrixUnitaire(dto.getPrixUnitaire());
    ligneCommandeClient.setQuantite(dto.getQuantite());
      //  ligneCommandeClient.setCommandeClient(CommandeClientDto.toEntity(dto.getCommandeClient()));
    ligneCommandeClient.setIdEntreprise(dto.getIdEntreprise());
    return ligneCommandeClient;
  }

}
