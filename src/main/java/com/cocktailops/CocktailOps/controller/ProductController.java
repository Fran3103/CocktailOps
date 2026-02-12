package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.ProductRequestDto;
import com.cocktailops.CocktailOps.dto.ProductResponseDto;
import com.cocktailops.CocktailOps.entitie.Product;
import com.cocktailops.CocktailOps.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;


    @PostMapping()
    public ResponseEntity<ProductResponseDto> create(@Validated  @RequestBody ProductRequestDto dto) {
        ProductResponseDto productResponseDto = productService.create(dto);
        return ResponseEntity.ok(productResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @Validated @RequestBody ProductRequestDto dto) {
        ProductResponseDto productResponseDto = productService.update(id, dto);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping()
    public  ResponseEntity <List<ProductResponseDto>> getAll(
            @RequestParam(required = false) Long id,
            @RequestParam (required = false)String name,
            @RequestParam (required = false) String category
    ) {

        if (id != null) return ResponseEntity.ok(List.of(productService.findById(id)));
        if (name != null && !name.isBlank()) return ResponseEntity.ok(List.of(productService.findByName(name)));
        if (category != null && !category.isBlank()) return ResponseEntity.ok(productService.findByCategoryName(category));

        return  ResponseEntity.ok(productService.findAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
