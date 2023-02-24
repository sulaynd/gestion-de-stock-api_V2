package com.talixmines.management.inventorymanager.repository;

import com.talixmines.management.inventorymanager.model.Article;
import com.talixmines.management.inventorymanager.model.CommandeClient;
import java.util.List;
import java.util.Optional;

import com.talixmines.management.inventorymanager.repository.custom.ArticleRepositoryCustom;
import com.talixmines.management.inventorymanager.repository.custom.CommandeClientRepositoryCuston;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommandeClientRepository extends JpaRepository<CommandeClient, Integer> , CommandeClientRepositoryCuston, QuerydslPredicateExecutor<CommandeClient> {

  Optional<CommandeClient> findCommandeClientByCode(String code);

  List<CommandeClient> findAllByClientId(Integer id);
}
