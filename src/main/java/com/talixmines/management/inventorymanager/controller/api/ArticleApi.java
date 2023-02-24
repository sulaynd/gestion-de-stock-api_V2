package com.talixmines.management.inventorymanager.controller.api;

import static com.talixmines.management.utils.Constants.APP_INVENTORY_MANAGER_ROOT;

import com.talixmines.management.inventorymanager.dto.ArticleDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeClientDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeFournisseurDto;
import com.talixmines.management.inventorymanager.dto.LigneVenteDto;
import io.swagger.annotations.*;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api("articles")
public interface ArticleApi {

  @ApiOperation(value = "Enregistrer un article", notes = "Cette methode permet d'enregistrer ou modifier un article", response = ArticleDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "L'objet article cree / modifie"),
         // @ApiResponse(code = 400, message = "L'objet article n'est pas valide")
          @ApiResponse(code = 400, message = "Request sent by the client was syntactically incorrect"),
          @ApiResponse(code = 404, message = "Resource access does not exist"),
          @ApiResponse(code = 500, message = "Internal server error during request processing")
  })
  @PostMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ArticleDto save(@RequestBody ArticleDto dto);

  @ApiOperation(value = "Rechercher un article par ID", notes = "Cette methode permet de chercher un article par son ID", response = ArticleDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
          //@ApiResponse(code = 404, message = "Aucun article n'existe dans la BDD avec l'ID fourni")
          @ApiResponse(code = 400, message = "Request sent by the client was syntactically incorrect"),
          @ApiResponse(code = 404, message = "Resource access does not exist"),
          @ApiResponse(code = 500, message = "Internal server error during request processing")
  })
  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
  ArticleDto findById(@PathVariable("idArticle") Integer id);

  @ApiOperation(value = "Rechercher un article par CODE", notes = "Cette methode permet de chercher un article par son CODE", response =
          ArticleDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
          @ApiResponse(code = 404, message = "Aucun article n'existe dans la BDD avec le CODE fourni")
  })
  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles/code/{codeArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
  ArticleDto findByCodeArticle(@PathVariable("codeArticle") String codeArticle);

  @ApiOperation(value = "Renvoi la liste des articles", notes = "Cette methode permet de chercher et renvoyer la liste des articles qui existent "
          + "dans la BDD", responseContainer = "List<ArticleDto>")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "La liste des article / Une liste vide"),
          @ApiResponse(code = 400, message = "Request sent by the client was syntactically incorrect"),
          @ApiResponse(code = 404, message = "Resource access does not exist"),
          @ApiResponse(code = 500, message = "Internal server error during request processing")
  })
  /*@GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
  List<ArticleDto> findAll();*/

  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles/historique/vente/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<LigneVenteDto> findHistoriqueVentes(@PathVariable("idArticle") Integer idArticle);

  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles/historique/commandeclient/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<LigneCommandeClientDto> findHistoriaueCommandeClient(@PathVariable("idArticle") Integer idArticle);

  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles/historique/commandefournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(@PathVariable("idArticle") Integer idArticle);

  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/categories/{idCategory}/articles", produces = MediaType.APPLICATION_JSON_VALUE)
  List<ArticleDto> findAllArticleByIdCategory(@PathVariable("idCategory") Integer idCategory);

  @ApiOperation(value = "Supprimer un article", notes = "Cette methode permet de supprimer un article par ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "L'article a ete supprime"),
          @ApiResponse(code = 400, message = "Request sent by the client was syntactically incorrect"),
          @ApiResponse(code = 404, message = "Resource access does not exist"),
          @ApiResponse(code = 500, message = "Internal server error during request processing")
  })
  @DeleteMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles/{idArticle}")
  void delete(@PathVariable("idArticle") Integer id);


  @ApiOperation(value = "Read paginated articles by filters", notes = "Read paginated articles in the Talix Mines Management by filters")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
          @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
          @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                  value = "Sorting criteria in the format: property(,asc|desc). " + "Default sort order is ascending. " + "Multiple sort criteria are supported.")})
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Success"),
          @ApiResponse(code = 400, message = "Request sent by the client was syntactically incorrect"),
          @ApiResponse(code = 404, message = "Resource access does not exist"),
          @ApiResponse(code = 500, message = "Internal server error during request processing")
  })
  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
  Page<ArticleDto> readArticles(
          @ApiParam(value = "Article designation", required = false) @RequestParam(value = "designation", required = false) String designation,
          @ApiParam(value = "Category identifier", required = false) @RequestParam(value = "categoryId", required = false) Integer categoryId,
          @ApiParam(value = "Creation date from", required = false) @RequestParam(value = "creationDateFrom", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") Instant creationDateFrom,
          @ApiParam(value = "Creation date to", required = false) @RequestParam(value = "creationDateTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") Instant creationDateTo,
          Pageable pageable);

  @ApiOperation(value = "Read paginated articles for a category identifier by filters", notes = "Read paginated articles for a category identifier in the Talix Mines Management by filters")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
          @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
          @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). " + "Default sort order is ascending. " + "Multiple sort criteria are supported.")})
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Success"),
          @ApiResponse(code = 400, message = "Request sent by the client was syntactically incorrect"),
          @ApiResponse(code = 404, message = "Resource access does not exist"),
          @ApiResponse(code = 500, message = "Internal server error during request processing")})
  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/category/{categoryId}/articles", produces = MediaType.APPLICATION_JSON_VALUE)
  Page<ArticleDto> readArticlesByCategoryCode(
          @ApiParam(value = "Category identifier", required = true) @PathVariable(value = "categoryId") Integer categoryId,
          Pageable pageable);



}
