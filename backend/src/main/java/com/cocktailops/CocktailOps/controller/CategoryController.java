package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.categoryDto.CategoryRequestDto;
import com.cocktailops.CocktailOps.dto.categoryDto.CategoryResponseDto;
import com.cocktailops.CocktailOps.service.ICategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@Tag(
        name = "Categorías",
        description = """
                Consulta y administración de las categorías utilizadas para organizar los productos.

                La consulta de categorías es pública.

                Las operaciones de creación, modificación y eliminación están
                implementadas en la API para usuarios con rol ADMIN, pero no
                están expuestas en la interfaz web actual de CocktailOps.
                """
)
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;


    @Operation(
            summary = "Crear una categoría",
            description = """
                Crea una nueva categoría para organizar productos.

                Requiere rol ADMIN.
                Esta operación está disponible a nivel API y no está expuesta
                en la interfaz web actual.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría creada correctamente",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de la categoría inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe una categoría con ese nombre"
            )
    })
    @PostMapping()
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto dto) {

        CategoryResponseDto response = categoryService.createCategory(dto);

        return ResponseEntity.ok().body(response);

    }

    @Operation(
            summary = "Consultar categorías",
            description = """
                Devuelve las categorías disponibles.

                Opcionalmente permite buscar por nombre o ID.
                Si no se envía ningún filtro, devuelve todas las categorías.

                Este endpoint es público.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categorías obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
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
