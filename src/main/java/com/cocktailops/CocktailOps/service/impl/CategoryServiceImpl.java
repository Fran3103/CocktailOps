package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.CategoryRequestDto;
import com.cocktailops.CocktailOps.dto.CategoryResponseDto;
import com.cocktailops.CocktailOps.dto.UserResponseDto;
import com.cocktailops.CocktailOps.entitie.Category;
import com.cocktailops.CocktailOps.entitie.Shop;
import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.exception.DuplicateResourceException;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.ICategoryRepository;
import com.cocktailops.CocktailOps.repository.IShopRepository;
import com.cocktailops.CocktailOps.service.ICategoryService;
import com.cocktailops.CocktailOps.service.IShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    private final IShopRepository shopRepository;

    @Override
    public CategoryResponseDto getCategoryById(long id) {
       Category category = categoryRepository.findById(id)
               .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));

       return new CategoryResponseDto(category.getId(), category.getName(), category.getShop().getId(),category.getSlug(),category.getActive());
     }


    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {

        if(categoryRepository.existsByName(categoryRequestDto.name())){
            throw new DuplicateResourceException("Category with name " + categoryRequestDto.name() + " already exists");}

        Shop shop = shopRepository.findById(categoryRequestDto.shop())
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id " + categoryRequestDto.shop() + " not found"));
           Category c = new Category();



            c.setName(categoryRequestDto.name());
            c.setShop(shop);
            c.setSlug(categoryRequestDto.slug());
            c.setActive(categoryRequestDto.active());

            Category savedCategory = categoryRepository.save(c);

        return new CategoryResponseDto(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getShop().getId(),
                savedCategory.getSlug(),
                savedCategory.getActive()
        );
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto Dto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));


        if (Dto.name() != null ) category.setName(Dto.name());
        if (Dto.slug() != null ) category.setSlug(Dto.slug());
        if (Dto.active() != null ) category.setActive(Dto.active());
        if (Dto.shop() != null) {
            Shop shop = shopRepository.findById(Dto.shop())
                    .orElseThrow(() -> new ResourceNotFoundException("Shop with id " + Dto.shop() + " not found"));
            category.setShop(shop);
        }

        Category updatedCategory = categoryRepository.save(category);

        return new CategoryResponseDto(
                updatedCategory.getId(),
                updatedCategory.getName(),
                updatedCategory.getShop().getId(),
                updatedCategory.getSlug(),
                updatedCategory.getActive()
        );
    }

    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));

        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(c -> new CategoryResponseDto(c.getId(), c.getName(), c.getShop().getId(), c.getSlug(), c.getActive() ))
                .toList();
    }

    @Override
    public CategoryResponseDto getCategoryByName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new ResourceNotFoundException("Category with name " + categoryName + " not found");
        }

        return new CategoryResponseDto(category.getId(), category.getName(), category.getShop().getId(),category.getSlug(),category.getActive()) ;
    }


}
