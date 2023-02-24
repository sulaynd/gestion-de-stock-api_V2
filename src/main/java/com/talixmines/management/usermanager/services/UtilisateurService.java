package com.talixmines.management.usermanager.services;

import com.talixmines.management.inventorymanager.dto.ChangerMotDePasseUtilisateurDto;
import com.talixmines.management.usermanager.dto.UtilisateurDto;
import java.util.List;

public interface UtilisateurService {

  UtilisateurDto save(UtilisateurDto dto);

  UtilisateurDto findById(Integer id);

  List<UtilisateurDto> findAll();

  void delete(Integer id);

  UtilisateurDto findByEmail(String email);

  UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto);


}
