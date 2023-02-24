package com.talixmines.management.inventorymanager.services.impl;

import com.talixmines.management.exception.*;
import com.talixmines.management.inventorymanager.dto.CategoryDto;
import com.talixmines.management.inventorymanager.model.Article;
import com.talixmines.management.inventorymanager.repository.ArticleRepository;
import com.talixmines.management.inventorymanager.repository.CategoryRepository;
import com.talixmines.management.inventorymanager.services.CategoryService;
import com.talixmines.management.inventorymanager.validator.CategoryValidator;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

  static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found with the id: {0}";
  static final String CATEGORY_CODE_NOT_FOUND_MESSAGE = "Category not found with the code: {0}";
  static final String CATEGORY_NOT_VALID_MESSAGE = "Category is not valid";
  static final String CATEGORY_REMOVAL_UNAUTHORIZED_MESSAGE = "Category removal unauthorized for id: {0}";
  private final CategoryRepository categoryRepository;
  private final ArticleRepository articleRepository;

  @Autowired
  public CategoryServiceImpl(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
    this.categoryRepository = categoryRepository;
    this.articleRepository = articleRepository;
  }

  @Override
  public CategoryDto save(CategoryDto dto) {
    List<String> errors = CategoryValidator.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Article is not valid {}", dto);
      throw new InvalidEntityException(CATEGORY_NOT_VALID_MESSAGE, ErrorCodes.CATEGORY_NOT_VALID, errors);
    }

    /* Saving entity */
    CategoryDto savedCategory =  CategoryDto.fromEntity(
        categoryRepository.save(CategoryDto.toEntity(dto))
    );

    log.info("saveCategory end ok - categoryId: {}", savedCategory.getId());
    log.trace("saveCategory end ok - category: {}", savedCategory);

    return savedCategory;

  }

  @Override
  public CategoryDto findById(Integer id) {
    if (id == null) {
      log.error("Category ID is null");
      return null;
    }
    CategoryDto category =  categoryRepository.findById(id)
        .map(CategoryDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format(CATEGORY_NOT_FOUND_MESSAGE, id),
            ErrorCodes.CATEGORY_NOT_FOUND)
        );
    log.info("readCategoryById end ok - categoryId: {}", id);
    log.trace("readCategoryById end ok - category: {}", category);

    return category;
  }

  @Override
  public CategoryDto findByCode(String code) {
    if (!StringUtils.hasLength(code)) {
      log.error("Category CODE is null");
      return null;
    }
    CategoryDto category = categoryRepository.findCategoryByCode(code)
        .map(CategoryDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format(CATEGORY_CODE_NOT_FOUND_MESSAGE, code),
            ErrorCodes.CATEGORY_NOT_FOUND)
        );

    log.info("readCategoryByCode end ok - code: {}", code);
    log.trace("readCategoryByCode end ok - category: {}", category);

    return category;
  }

  @Override
  public List<CategoryDto> findAll() {
    return categoryRepository.findAll().stream()
        .map(CategoryDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Category ID is null");
      return;
    }
    /* Getting Category */
    CategoryDto category = findById(id);

    List<Article> articles = articleRepository.findAllByCategoryId(id);
    if (!articles.isEmpty()) {
      throw new InvalidOperationException(MessageFormat.format(CATEGORY_REMOVAL_UNAUTHORIZED_MESSAGE, id),
          ErrorCodes.CATEGORY_ALREADY_IN_USE);
    }
    categoryRepository.deleteById(id);

    log.info("deleteCategory end ok - categoryId: {}", id);
    log.trace("deleteCategory end ok - category: {}", category);

  }
}
