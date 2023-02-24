package com.talixmines.management.inventorymanager.repository;

import com.talixmines.management.inventorymanager.model.CommandeClient;
import com.talixmines.management.inventorymanager.model.CommandeFournisseur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Integer> {

  Optional<CommandeFournisseur> findCommandeFournisseurByCode(String code);

  List<CommandeClient> findAllByFournisseurId(Integer id);
}
