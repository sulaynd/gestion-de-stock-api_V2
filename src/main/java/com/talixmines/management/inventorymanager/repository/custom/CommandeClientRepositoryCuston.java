package com.talixmines.management.inventorymanager.repository.custom;

import com.talixmines.management.inventorymanager.model.CommandeClient;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import com.talixmines.management.inventorymanager.model.Fournisseur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface CommandeClientRepositoryCuston {

    Page<CommandeClient> findByFilter(
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
