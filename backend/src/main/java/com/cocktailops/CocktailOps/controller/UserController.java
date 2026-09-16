package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.userDto.UserRequestDto;
import com.cocktailops.CocktailOps.dto.userDto.UserResponseDto;
import com.cocktailops.CocktailOps.service.IUserService;
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

import java.net.URI;
import java.util.List;

@Tag(
        name = "Usuarios",
        description = """
                Administración de usuarios a nivel API.

                El registro utilizado por la aplicación web se realiza mediante
                el endpoint público /auth/register.

                Los endpoints de /user corresponden a operaciones administrativas,
                requieren rol ADMIN y no están expuestos en la interfaz web actual.
                """
)
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @Operation(
            summary = "Crear un usuario desde administración",
            description = """
                Crea un usuario desde el módulo administrativo de la API.

                Puede asignarse un rol y, opcionalmente, una tienda existente.
                Esta operación no corresponde al registro público de usuarios.

                Requiere rol ADMIN.
                No está expuesta en la interfaz web actual.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado correctamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Tienda asociada no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese correo electrónico")
    })
    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto createUser = userService.save(userRequestDto);

        URI location = URI.create("/user/" + createUser.id());


        return ResponseEntity.created(location).body(createUser);
    }

    @Operation(
            summary = "Consultar usuarios",
            description = """
                Devuelve usuarios registrados en el sistema.

                Opcionalmente permite buscar por correo electrónico o ID.
                Si no se envía ningún filtro, devuelve todos los usuarios.

                Requiere rol ADMIN.
                No está expuesto en la interfaz web actual.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @Parameter(description = "Correo electrónico del usuario", example = "usuario@ejemplo.com")
            @RequestParam(required = false) String email,

            @Parameter(description = "ID del usuario", example = "1")
            @RequestParam(required = false) Long userId
    ) {

        if (email != null) return ResponseEntity.ok(List.of(userService.findByEmail(email)));
        if (userId != null) return ResponseEntity.ok(List.of(userService.findById(userId)));

        List<UserResponseDto> res = userService.findAll();

        return ResponseEntity.ok(res);
    }


    @Operation(
            summary = "Eliminar un usuario",
            description = """
                Elimina un usuario registrado.

                Requiere rol ADMIN.
                Esta operación está disponible a nivel API y no está expuesta
                en la interfaz web actual.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Actualizar un usuario",
            description = """
                Modifica parcialmente los datos de un usuario existente.

                Permite actualizar nombre, apellido, contraseña, rol
                y asociación con una tienda.

                Requiere rol ADMIN.
                No está expuesto en la interfaz web actual.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores"),
            @ApiResponse(responseCode = "404", description = "Usuario o tienda no encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updateUser = userService.update(id, userRequestDto);
        return ResponseEntity.ok(updateUser);
    }
}
