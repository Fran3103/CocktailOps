package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailIngredientRequestDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailRequestDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailResponseDto;
import com.cocktailops.CocktailOps.service.ICocktailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@Tag(
        name = "Cócteles",
        description = """
                Consulta del catálogo de cócteles y administración de sus recetas.

                La consulta de cócteles es pública.

                Las operaciones de creación, modificación, eliminación y gestión
                de ingredientes están implementadas en la API y requieren rol ADMIN,
                pero no están expuestas en la interfaz web actual de CocktailOps.
                """
)
@RestController
@RequestMapping("/cocktails")
@RequiredArgsConstructor
public class CocktailController {

    public final ICocktailService cocktailService;


    @Operation(
            summary = "Crear un cóctel",
            description = """
                Crea un nuevo cóctel junto con su receta de ingredientes.
                Todos los productos utilizados como ingredientes deben existir previamente.

                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cóctel creado correctamente",
                    content = @Content(schema = @Schema(implementation = CocktailResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del cóctel inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Alguno de los productos indicados no existe"),
            @ApiResponse(responseCode = "409", description = "Ya existe un cóctel con ese nombre")
    })
    @PostMapping()
    public ResponseEntity<CocktailResponseDto> create(@RequestBody @Valid CocktailRequestDto dto) {
        CocktailResponseDto response = cocktailService.create(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Consultar cócteles",
            description = """
                Devuelve los cócteles disponibles junto con los ingredientes de cada receta.

                Opcionalmente permite buscar por nombre o ID.
                Si no se envía ningún filtro, devuelve todos los cócteles.
                Este endpoint es público.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cócteles obtenidos correctamente"),
            @ApiResponse(responseCode = "404", description = "Cóctel no encontrado")
    })
    @GetMapping()
    public ResponseEntity<List<CocktailResponseDto>> find(
            @Parameter(description = "Nombre del cóctel", example = "Negroni")
            @RequestParam(required = false) String name,

            @Parameter(description = "ID del cóctel", example = "1")
            @RequestParam(required = false) Long id
    ) {
        if (name != null && !name.isBlank()) return ResponseEntity.ok(List.of(cocktailService.findByName(name)));
        if (id != null) return ResponseEntity.ok(List.of(cocktailService.getById(id)));
        return ResponseEntity.ok(cocktailService.findAll());
    }


    @Operation(
            summary = "Actualizar parcialmente un cóctel",
            description = """
                Modifica los datos generales de un cóctel existente.
                Los campos omitidos conservan su valor actual.

                La administración de ingredientes se realiza mediante
                los endpoints específicos de ingredientes.

                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cóctel actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = CocktailResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Cóctel no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CocktailResponseDto> update(
            @PathVariable Long id,
            @RequestBody CocktailResponseDto dto
    ) {
        CocktailResponseDto response = cocktailService.update(id, dto);
        return ResponseEntity.ok(response);

    }

    @Operation(
            summary = "Eliminar un cóctel",
            description = "Elimina un cóctel del catálogo. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cóctel eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Cóctel no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cocktailService.delete(id);
        return ResponseEntity.noContent().build();
    }



    // crud for cocktail ingredients



    @Operation(
            summary = "Agregar ingredientes a un cóctel",
            description = """
                Agrega uno o más productos a la receta de un cóctel.

                El cóctel y los productos deben existir previamente.
                No se puede agregar dos veces el mismo producto a una receta.

                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ingredientes agregados correctamente",
                    content = @Content(schema = @Schema(implementation = CocktailResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del ingrediente inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Cóctel o producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "El producto ya forma parte de la receta")
    })
    @PostMapping("/{cocktailId}/ingredients")
    public ResponseEntity<CocktailResponseDto>addIngredient(
            @PathVariable Long cocktailId,
            @Valid
            @RequestBody List<CocktailIngredientRequestDto> ingredientDto
    ){
        return ResponseEntity.ok(cocktailService.addIngredientToCocktail(cocktailId, ingredientDto));
    }


    @Operation(
            summary = "Eliminar un ingrediente de un cóctel",
            description = """
                Elimina un producto de la receta del cóctel indicado.
                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ingrediente eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado en el cóctel")
    })
    @DeleteMapping("/{cocktailId}/ingredients/{productId}")
    public ResponseEntity<Void> removeIngredient(
            @PathVariable Long cocktailId,
            @PathVariable Long productId
    ) {
        cocktailService.removeIngredientFromCocktail(cocktailId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar un ingrediente de un cóctel",
            description = """
                Modifica la cantidad y la unidad de medida de un ingrediente
                existente dentro de la receta de un cóctel.

                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ingrediente actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = CocktailResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del ingrediente inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado en el cóctel")
    })
    @PatchMapping("/{cocktailId}/ingredients/{productId}")
    public ResponseEntity<CocktailResponseDto> updateIngredient(
            @PathVariable Long cocktailId,
            @PathVariable Long productId,
            @Valid
            @RequestBody CocktailIngredientRequestDto ingredientDto){

        return ResponseEntity.ok(cocktailService.updateCocktailIngredient(cocktailId, productId, ingredientDto));

    }
}