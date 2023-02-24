package com.talixmines.management.inventorymanager.controller.api;

import static com.talixmines.management.utils.Constants.APP_INVENTORY_MANAGER_ROOT;
import static com.talixmines.management.utils.Constants.APP_ROOT;

import com.talixmines.management.inventorymanager.dto.CategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api("categories")
public interface CategoryApi {

  @ApiOperation(value = "Enregistrer une categorie", notes = "Cette methode permet d'enregistrer ou modifier une categorie", response =
          CategoryDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "L'objet category cree / modifie"),
          @ApiResponse(code = 400, message = "L'objet category n'est pas valide")
  })
  @PostMapping(value = APP_INVENTORY_MANAGER_ROOT + "/categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  CategoryDto save(@RequestBody CategoryDto dto);

  @ApiOperation(value = "Rechercher une categorie par ID", notes = "Cette methode permet de chercher une categorie par son ID", response =
          CategoryDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "La categorie a ete trouve dans la BDD"),
          @ApiResponse(code = 404, message = "Aucune categorie n'existe dans la BDD avec l'ID fourni")
  })
  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/categories/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
  CategoryDto findById(@PathVariable("idCategory") Integer idCategory);

  @ApiOperation(value = "Rechercher une categorie par CODE", notes = "Cette methode permet de chercher une categorie par son CODE", response =
          CategoryDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
          @ApiResponse(code = 404, message = "Aucun article n'existe dans la BDD avec le CODE fourni")
  })
  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/categories/code/{codeCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
  CategoryDto findByCode(@PathVariable("codeCategory") String codeCategory);

  @ApiOperation(value = "Renvoi la liste des categories", notes = "Cette methode permet de chercher et renvoyer la liste des categories qui existent "
          + "dans la BDD", responseContainer = "List<CategoryDto>")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "La liste des article / Une liste vide")
  })
  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
  List<CategoryDto> findAll();

  @ApiOperation(value = "Supprimer un article", notes = "Cette methode permet de supprimer une categorie par ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "La categorie a ete supprime")
  })
  @DeleteMapping(value = APP_INVENTORY_MANAGER_ROOT + "/categories/{idCategory}")
  void delete(@PathVariable("idCategory") Integer id);

}
