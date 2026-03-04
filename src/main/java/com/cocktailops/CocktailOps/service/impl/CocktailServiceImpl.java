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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CocktailServiceImpl implements ICocktailService {

    private final ICocktailRepository cocktailRepository;

    private final IProductRepository productRepository;

    private final ICocktailIngredientRepository ingredientRepository;

    @Override
    public CocktailResponseDto create(CocktailRequestDto dto) {

        // validaciones
        if (cocktailRepository.existsByName(dto.name())) {
            log.warn("Attempt to create duplicate cocktail with name: {}", dto.name());
            throw new DuplicateResourceException("Cocktail with name " + dto.name() + " already exists.");
        }



        // Crear el cocktail

        log.info("Creating cocktail with name: {}", dto.name());
        Cocktail cocktail = new Cocktail();
        cocktail.setName(dto.name());
        cocktail.setDescription(dto.description());
        cocktail.setImageAlt(dto.imageAlt());
        cocktail.setImageUrl(dto.imageUrl());

        // Crear los ingredientes y asociarlos al cocktail
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

        // Asociar los ingredientes al cocktail
        cocktail.setIngredients(ingredients);

        // Guardar el cocktail (esto también guardará los ingredientes debido a la relación cascade)
        Cocktail savedCocktail = cocktailRepository.save(cocktail);
        log.info("Cocktail created successfully with id: {}", savedCocktail.getId());
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

        log.info("Fetching cocktail with id: {}", id);
        Optional<Cocktail> cocktail = cocktailRepository.findByWithIngredients(id);
        if (cocktail.isEmpty()) {
            log.warn("Cocktail with id {} not found", id);
            throw new ResourceNotFoundException("Cocktail with id " + id + " not found");
        }
        log.info("Cocktail with id {} found", id);
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

        log.info("Updating cocktail with id: {}", id);

        Optional<Cocktail> cocktail = cocktailRepository.findById(id);
        if (cocktail.isEmpty()) {
            log.warn("Cocktail with id {} not found for update", id);
            throw new ResourceNotFoundException("Cocktail with id " + id + " not found");
        }

        Cocktail existingCocktail = cocktail.get();

        if (Dto.name() != null) existingCocktail.setName(Dto.name());
        if (Dto.description() != null) existingCocktail.setDescription(Dto.description());
        if (Dto.imageUrl() != null) existingCocktail.setImageUrl(Dto.imageUrl());
        if (Dto.imageAlt() != null) existingCocktail.setImageAlt(Dto.imageAlt());

        Cocktail updatedCocktail = cocktailRepository.save(existingCocktail);
        log.info("Cocktail with id {} updated successfully", id);

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
        log.info("Fetching cocktail with name: {}", name);
        CocktailResponseDto cocktail = cocktailRepository.findByName(name);
        if (cocktail == null) {
            log.warn("Cocktail with name {} not found", name);
            throw new ResourceNotFoundException("Shop with name " + name + " not found");
        }
        log.info("Cocktail with name {} found", name);
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

        int count = cocktailIngredientDto != null ? cocktailIngredientDto.size() : 0;
        log.info("addIngredientToCocktail started:  cocktailId={}, ingredientsToAdd={}", cocktailId, count);

        Cocktail cocktail = cocktailRepository.findById(cocktailId)
                .orElseThrow(() -> {
                    log.warn("Cocktail with id {} not found for adding ingredients", cocktailId);
                    return new ResourceNotFoundException("Cocktail with id " + cocktailId + " not found");
                });

        // Validar que los productos existan y que no se dupliquen ingredientes para el mismo producto
        for (CocktailIngredientRequestDto dto : cocktailIngredientDto){
       Product product = productRepository.findById(dto.productId())
               .orElseThrow(() -> {
                   log.warn("Product with id {} not found for cocktail id {}", dto.productId(), cocktailId);
                  return new ResourceNotFoundException("Product with id " + dto.productId() + " not found");
               });

        log.info("Product with id {} found for cocktail id {}", dto.productId(), cocktailId);
            if (ingredientRepository.existsByCocktailIdAndProductId(cocktailId, dto.productId())) {
                log.warn("Ingredient with product id {} already exists for cocktail id {}", dto.productId(), cocktailId);
                throw new DuplicateResourceException("Ingredient already exists for productId " + dto.productId());
            }

            log.debug("Creating ingredient: product id {}, cocktail id {}, amount={}, unit={}",
                    dto.productId(), cocktailId, dto.amount(), dto.unit());
        CocktailIngredient ingredient = new CocktailIngredient();
        ingredient.setCocktail(cocktail);
        ingredient.setProduct(product);
        ingredient.setAmount(dto.amount());
        ingredient.setUnit(dto.unit());

        ingredientRepository.save(ingredient);

        }

        log.info("addIngredientToCocktail completed successfully for cocktailId={}", cocktailId);
        return getById(cocktailId);
    }

    @Override
    @Transactional
    public void removeIngredientFromCocktail(Long cocktailId, Long productId) {

        if(!ingredientRepository.existsByCocktailIdAndProductId(cocktailId, productId)) {
            log.warn("Ingredient with cocktail id {} and product id {} not found", cocktailId, productId);
            throw new ResourceNotFoundException("Ingredient with cocktail id " + cocktailId + " and product id " + productId + " not found");
        }
        log.info("Removing ingredient with cocktail id {} and product id {}", cocktailId, productId);
        ingredientRepository.deleteByCocktailIdAndProductId(cocktailId, productId);

    }

    @Override
    public CocktailResponseDto updateCocktailIngredient(Long cocktailId, Long productId, CocktailIngredientRequestDto cocktailIngredientDto) {
        CocktailIngredient ing = ingredientRepository.findByCocktailIdAndProductId(cocktailId, productId)
                .orElseThrow(() -> {
                    log.warn("Ingredient not found for cocktail id {} and product id {}", cocktailId, productId);
                    return new ResourceNotFoundException("Ingredient not found for cocktail " + cocktailId + " and product " + productId);
                });

        ing.setAmount(cocktailIngredientDto.amount());
        ing.setUnit(cocktailIngredientDto.unit());
        ingredientRepository.save(ing);

        log.info("Ingredient updated for cocktail id {} and product id {}", cocktailId, productId);
        return getById(cocktailId);


    }




}
