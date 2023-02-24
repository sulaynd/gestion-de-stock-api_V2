package com.talixmines.management.inventorymanager.validator;

import com.talixmines.management.inventorymanager.dto.CategoryDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class CategoryValidator {
  static final String CATEGORY_REQUIRED_CODE_MESSAGE = "Please enter the category code";

  public static List<String> validate(CategoryDto categoryDto) {
    List<String> errors = new ArrayList<>();

    if (categoryDto == null || !StringUtils.hasLength(categoryDto.getCode())) {
      errors.add(CATEGORY_REQUIRED_CODE_MESSAGE);
    }
    return errors;
  }

}
