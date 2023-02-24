package com.talixmines.management.inventorymanager.services.impl;

import com.talixmines.management.exception.EntityNotFoundException;
import com.talixmines.management.exception.ErrorCodes;
import com.talixmines.management.exception.InvalidEntityException;
import com.talixmines.management.exception.InvalidOperationException;
import com.talixmines.management.inventorymanager.dto.CategoryDto;
import com.talixmines.management.inventorymanager.services.CategoryService;
import com.talixmines.management.utils.ConstantUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {
  @Autowired
  private CategoryService service;

  @Test
  public void shouldSaveCategoryWithSuccess() {
    CategoryDto expectedCategory = ConstantUtils.getCategory();
    CategoryDto savedCategory = service.save(expectedCategory);

    assertNotNull(savedCategory);
    assertNotNull(savedCategory.getId());
    assertEquals(expectedCategory.getCode(), savedCategory.getCode());
    assertEquals(expectedCategory.getDesignation(), savedCategory.getDesignation());
    assertEquals(expectedCategory.getIdEntreprise(), savedCategory.getIdEntreprise());
  }

  @Test(expected = InvalidEntityException.class)
  public void saveCategoryShouldThrowInvalidEntityException() {
    CategoryDto expectedCategory = CategoryDto.builder()
            .designation("Designation test")
            .build();

    service.save(expectedCategory);
  }

  @Test
  public void shouldUpdateCategoryWithSuccess() {

    /* Getting Category */
    CategoryDto categoryDto = service.findById(101);
    categoryDto.setCode("CAT-UP");
    categoryDto.setDesignation("category updated");

    CategoryDto categoryToUpdate = service.save(categoryDto);

    assertNotNull(categoryToUpdate);
    assertEquals(categoryToUpdate.getCode(), "CAT-UP");
    assertEquals(categoryToUpdate.getDesignation(), "category updated");
  }

  @Test
  public final void testReadCategoryById() throws Exception {

    /* Getting category */
    CategoryDto category = service.findById(100);

    assertNotNull(category,"The Category should be existing");
    assertEquals(category.getIdEntreprise(),3);
  }

  @Test(expected = EntityNotFoundException.class)
  public final void testReadNonExistingCategoryIdentifier() throws Exception {
    /* Getting non existing Category */
    service.findById(0);
  }

  @Test
  public final void testReadCategoryByIdIsNull() throws Exception {

    /* Getting category */
    CategoryDto category = service.findById(null);

    assertNull(category);
  }

  @Test
  public final void testReadCategoryByCode() throws Exception {

    /* Getting category */
    CategoryDto category = service.findByCode("CAT-100");

    assertNotNull(category,"The Category should be existing");
    assertEquals(category.getIdEntreprise(),3);
  }

  @Test
  public final void testReadCategoryWithCodeIsEmpty() throws Exception {

    /* Getting category */
    CategoryDto category = service.findByCode("");

    assertNull(category);
  }

  @Test(expected = EntityNotFoundException.class)
  public final void testReadNonExistingCategoryCode() throws Exception {
    /* Getting non existing Category */
    service.findByCode("0");
  }
  @Test
  public final void testReadCategories() throws Exception {

    /* Getting categories */
    List<CategoryDto> categories = service.findAll();

    assertNotNull(categories);
    assertTrue(categories.size() > 0);
  }


  @Test
  public void shouldThrowInvalidEntityException() {
    CategoryDto expectedCategory = CategoryDto.builder().build();

    InvalidEntityException expectedException = assertThrows(InvalidEntityException.class, () -> service.save(expectedCategory));

    assertEquals(ErrorCodes.CATEGORY_NOT_VALID, expectedException.getErrorCode());
    assertEquals(1, expectedException.getErrors().size());
    assertEquals("Please enter the category code", expectedException.getErrors().get(0));
  }

  @Test
  public void shouldThrowEntityNotFoundException() {
    EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> service.findById(0));

    assertEquals(ErrorCodes.CATEGORY_NOT_FOUND, expectedException.getErrorCode());
    assertEquals(MessageFormat.format("Category not found with the id: {0}", 0), expectedException.getMessage());
  }


  @Test(expected = EntityNotFoundException.class)
  public final void testDeleteCategory() throws Exception {
    /* Deleting Category */
    service.delete(100);
    /* Getting Category */
    service.findById(100);
  }

  @Test(expected = EntityNotFoundException.class)
  public final void testDeleteCategoryShouldThrowEntityNotFoundException() throws Exception {
    /* Deleting category */
    service.delete(0);
  }

  @Test
  public final void testDeleteCategoryWithEmptyId() throws Exception {
    /* Deleting category */
    service.delete(null);
  }

  @Test
  public void testDeleteCategoryShouldThrowInvalidOperationException() {
    InvalidOperationException expectedException = assertThrows(InvalidOperationException.class, () -> service.delete(101));

    assertEquals(ErrorCodes.CATEGORY_ALREADY_IN_USE, expectedException.getErrorCode());
    assertEquals(MessageFormat.format("Category removal unauthorized for id: {0}", 101), expectedException.getMessage());
  }
}
