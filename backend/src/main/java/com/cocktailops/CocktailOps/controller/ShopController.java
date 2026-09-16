package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.shopDto.ShopRequestDto;
import com.cocktailops.CocktailOps.dto.shopDto.ShopResponseDto;
import com.cocktailops.CocktailOps.service.IShopService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        name = "Tiendas",
        description = """
                Administración de tiendas registradas en la API.

                Este módulo existe a nivel backend, pero no está integrado
                al flujo funcional actual de CocktailOps ni está expuesto
                en la interfaz web.

                Actualmente las tiendas no participan en la generación de órdenes,
                listas de compra ni enlaces de compra.

                Todos los endpoints de este módulo requieren rol ADMIN.
                """
)
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final IShopService shopService;

    @Operation(
            summary = "Crear una tienda",
            description = """
                Crea una nueva tienda en la API.

                Esta funcionalidad está implementada únicamente a nivel backend
                y no forma parte del flujo actual de la aplicación web.

                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tienda creada correctamente",
                    content = @Content(schema = @Schema(implementation = ShopResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "409", description = "Ya existe una tienda con ese slug")
    })
    @PostMapping()
    ResponseEntity<ShopResponseDto> addShop(@RequestBody ShopRequestDto shopRequestDto){

        ShopResponseDto shopResponseDto = shopService.createShop(shopRequestDto);

        return ResponseEntity.ok(shopResponseDto);
    }

    @Operation(
            summary = "Consultar tiendas",
            description = """
                Devuelve las tiendas registradas en la API.

                Opcionalmente permite buscar por nombre o ID.
                Si no se envía ningún filtro, devuelve todas las tiendas.

                Este módulo no está integrado al flujo actual del frontend.
                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tiendas obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @GetMapping()
    ResponseEntity<List<ShopResponseDto>> getAllShops(
            @Parameter(description = "Nombre de la tienda", example = "Proveedor Central")
            @RequestParam(required = false) String name,

            @Parameter(description = "ID de la tienda", example = "1")
            @RequestParam(required = false) Long id
    ) {

        if(name != null && !name.isBlank()) return ResponseEntity.ok(List.of(shopService.findByName(name)));
        if (id != null) return ResponseEntity.ok(List.of(shopService.findById(id)));

        return ResponseEntity.ok(shopService.findAll());
    }


    @Operation(
            summary = "Eliminar una tienda",
            description = """
                Elimina una tienda registrada en la API.

                Esta funcionalidad no está integrada al flujo actual de CocktailOps.
                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tienda eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada"),
            @ApiResponse(responseCode = "409", description = "La tienda no puede eliminarse por un conflicto de datos")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Actualizar una tienda",
            description = """
                Modifica parcialmente los datos de una tienda existente.

                Esta funcionalidad está implementada únicamente a nivel backend
                y no forma parte del flujo actual de la aplicación web.

                Requiere rol ADMIN.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tienda actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = ShopResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PatchMapping("/{id}")
    ResponseEntity<ShopResponseDto> updateShop(@PathVariable Long id, @RequestBody ShopRequestDto Dto) {
        ShopResponseDto updatedShop = shopService.updateShop(id, Dto);
        return ResponseEntity.ok(updatedShop);
    }
}
