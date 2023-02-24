package com.talixmines.management.utils;

import com.talixmines.management.inventorymanager.dto.*;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import com.talixmines.management.inventorymanager.model.LigneCommandeClient;
import com.talixmines.management.usermanager.dto.AdresseDto;
import com.talixmines.management.usermanager.dto.EntrepriseDto;
import com.talixmines.management.usermanager.dto.UtilisateurDto;
import com.talixmines.management.usermanager.dto.auth.AuthenticationRequest;

import java.math.BigDecimal;
import java.time.Instant;

public final class ConstantUtils {

    public static final String ID = "356486";

    public static EntrepriseDto getEntreprise() {
        EntrepriseDto entreprise = EntrepriseDto.builder()
                .codeFiscal("12344")
                .description("talix-mines")
                .adresse(getAdresse())
                .numTel("numtel")
                .email("company@gmail.com")
                .steWeb("url.com")
                .build();

        return entreprise;
    }
    public static ArticleDto getArticle() {
        ArticleDto article = ArticleDto.builder()
                .codeArticle("ART-X")
                .designation("Article test")
                .photo("photo")
                .prixUnitaireHt(BigDecimal.valueOf(5))
                .prixUnitaireTtc(BigDecimal.valueOf(5))
                .tauxTva(BigDecimal.valueOf(0.18))
                .idEntreprise(3)
                .build();

        return article;
    }

    public static CategoryDto getCategory() {
        CategoryDto category = CategoryDto.builder()
                .code("CAT-X")
                .designation("Designation test")
                .idEntreprise(1)
                .build();

        return category;
    }

    public static ClientDto getClient() {
        ClientDto client = ClientDto.builder()
                .nom("Test")
                .prenom("Test")
                .numTel("numTel")
                .mail("test@mail.com")
                .idEntreprise(3)
                .adresse(getAdresse())
                .photo("photo")
                .build();

        return client;
    }

    public static CommandeClientDto getCommandeClient() {
        CommandeClientDto commandeClient = CommandeClientDto.builder()
                .code("COMM-X")
                .etatCommande(EtatCommande.EN_PREPARATION)
                .dateCommande(Instant.now())
                .idEntreprise(3)
                .build();

        return commandeClient;
    }

    public static LigneCommandeClientDto getLigneCommandeClient() {
        LigneCommandeClientDto ligneCommandeClient = LigneCommandeClientDto.builder()
                .article(getArticle())
                .quantite(BigDecimal.valueOf(2))
                .prixUnitaire(BigDecimal.valueOf(5))
                .build();

        return ligneCommandeClient;
    }

    public static AuthenticationRequest getAuthenticationRequest() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .login("user@email.com")
                .password("password")
                .build();

        return request;
    }


    public static UtilisateurDto getUtilisateur() {
        UtilisateurDto user = UtilisateurDto.builder()
                .email("joe@gmail.com")
                .moteDePasse("password")
                .nom("test")
                .prenom("test")
                .dateDeNaissance(Instant.now())
                .adresse(getAdresse())
                .photo("photo")
                .build();

        return user;
    }

    public static AdresseDto getAdresse() {
        AdresseDto adresse = AdresseDto.builder()
                .adresse1("adresse1")
                .adresse2("adresse2")
                .ville("Dakar")
                .pays("Senegal")
                .codePostale("12345")
                .build();

        return adresse;
    }

    public ConstantUtils() {
        // prevent from instantiation
    }
}
