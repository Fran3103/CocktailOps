package com.cocktailops.CocktailOps.service.impl;


import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailIngredientRequestDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailIngredientResponseDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailRequestDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailResponseDto;
import com.cocktailops.CocktailOps.entitie.Cocktail;
import com.cocktailops.CocktailOps.entitie.CocktailIngredient;
import com.cocktailops.CocktailOps.entitie.Product;
import com.cocktailops.CocktailOps.exception.DuplicateResourceException;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.ICocktailIngredientRepository;
import com.cocktailops.CocktailOps.repository.ICocktailRepository;
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.service.ICocktailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CocktailServiceImpl implements ICocktailService {

    private final ICocktailRepository cocktailRepository;

    private final IProductRepository productRepository;

    private final ICocktailIngredientRepository ingredientRepository;

    @Override
    public CocktailResponseDto create(CocktailRequestDto dto) {
        if (cocktailRepository.existsByName(dto.name())) {
            throw new DuplicateResourceException("Cocktail with name " + dto.name() + " already exists.");
        }

        Cocktail cocktail = new Cocktail();
        cocktail.setName(dto.name());
        cocktail.setDescription(dto.description());
        cocktail.setImageAlt(dto.imageAlt());
        cocktail.setImageUrl(dto.imageUrl());

        List<CocktailIngredient> ingredients = dto.ingredients().stream()
                .map(ingredientDto -> {
                    Product product = productRepository.findById(ingredientDto.productId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + ingredientDto.productId() + " not found"));

                    CocktailIngredient ing= new CocktailIngredient();
                    ing.setProduct(product);
                    ing.setAmount(ingredientDto.amount());
                    ing.setUnit(ingredientDto.unit());
                    ing.setCocktail(cocktail);
                    return ing;
                })
                .toList();

        cocktail.setIngredients(ingredients);

        Cocktail savedCocktail = cocktailRepository.save(cocktail);
        return new CocktailResponseDto(
                savedCocktail.getId(),
                savedCocktail.getName(),
                savedCocktail.getDescription(),
                savedCocktail.getImageUrl(),
                savedCocktail.getImageAlt(),
                savedCocktail.getIngredients().stream()
                        .map(ing -> new CocktailIngredientResponseDto(
                                ing.getProduct().getId(),
                                ing.getProduct().getName(),
                                ing.getAmount(),
                                ing.getUnit()
                        ))
                        .toList()

        );
    }

    @Override
    @Transactional(readOnly = true)
    public CocktailResponseDto getById(Long id) {
        Optional<Cocktail> cocktail = cocktailRepository.findByWithIngredients(id);
        if (cocktail.isEmpty()) {
            throw new ResourceNotFoundException("Cocktail with id " + id + " not found");
        }

        return new CocktailResponseDto(
                cocktail.get().getId(),
                cocktail.get().getName(),
                cocktail.get().getDescription(),
                cocktail.get().getImageUrl(),
                cocktail.get().getImageAlt(),
                cocktail.get().getIngredients().stream()
                        .map(ing -> new CocktailIngredientResponseDto(
                                ing.getProduct().getId(),
                                ing.getProduct().getName(),
                                ing.getAmount(),
                                ing.getUnit()
                        ))
                        .toList()
        );
    }

    @Override
    public CocktailResponseDto update(Long id, CocktailResponseDto Dto) {
        Cocktail cocktail = cocktailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cocktail with id " + id + " not found"));

        if (Dto.name() != null) cocktail.setName(Dto.name());
        if (Dto.description() != null) cocktail.setDescription(Dto.description());
        if (Dto.imageUrl() != null) cocktail.setImageUrl(Dto.imageUrl());
        if (Dto.imageAlt() != null) cocktail.setImageAlt(Dto.imageAlt());

        Cocktail updatedCocktail = cocktailRepository.save(cocktail);

        return new CocktailResponseDto(
                updatedCocktail.getId(),
                updatedCocktail.getName(),
                updatedCocktail.getDescription(),
                updatedCocktail.getImageUrl(),
                updatedCocktail.getImageAlt(),
                updatedCocktail.getIngredients().stream()
                        .map(ing -> new CocktailIngredientResponseDto(
                                ing.getProduct().getId(),
                                ing.getProduct().getName(),
                                ing.getAmount(),
                                ing.getUnit()
                        ))
                        .toList()
        );
    }

    @Override
    public void delete(Long id) {
        Cocktail cocktail = cocktailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id + " not found"));
        cocktailRepository.delete(cocktail);
    }

    @Override
    public CocktailResponseDto findByName(String name) {
        CocktailResponseDto cocktail = cocktailRepository.findByName(name);
        if (cocktail == null) {
            throw new ResourceNotFoundException("Shop with name " + name + " not found");
        }

        return cocktail;

    }

    @Override
    public List<CocktailResponseDto> findAll() {

        List<Cocktail> cocktails = cocktailRepository.findAll();
        return cocktails.stream()
                .map(cocktail -> new CocktailResponseDto(
                        cocktail.getId(),
                        cocktail.getName(),
                        cocktail.getDescription(),
                        cocktail.getImageUrl(),
                        cocktail.getImageAlt(),
                        cocktail.getIngredients().stream()
                                .map(ing -> new CocktailIngredientResponseDto(
                                        ing.getProduct().getId(),
                                        ing.getProduct().getName(),
                                        ing.getAmount(),
                                        ing.getUnit()
                                ))
                                .toList()
                ))
                .toList();
    }

    @Override
    public CocktailResponseDto addIngredientToCocktail(Long cocktailId, List<CocktailIngredientRequestDto> cocktailIngredientDto) {
        Cocktail cocktail = cocktailRepository.findById(cocktailId)
                .orElseThrow(() -> new ResourceNotFoundException("Cocktail with id " + cocktailId + " not found"));

        for (CocktailIngredientRequestDto dto : cocktailIngredientDto){
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + dto.productId() + " not found"));
            if (ingredientRepository.existsByCocktailIdAndProductId(cocktailId, dto.productId())) {
                throw new DuplicateResourceException("Ingredient already exists for productId " + dto.productId());
            }

        CocktailIngredient ingredient = new CocktailIngredient();
        ingredient.setCocktail(cocktail);
        ingredient.setProduct(product);
        ingredient.setAmount(dto.amount());
        ingredient.setUnit(dto.unit());

        ingredientRepository.save(ingredient);

        }

        return getById(cocktailId);
    }

    @Override
    @Transactional
    public void removeIngredientFromCocktail(Long cocktailId, Long productId) {

        if(!ingredientRepository.existsByCocktailIdAndProductId(cocktailId, productId)) {
            throw new ResourceNotFoundException("Ingredient with cocktail id " + cocktailId + " and product id " + productId + " not found");
        }
        ingredientRepository.deleteByCocktailIdAndProductId(cocktailId, productId);

    }

    @Override
    public CocktailResponseDto updateCocktailIngredient(Long cocktailId, Long productId, CocktailIngredientRequestDto cocktailIngredientDto) {
        CocktailIngredient ing = ingredientRepository.findByCocktailIdAndProductId(cocktailId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ingredient not found for cocktail " + cocktailId + " and product " + productId
                ));

        ing.setAmount(cocktailIngredientDto.amount());
        ing.setUnit(cocktailIngredientDto.unit());
        ingredientRepository.save(ing);

        return getById(cocktailId);


    }




}
