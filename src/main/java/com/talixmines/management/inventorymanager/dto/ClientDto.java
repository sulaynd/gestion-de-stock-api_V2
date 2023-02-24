package com.talixmines.management.inventorymanager.dto;

import com.talixmines.management.inventorymanager.model.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.talixmines.management.inventorymanager.model.CommandeClient;
import com.talixmines.management.usermanager.dto.AdresseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

  private Integer id;

  private String nom;

  private String prenom;

  private AdresseDto adresse;

  private String photo;

  private String mail;

  private String numTel;

  private Integer idEntreprise;

  @JsonIgnore
  private List<CommandeClientDto> commandeClients;

  public static ClientDto fromEntity(Client client) {
    if (client == null) {
      return null;
    }
    return ClientDto.builder()
        .id(client.getId())
        .nom(client.getNom())
        .prenom(client.getPrenom())
        .adresse(AdresseDto.fromEntity(client.getAdresse()))
        .photo(client.getPhoto())
        .mail(client.getMail())
        .numTel(client.getNumTel())
        .idEntreprise(client.getIdEntreprise())
        //.commandeClients(getCustomerOrders(client))
        .build();
  }

  public static Client toEntity(ClientDto dto) {
    if (dto == null) {
      return null;
    }
    Client client = new Client();
    client.setId(dto.getId());
    client.setNom(dto.getNom());
    client.setPrenom(dto.getPrenom());
    client.setAdresse(AdresseDto.toEntity(dto.getAdresse()));
    client.setPhoto(dto.getPhoto());
    client.setMail(dto.getMail());
    client.setNumTel(dto.getNumTel());
    client.setIdEntreprise(dto.getIdEntreprise());
    return client;
  }

  /*
  public static List<CommandeClientDto> getCustomerOrders(Client client) {
    List<CommandeClientDto> customersOrders = new ArrayList<>();
    if (Objects.nonNull(client)) {
      client.getCommandeClients().forEach(
              order ->
                      customersOrders.add(CommandeClientDto.fromEntity(order))
      );
    }
    return customersOrders;
  }*/
}
