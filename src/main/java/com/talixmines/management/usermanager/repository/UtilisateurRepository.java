package com.talixmines.management.usermanager.repository;

import com.talixmines.management.usermanager.model.Utilisateur;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

  // JPQL query
 // @Query(value = "select u from Utilisateur u where u.email = :email")
 // Optional<Utilisateur> findUtilisateurByEmail(@Param("email") String email);

  Optional<Utilisateur> findByEmail(String email);

}
