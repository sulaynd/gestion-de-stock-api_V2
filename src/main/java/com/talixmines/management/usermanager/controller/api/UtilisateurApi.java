package com.talixmines.management.usermanager.controller.api;


import static com.talixmines.management.utils.Constants.UTILISATEUR_ENDPOINT;

import com.talixmines.management.inventorymanager.dto.ChangerMotDePasseUtilisateurDto;
import com.talixmines.management.usermanager.dto.UtilisateurDto;
import com.talixmines.management.utils.Constants;
import io.swagger.annotations.Api;
import java.util.List;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api(tags = Constants.TAG_USER_MANAGEMENT)
public interface UtilisateurApi {

  @ApiOperation(
          value = Constants.ACTION_CREATE_USER,
          tags = { Constants.TAG_USER_MANAGEMENT },
          notes = "Create a user in the Talix Mines Management"
  )
  @PostMapping(UTILISATEUR_ENDPOINT)
  UtilisateurDto save(@RequestBody UtilisateurDto dto);

  @ApiOperation(
          value = Constants.ACTION_UPDATE_USER,
          tags = { Constants.TAG_USER_MANAGEMENT },
          notes = "Update a user in the Talix Mines Management"
  )
  @PostMapping(UTILISATEUR_ENDPOINT + "/update/password")
  UtilisateurDto changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto dto);

  @ApiOperation(
          value = Constants.ACTION_READ_USER_BY_ID,
          tags = { Constants.TAG_USER_MANAGEMENT },
          notes = "Read a user in the Talix Mines Management by his identifier"
  )
  @GetMapping(UTILISATEUR_ENDPOINT + "/{idUtilisateur}")
  UtilisateurDto findById(@PathVariable("idUtilisateur") Integer id);

  @ApiOperation(
          value = Constants.ACTION_READ_USER_BY_EMAIL,
          tags = { Constants.TAG_USER_MANAGEMENT },
          notes = "Read a user in the Talix Mines Management by his email"
  )
  @GetMapping(UTILISATEUR_ENDPOINT + "/email/{email:.+}")
  UtilisateurDto findByEmail(@PathVariable("email") String email);

  @ApiOperation(
          value = Constants.ACTION_READ_USERS,
          tags = { Constants.TAG_USER_MANAGEMENT },
          notes = "Read all users in the Talix Mines Management"
  )
  @GetMapping(UTILISATEUR_ENDPOINT)
  List<UtilisateurDto> findAll();

  @ApiOperation(
          value = Constants.ACTION_DELETE_USER,
          tags = { Constants.TAG_USER_MANAGEMENT },
          notes = "Delete a user in the Talix Mines Management"
  )
  @DeleteMapping(UTILISATEUR_ENDPOINT + "/{idUtilisateur}")
  void delete(@PathVariable("idUtilisateur") Integer id);

}
