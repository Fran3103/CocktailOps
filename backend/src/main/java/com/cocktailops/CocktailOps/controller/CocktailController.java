package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailIngredientRequestDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailRequestDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailResponseDto;
import com.cocktailops.CocktailOps.service.ICocktailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name =  "Cocktails", description = "Crea y gestiona cocktails ")
@RestController
@RequestMapping("/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    public final ICocktailService cocktailService;


    @PostMapping()
    public ResponseEntity<CocktailResponseDto> create(@RequestBody CocktailRequestDto dto) {
        CocktailResponseDto response = cocktailService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<CocktailResponseDto>> find(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) Long id
    ){
        if (name != null && !name.isBlank()) return ResponseEntity.ok(List.of(cocktailService.findByName(name)));
        if (id != null) return ResponseEntity.ok(List.of(cocktailService.getById(id)));
        return ResponseEntity.ok(cocktailService.findAll());
    }


    @PatchMapping("/{id}")
    public ResponseEntity<CocktailResponseDto> update(
            @PathVariable Long id,
            @RequestBody CocktailResponseDto dto
    ) {
        CocktailResponseDto response = cocktailService.update(id, dto);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cocktailService.delete(id);
        return ResponseEntity.noContent().build();
    }



    // crud for cocktail ingredients



    @PostMapping("/{cocktailId}/ingredients")
    public ResponseEntity<CocktailResponseDto>addIngredient(
            @PathVariable Long cocktailId,
            @RequestBody List<CocktailIngredientRequestDto> ingredientDto
    ){
        return ResponseEntity.ok(cocktailService.addIngredientToCocktail(cocktailId, ingredientDto));
    }


    @DeleteMapping("/{cocktailId}/ingredients/{productId}")
    public ResponseEntity<Void> removeIngredient(
            @PathVariable Long cocktailId,
            @PathVariable Long productId
    ) {
        cocktailService.removeIngredientFromCocktail(cocktailId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cocktailId}/ingredients/{productId}")
    public ResponseEntity<CocktailResponseDto> updateIngredient(
            @PathVariable Long cocktailId,
            @PathVariable Long productId,
            @RequestBody CocktailIngredientRequestDto ingredientDto){

        return ResponseEntity.ok(cocktailService.updateCocktailIngredient(cocktailId, productId, ingredientDto));

    }
}