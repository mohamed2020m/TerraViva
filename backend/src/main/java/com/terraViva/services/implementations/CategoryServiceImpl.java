package com.terraViva.services.implementations;

import com.terraViva.dto.CategoryDTO;
import com.terraViva.exceptions.UserNotFoundException;
import com.terraViva.mapper.CategoryDTOConverter;
import com.terraViva.models.Category;
import com.terraViva.repositories.CategoryRepository;
import com.terraViva.repositories.ThreeDObjectRepository;
import com.terraViva.services.interfaces.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDTOConverter categoryDTOConverter;
    private CategoryRepository categoryRepository;
    private final ThreeDObjectRepository threeDObjectRepository;


    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryDTOConverter categoryDTOConverter, ThreeDObjectRepository threeDObjectRepository) {
        this.categoryDTOConverter = categoryDTOConverter;
        this.categoryRepository = categoryRepository;
        this.threeDObjectRepository = threeDObjectRepository;
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        Category parentCategory = null;
        if (categoryDTO.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(categoryDTO.getParentCategoryId())
                    .orElse(null);
        }

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .image(categoryDTO.getImage())
                .createdAt(LocalDateTime.now())
                .parentCategory(parentCategory)
                .build();

        CategoryDTO savedCategory = categoryDTOConverter.toDto(categoryRepository.save(category));
        return savedCategory;
    }


    @Override
    public CategoryDTO getCategoryById(Long categoryId) throws UserNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new UserNotFoundException("Category not found"));
        CategoryDTO categoryDTO = categoryDTOConverter.toDto(category);
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> getSubCategoryByCategoryId(Long categoryId) throws UserNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new UserNotFoundException("Category not found"));
        List<Category> subcategories = categoryRepository.findByParentCategoryId(categoryId);
        return subcategories.stream()
                .map(categoryDTOConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categories.stream().map(category -> categoryDTOConverter.toDto(category)).collect(Collectors.toList());
        return categoryDTOs;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) throws UserNotFoundException {
        Category existingCategory = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new UserNotFoundException("Category not found with id: " + categoryDTO.getId()));

        if (categoryDTO.getImage() != null) {
            existingCategory.setImage(categoryDTO.getImage());
        }
        if (categoryDTO.getDescription() != null) {
            existingCategory.setDescription(categoryDTO.getDescription());
        }
        if (categoryDTO.getName() != null) {
            existingCategory.setName(categoryDTO.getName());
        }
        if (categoryDTO.getCreatedAt() != null) {
            existingCategory.setCreatedAt(categoryDTO.getCreatedAt());
        }

        existingCategory.setUpdatedAt(LocalDateTime.now());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryDTOConverter.toDto(updatedCategory);
    }


    @Override
    public void deleteCategory(Long categoryId) throws UserNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new UserNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public Long getCategoriesCount() {
        return categoryRepository.count();
    }

    @Override
    public List<CategoryDTO> getMainCategories(){
        List<Category> categories = categoryRepository.findByParentCategoryIdIsNull();
        List<CategoryDTO> categoryDTOs = categories.stream().map(category -> categoryDTOConverter.toDto(category)).collect(Collectors.toList());
        return categoryDTOs;
    }

    @Override
    public Long getMainCategoriesCount() {
        return categoryRepository.countMainCategories();
    }

    @Override
    public Long getSubCategoryCountByCategoryId(Long categoryId) {
        return categoryRepository.countByParentId(categoryId);
    }

    @Override
    public Long getThreeDObjectCountByCategoryId(Long categoryId) {
        return threeDObjectRepository.countByCategoryId(categoryId);
    }
}