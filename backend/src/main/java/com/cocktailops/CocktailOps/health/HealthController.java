package com.cocktailops.CocktailOps.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(
        name = "Estado del servicio",
        description = "Verificación del estado básico de la API CocktailOps"
)
@RestController
public class HealthController {

    @Operation(
            summary = "Consultar estado de la API",
            description = """
                    Comprueba que el servicio backend se encuentra disponible.

                    Este endpoint es público y se utiliza también para
                    verificaciones de disponibilidad durante el despliegue.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "La API se encuentra disponible"
            )
    })
    @GetMapping("/healthz")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}