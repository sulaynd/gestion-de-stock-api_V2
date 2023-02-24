package com.talixmines.management.usermanager.controller.api;

import static com.talixmines.management.utils.Constants.ENTREPRISE_ENDPOINT;

import com.talixmines.management.usermanager.dto.EntrepriseDto;
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
public interface EntrepriseApi {

  @ApiOperation(value = "Create a company", notes = "Create a company in the User Manager")
  @PostMapping(ENTREPRISE_ENDPOINT + "/create")
  EntrepriseDto save(@RequestBody EntrepriseDto dto);

  @ApiOperation(value = "Read a company by his identifier", notes = "Read a company in the User Manager by his identifier")
  @GetMapping(ENTREPRISE_ENDPOINT + "/{idEntreprise}")
  EntrepriseDto findById(@PathVariable("idEntreprise") Integer id);

  @ApiOperation(value = "Read all companies", notes = "Read all companies in the User Manager")
  @GetMapping(ENTREPRISE_ENDPOINT + "/all")
  List<EntrepriseDto> findAll();

  @ApiOperation(value = "Delete a company", notes = "Delete a company in the User Manager")
  @DeleteMapping(ENTREPRISE_ENDPOINT + "/delete/{idEntreprise}")
  void delete(@PathVariable("idEntreprise") Integer id);

}
