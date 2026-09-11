package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.productDto.ProductRequestDto;
import com.cocktailops.CocktailOps.dto.productDto.ProductResponseDto;
import com.cocktailops.CocktailOps.entitie.Category;
import com.cocktailops.CocktailOps.entitie.Product;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.ICategoryRepository;
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.service.IProductService;
import com.cocktailops.CocktailOps.exception.DuplicateResourceException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));




        return new ProductResponseDto(
                product.getId(),
                 product.getName(),
                product.getDescription(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getUnit(),
                product.getPurchasable(),
                product.getImageUrl(),
                product.getImageAlt(),
                product.getActive(),
                product.getUnitSize()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found by name: " + name));

        return toResponse(product);
    }

    @Override
    public ProductResponseDto create(ProductRequestDto productDto) {
        if (productRepository.existsByName(productDto.name())) {
            log.warn("Product with name {} already exists", productDto.name());

            throw new DuplicateResourceException(
                    "Product with name " + productDto.name() + " already exists"
            );
        }
        Optional<Category> category = categoryRepository.findById(productDto.category());
        if (category.isEmpty()) {
            log.warn("Category with id {} not found", productDto.category());
            throw new ResourceNotFoundException("Category with id " + productDto.category() + " not found");
        }


        Product product = new Product();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setCategory(category.get());
        product.setUnit(productDto.unit());
        product.setPurchasable(
                productDto.purchasable() == null || productDto.purchasable()
        );
        product.setDescription(productDto.description());
        product.setImageUrl(productDto.imageUrl());
        product.setImageAlt(productDto.imageAlt());
        product.setActive(productDto.active());
        product.setUnitSize(productDto.unitSize());
        Product savedProduct = productRepository.save(product);

        log.info("Product created with id: {}", savedProduct.getId());
        return new ProductResponseDto(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getCategory().getId(),
                savedProduct.getCategory().getName(),
                savedProduct.getUnit(),
                savedProduct.getPurchasable(),
                savedProduct.getImageUrl(),
                savedProduct.getImageAlt(),
                savedProduct.getActive(),
                savedProduct.getUnitSize()
        );




    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto productDto) {
        Product product = productRepository.findById(id)
                 .orElseThrow(() -> {
                    log.warn("Product with id {} not found", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });

        if (productDto.category() != null) {
            Category category = categoryRepository.findById(productDto.category())
                    .orElseThrow(() -> {
                        log.warn("Category with id {} not found for update", productDto.category());
                        return new ResourceNotFoundException(
                                "Category with id " + productDto.category() + " not found"
                        );
                    });

            product.setCategory(category);
        }


        if (productDto.name() != null) product.setName(productDto.name());

        if (productDto.description() != null) {
            product.setDescription(productDto.description());
        };
        if (productDto.unit() != null) product.setUnit(productDto.unit());
        if (productDto.purchasable() != null) {
            product.setPurchasable(productDto.purchasable());
        }
        if (productDto.imageUrl() != null) product.setImageUrl(productDto.imageUrl());
        if (productDto.imageAlt() != null) product.setImageAlt(productDto.imageAlt());
        if (productDto.active() != null) product.setActive(productDto.active());
        if (productDto.unitSize() != null) product.setUnitSize(productDto.unitSize());
        Product updatedProduct = productRepository.save(product);

        log.info("Product with id {} updated successfully", id);
        return new ProductResponseDto(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getCategory().getId(),
                updatedProduct.getCategory().getName(),
                updatedProduct.getUnit(),
                updatedProduct.getPurchasable(),
                updatedProduct.getImageUrl(),
                updatedProduct.getImageAlt(),
                updatedProduct.getActive(),
                updatedProduct.getUnitSize()
        );

    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product with id {} not found for deletion", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });

        productRepository.delete(product);
    }

    @Override
    public List<ProductResponseDto> findAll() {
        List<Product> products = productRepository.findAllWithCategory();
        return products.stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getUnit(),
                        product.getPurchasable(),
                        product.getImageUrl(),
                        product.getImageAlt(),
                        product.getActive(),
                        product.getUnitSize()
                ))
                .toList();
    }

    @Override
    public List<ProductResponseDto> findByCategoryName(String categoryName) {
        List<Product> products = productRepository.findAllWithCategory();
        return products.stream()
                .filter(product -> product.getCategory().getName().equalsIgnoreCase(categoryName))
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getUnit(),
                        product.getPurchasable(),
                        product.getImageUrl(),
                        product.getImageAlt(),
                        product.getActive(),
                        product.getUnitSize()
                ))
                .toList();
    }

    private ProductResponseDto toResponse(Product product) {
        Category category = product.getCategory();

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                category.getId(),
                category.getName(),
                product.getUnit(),
                product.getPurchasable(),
                product.getImageUrl(),
                product.getImageAlt(),
                product.getActive(),
                product.getUnitSize()

        );
    }
}
