package com.talixmines.management.usermanager.services;

import com.talixmines.management.usermanager.dto.EntrepriseDto;
import java.util.List;

public interface EntrepriseService {

  EntrepriseDto save(EntrepriseDto dto);

  EntrepriseDto findById(Integer id);

  List<EntrepriseDto> findAll();

  void delete(Integer id);

}
