package com.talixmines.management.inventorymanager.repository;

import com.talixmines.management.inventorymanager.model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  Optional<Category> findCategoryByCode(String code);

  boolean existsByCode(String code);

}
