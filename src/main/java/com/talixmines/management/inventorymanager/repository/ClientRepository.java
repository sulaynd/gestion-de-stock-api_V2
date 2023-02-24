package com.talixmines.management.inventorymanager.repository;

import com.talixmines.management.inventorymanager.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
