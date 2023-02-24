package com.talixmines.management.inventorymanager.controller.api;

import static com.talixmines.management.utils.Constants.APP_INVENTORY_MANAGER_ROOT;
import static com.talixmines.management.utils.Constants.APP_ROOT;

import com.talixmines.management.inventorymanager.dto.ArticleDto;
import com.talixmines.management.inventorymanager.dto.CommandeClientDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeClientDto;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import io.swagger.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api("commandesclients")
public interface CommandeClientApi {


  @PostMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/create")
  ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto);

  @PatchMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/update/etat/{idCommande}/{etatCommande}")
  ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande);

  @PatchMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
  ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite);

  @PatchMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/update/client/{idCommande}/{idClient}")
  ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Integer idCommande, @PathVariable("idClient") Integer idClient);

  @PatchMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
  ResponseEntity<CommandeClientDto> updateArticle(@PathVariable("idCommande") Integer idCommande,
      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle);

  @DeleteMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/delete/article/{idCommande}/{idLigneCommande}")
  ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande);

  @GetMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/{idCommandeClient}")
  ResponseEntity<CommandeClientDto> findById(@PathVariable Integer idCommandeClient);

  @GetMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/filter/{codeCommandeClient}")
  ResponseEntity<CommandeClientDto> findByCode(@PathVariable("codeCommandeClient") String code);

  /*
  @GetMapping(APP_ROOT + "/commandesclients/all")
  ResponseEntity<List<CommandeClientDto>> findAll();
  */

  @GetMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/lignesCommande/{idCommande}")
  ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande);

  @DeleteMapping(APP_INVENTORY_MANAGER_ROOT + "/commandesclients/delete/{idCommandeClient}")
  ResponseEntity<Void> delete(@PathVariable("idCommandeClient") Integer id);

  @ApiOperation(value = "Read paginated customer orders by filters", notes = "Read paginated customer orders in the Talix Mines Management by filters")
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
  @GetMapping(value = APP_INVENTORY_MANAGER_ROOT + "/customer/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  Page<CommandeClientDto> readCustomerOrders(
          @ApiParam(value = "Order code", required = false) @RequestParam(value = "code", required = false) String code,
          @ApiParam(value = "Creation date from", required = false) @RequestParam(value = "creationDateFrom", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") Instant creationDateFrom,
          @ApiParam(value = "Creation date to", required = false) @RequestParam(value = "creationDateTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") Instant creationDateTo,
          @ApiParam(value = "Customer order status", required = false) @RequestParam(value = "orderStatus", required = false) EtatCommande orderStatus,
          @ApiParam(value = "Filter by a list of except statuses order") @RequestParam(name = "exceptStatuses", required = false) final  List<EtatCommande> exceptStatuses,
          @ApiParam(value = "Customer email address", required = false) @RequestParam(value = "customerEmail", required = false) String customerEmail,
          @ApiParam(value = "Customer first name", required = false) @RequestParam(value = "customerFirstName", required = false) String customerFirstName,
          @ApiParam(value = "Customer last name", required = false) @RequestParam(value = "customerLastName", required = false) String customerLastName,
          @ApiParam(value = "Customer phone number", required = false) @RequestParam(value = "customerNumTel", required = false) String customerNumTel,
          @ApiParam(value = "Customer zip code", required = false) @RequestParam(value = "customerZipCode", required = false) String customerZipCode,
          @ApiParam(value = "Code article") @RequestParam(name = "codeArticle", required = false) final  String codeArticle,
          @ApiParam(value = "Company identifier", required = false) @RequestParam(value = "companyId", required = false) Integer companyId,
          Pageable pageable);
}
