package com.talixmines.management.inventorymanager.dto;

import com.talixmines.management.inventorymanager.model.CommandeClient;
import com.talixmines.management.inventorymanager.model.EtatCommande;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeClientDto {

  private Integer id;

  private String code;

  private Instant dateCommande;

  private EtatCommande etatCommande;

  private ClientDto client;

  private Integer idEntreprise;

  private List<LigneCommandeClientDto> ligneCommandeClients;

  public static CommandeClientDto fromEntity(CommandeClient commandeClient) {
    if (commandeClient == null) {
      return null;
    }
    return CommandeClientDto.builder()
        .id(commandeClient.getId())
        .code(commandeClient.getCode())
        .dateCommande(commandeClient.getDateCommande())
        .etatCommande(commandeClient.getEtatCommande())
        .client(ClientDto.fromEntity(commandeClient.getClient()))
        .ligneCommandeClients(getLigneCommandeClients(commandeClient))
        .idEntreprise(commandeClient.getIdEntreprise())
        .build();

  }

  public static CommandeClient toEntity(CommandeClientDto dto) {
    if (dto == null) {
      return null;
    }
    CommandeClient commandeClient = new CommandeClient();
    commandeClient.setId(dto.getId());
    commandeClient.setCode(dto.getCode());
    commandeClient.setClient(ClientDto.toEntity(dto.getClient()));
    commandeClient.setDateCommande(dto.getDateCommande());
    commandeClient.setEtatCommande(dto.getEtatCommande());
    commandeClient.setIdEntreprise(dto.getIdEntreprise());
    return commandeClient;
  }

  public static List<LigneCommandeClientDto> getLigneCommandeClients(CommandeClient commandeClient) {
    List<LigneCommandeClientDto> ligneCommandeClients = new ArrayList<>();
    if (Objects.nonNull(commandeClient)) {
      commandeClient.getLigneCommandeClients().forEach(
              source ->
                      ligneCommandeClients.add(LigneCommandeClientDto.fromEntity(source))
      );
    }
    return ligneCommandeClients;
  }
  public boolean isCommandeLivree() {
    return EtatCommande.LIVREE.equals(this.etatCommande);
  }
}
