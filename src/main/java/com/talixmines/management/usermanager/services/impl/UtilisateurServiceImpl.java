package com.talixmines.management.usermanager.services.impl;

import com.talixmines.management.exception.*;
import com.talixmines.management.inventorymanager.dto.CategoryDto;
import com.talixmines.management.inventorymanager.dto.ChangerMotDePasseUtilisateurDto;
import com.talixmines.management.usermanager.dto.UtilisateurDto;
import com.talixmines.management.usermanager.model.Utilisateur;
import com.talixmines.management.usermanager.services.UtilisateurService;
import com.talixmines.management.usermanager.repository.UtilisateurRepository;
import com.talixmines.management.usermanager.validator.UtilisateurValidator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository,
      PasswordEncoder passwordEncoder) {
    this.utilisateurRepository = utilisateurRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UtilisateurDto save(UtilisateurDto dto) {

    List<String> errors = UtilisateurValidator.validate(dto);

    if (!errors.isEmpty()) {
      log.error("Utilisateur is not valid {}", dto);
      throw new InvalidEntityException("L'utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
    }

    if(userAlreadyExists(dto.getEmail())) {
      throw new ResourceAlreadyExistException("Un autre utilisateur avec le meme email " + dto .getEmail() +" existe deja", ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
          Collections.singletonList("Un autre utilisateur avec le meme email " + dto .getEmail() + " existe deja dans la BDD"));
    }


    dto.setMoteDePasse(passwordEncoder.encode(dto.getMoteDePasse()));

    var  createdUser = UtilisateurDto.fromEntity(
        utilisateurRepository.save(
            UtilisateurDto.toEntity(dto)
        )
    );

    log.info("createdUser end ok - userId: {}", createdUser.getId());
    log.trace("createdUser end ok - user: {}", createdUser);

    return createdUser;
  }

  private boolean userAlreadyExists(String email) {
    Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);
    return user.isPresent();
  }

  @Override
  public UtilisateurDto findById(Integer id) {
    if (id == null) {
      log.error("Utilisateur ID is null");
      return null;
    }
    return utilisateurRepository.findById(id)
        .map(UtilisateurDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
            "Aucun utilisateur avec l'ID = " + id + " n' ete trouve dans la BDD",
            ErrorCodes.UTILISATEUR_NOT_FOUND)
        );
  }

  @Override
  public List<UtilisateurDto> findAll() {
    return utilisateurRepository.findAll().stream()
        .map(UtilisateurDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Utilisateur ID is null");
      return;
    }
    /* Getting user */
    var user = findById(id);

    utilisateurRepository.deleteById(id);

    log.info("deleteUser end ok - userId: {}", id);
    log.trace("deleteUser end ok - user: {}", user);
  }

  @Override
  public UtilisateurDto findByEmail(String email) {
    return utilisateurRepository.findByEmail(email)
        .map(UtilisateurDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
        "Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
        ErrorCodes.UTILISATEUR_NOT_FOUND)
    );
  }

  @Override
  public UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) {
    validate(dto);
    Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(dto.getId());
    if (utilisateurOptional.isEmpty()) {
      log.warn("Aucun utilisateur n'a ete trouve avec l'ID " + dto.getId());
      throw new EntityNotFoundException("Aucun utilisateur n'a ete trouve avec l'ID " + dto.getId(), ErrorCodes.UTILISATEUR_NOT_FOUND);
    }

    Utilisateur utilisateur = utilisateurOptional.get();
    utilisateur.setMoteDePasse(passwordEncoder.encode(dto.getMotDePasse()));

    return UtilisateurDto.fromEntity(
        utilisateurRepository.save(utilisateur)
    );
  }

  private void validate(ChangerMotDePasseUtilisateurDto dto) {
    if (dto == null) {
      log.warn("Impossible de modifier le mot de passe avec un objet NULL");
      throw new InvalidOperationException("Aucune information n'a ete fourni pour pouvoir changer le mot de passe",
          ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
    }
    if (dto.getId() == null) {
      log.warn("Impossible de modifier le mot de passe avec un ID NULL");
      throw new InvalidOperationException("ID utilisateur null:: Impossible de modifier le mote de passe",
          ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
    }
    if (!StringUtils.hasLength(dto.getMotDePasse()) || !StringUtils.hasLength(dto.getConfirmMotDePasse())) {
      log.warn("Impossible de modifier le mot de passe avec un mot de passe NULL");
      throw new InvalidOperationException("Mot de passe utilisateur null:: Impossible de modifier le mote de passe",
          ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
    }
    if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
      log.warn("Impossible de modifier le mot de passe avec deux mots de passe different");
      throw new InvalidOperationException("Mots de passe utilisateur non conformes:: Impossible de modifier le mote de passe",
          ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
    }
  }
}
