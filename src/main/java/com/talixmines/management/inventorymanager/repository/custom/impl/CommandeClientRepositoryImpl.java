package com.talixmines.management.inventorymanager.repository.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.talixmines.management.inventorymanager.model.CommandeClient;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import com.talixmines.management.inventorymanager.model.QCommandeClient;
import com.talixmines.management.inventorymanager.repository.CommandeClientRepository;
import com.talixmines.management.inventorymanager.repository.custom.CommandeClientRepositoryCuston;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;

public class CommandeClientRepositoryImpl implements CommandeClientRepositoryCuston {

    @Autowired
    CommandeClientRepository commandeClientRepository;

    @Override
    public Page<CommandeClient> findByFilter(String code, Instant fromCreationDate, Instant toCreationDate, EtatCommande orderStatus,List<EtatCommande> exceptStatuses, String customerEmail, String customerFirstName, String customerLastName, String customerNumTel, String customerZipCode, String codeArticle, Integer idCompany, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if(code != null) {
            builder.and(QCommandeClient.commandeClient.code.eq(code));
        }

        if (fromCreationDate != null || toCreationDate != null) {
            builder.and(QCommandeClient.commandeClient.creationDate.between(fromCreationDate, toCreationDate));
        }
        if(orderStatus != null) {
            builder.and(QCommandeClient.commandeClient.etatCommande.eq(orderStatus));
        }

        if (CollectionUtils.isNotEmpty(exceptStatuses)) {
            builder.and(QCommandeClient.commandeClient.etatCommande.notIn(exceptStatuses));
        }

        if(customerEmail != null) {
            builder.and(QCommandeClient.commandeClient.client.mail.eq(customerEmail));
        }

        if (StringUtils.isNotEmpty(customerLastName)) {
            builder.and(QCommandeClient.commandeClient.client.nom.containsIgnoreCase(customerLastName));
        }

        if (StringUtils.isNotEmpty(customerFirstName)) {
            builder.and(QCommandeClient.commandeClient.client.prenom.containsIgnoreCase(customerFirstName));
        }

        if (StringUtils.isNotEmpty(customerNumTel)) {
            builder.and(QCommandeClient.commandeClient.client.numTel.containsIgnoreCase(customerNumTel));
        }

        if (StringUtils.isNotEmpty(customerZipCode)) {
            builder.and(QCommandeClient.commandeClient.client.adresse.codePostale.eq(customerZipCode));
        }

        if (StringUtils.isNotEmpty(codeArticle)) {
            builder.and(QCommandeClient.commandeClient.ligneCommandeClients.any().article.codeArticle.eq(codeArticle));
        }
        if(idCompany != null) {
            builder.and(QCommandeClient.commandeClient.idEntreprise.eq(idCompany));
        }

        return commandeClientRepository.findAll(builder, pageable);
    }
}
