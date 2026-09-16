package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.productDto.ProductRequestDto;
import com.cocktailops.CocktailOps.dto.productDto.ProductResponseDto;
import com.cocktailops.CocktailOps.service.IProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
        name = "Productos",
        description = """
                Consulta y administración del catálogo de productos utilizados como insumos.

                La consulta de productos es pública.

                Las operaciones administrativas del catálogo están disponibles
                en la API para usuarios con rol ADMIN, pero no están expuestas
                en la interfaz web actual.
                """
)
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @Operation(
            summary = "Crear un producto",
            description = "Crea un nuevo producto en el catálogo. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto creado correctamente",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un producto con ese nombre")
    })
    @PostMapping()
    public ResponseEntity<ProductResponseDto> create(@Validated  @RequestBody ProductRequestDto dto) {
        ProductResponseDto productResponseDto = productService.create(dto);
        return ResponseEntity.ok(productResponseDto);
    }


    @Operation(
            summary = "Actualizar parcialmente un producto",
            description = """
                Modifica únicamente los campos enviados del producto.

                Los campos omitidos conservan su valor actual.
                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @Validated @RequestBody ProductRequestDto dto) {
        ProductResponseDto productResponseDto = productService.update(id, dto);
        return ResponseEntity.ok(productResponseDto);
    }


    @Operation(
            summary = "Consultar productos",
            description = """
                Devuelve los productos disponibles en el catálogo.

                Opcionalmente permite buscar por ID, nombre o categoría.
                Si no se envía ningún filtro, devuelve todos los productos.
                Este endpoint es público.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping()
    public  ResponseEntity <List<ProductResponseDto>> getAll(

            @Parameter(description = "ID del producto", example = "1")
            @RequestParam(required = false) Long id,

            @Parameter(description = "Nombre del producto", example = "Gin")
            @RequestParam(required = false) String name,

            @Parameter(description = "Nombre de la categoría", example = "Destilados")
            @RequestParam(required = false) String category

    ) {

        if (id != null) return ResponseEntity.ok(List.of(productService.findById(id)));
        if (name != null && !name.isBlank()) return ResponseEntity.ok(List.of(productService.findByName(name)));
        if (category != null && !category.isBlank()) return ResponseEntity.ok(productService.findByCategoryName(category));

        return  ResponseEntity.ok(productService.findAll());
    }

    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina un producto del catálogo. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
