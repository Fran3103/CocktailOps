package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.categoryDto.CategoryRequestDto;
import com.cocktailops.CocktailOps.dto.categoryDto.CategoryResponseDto;
import com.cocktailops.CocktailOps.service.ICategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "categories" , description = "crea cataegoria de los productos")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;



    @PostMapping()
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto dto) {

        CategoryResponseDto response = categoryService.createCategory(dto);

        return ResponseEntity.ok().body(response);

    }


    @GetMapping()
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) Long id
    ) {
        if  (name != null && !name.isBlank()) return ResponseEntity.ok(List.of( categoryService.getCategoryByName(name)));
        if (id != null) return ResponseEntity.ok(List.of(categoryService.getCategoryById(id)));

        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto dto
    ) {
        CategoryResponseDto response = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
