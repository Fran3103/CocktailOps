package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.orderDto.OrderByDrinksRequestDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderRequestDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderResponseDto;
import com.cocktailops.CocktailOps.service.IOrderService;
import com.cocktailops.CocktailOps.service.IOrderPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
@Tag(
        name = "Órdenes",
        description = "Creación, previsualización y consulta de órdenes, cálculo de insumos y generación de PDF"
)
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {


    private final IOrderService orderService;

    private final IOrderPdfService orderPdfService;

    @Operation(
            summary = "Crear una orden por invitados y duración",
            description = """
                Crea y guarda una orden calculada según la cantidad de invitados,
                la duración del evento y la prioridad asignada a cada cóctel.

                La prioridad puede tomar valores entre 1 y 4.
                Si no se especifica una prioridad, se utiliza el valor predeterminado.

                Requiere autenticación mediante JWT.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Orden creada correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de la orden inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Cóctel o producto no encontrado"),
            @ApiResponse(responseCode = "422", description = "No se pudo cumplir una regla de negocio"),
            @ApiResponse(responseCode = "429", description = "Se alcanzó el límite de órdenes guardadas")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderRequestDto dto) {
        OrderResponseDto created = orderService.createOrder(dto);
        URI location = URI.create("/orders/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(
            summary = "Obtener mis órdenes",
            description = "Devuelve las órdenes guardadas pertenecientes al usuario autenticado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Órdenes obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders() {
        List<OrderResponseDto> ordes = orderService.getMyOrders();
        return ResponseEntity.ok(ordes);
    }

    @Operation(
            summary = "Obtener una orden por ID",
            description = """
                Devuelve una orden específica.

                Un usuario con rol USER solo puede consultar sus propias órdenes.
                Un usuario con rol ADMIN puede consultar cualquier orden.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a esta orden"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(
            summary = "Obtener todas las órdenes",
            description = "Devuelve todas las órdenes registradas. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Órdenes obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso permitido únicamente a administradores")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    @Operation(
            summary = "Obtener el PDF de una orden",
            description = """
                Genera el PDF correspondiente a una orden guardada.

                Un usuario con rol USER solo puede descargar el PDF de sus propias órdenes.
                Un usuario con rol ADMIN puede acceder al PDF de cualquier orden.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF generado correctamente",
                    content = @Content(mediaType = "application/pdf")
            ),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a esta orden"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error al generar el PDF")
    })
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {

        byte[] pdf = orderPdfService.generateOrderPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename("order" + id + ".pdf")
                        .build()
        );

        return ResponseEntity.ok().headers(headers).body(pdf);
    }


    @Operation(
            summary = "Crear una orden por cantidad exacta de cócteles",
            description = """
                Crea y guarda una orden indicando la cantidad total de cócteles
                y la cantidad correspondiente a cada cóctel seleccionado.

                La suma de las cantidades individuales debe coincidir con el total indicado.

                Requiere autenticación mediante JWT.
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Orden creada correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de la orden inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Cóctel o producto no encontrado"),
            @ApiResponse(responseCode = "422", description = "No se pudo cumplir una regla de negocio"),
            @ApiResponse(responseCode = "429", description = "Se alcanzó el límite de órdenes guardadas")
    })
    @PostMapping("/by-drinks")
    public ResponseEntity<OrderResponseDto> createByDrinks(@Valid @RequestBody OrderByDrinksRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderByDrinks(dto));
    }


    @Operation(
            summary = "Generar una vista previa en PDF de una orden por tiempo",
            description = "Calcula una orden por invitados y duración y genera el PDF sin guardar la orden."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF generado correctamente",
                    content = @Content(mediaType = "application/pdf")
            ),
            @ApiResponse(responseCode = "400", description = "Datos de la orden inválidos"),
            @ApiResponse(responseCode = "404", description = "Cóctel o producto no encontrado"),
            @ApiResponse(responseCode = "422", description = "No se pudo cumplir una regla de negocio"),
            @ApiResponse(responseCode = "500", description = "Error al generar el PDF")
    })
    @PostMapping("/preview/pdf")
    public ResponseEntity<byte[]> generatePreviewPdf(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        byte[] pdf = orderPdfService.generateOrderPreviewPdf(orderRequestDto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order-preview.pdf")
                .body(pdf);
    }


    @Operation(
            summary = "Generar una vista previa en PDF por cantidad exacta",
            description = "Calcula una orden por cantidades exactas de cócteles y genera el PDF sin guardar la orden."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF generado correctamente",
                    content = @Content(mediaType = "application/pdf")
            ),
            @ApiResponse(responseCode = "400", description = "Datos de la orden inválidos"),
            @ApiResponse(responseCode = "404", description = "Cóctel o producto no encontrado"),
            @ApiResponse(responseCode = "422", description = "No se pudo cumplir una regla de negocio"),
            @ApiResponse(responseCode = "500", description = "Error al generar el PDF")
    })
    @PostMapping("/by-drinks/preview/pdf")
    public ResponseEntity<byte[]> generateByDrinksPreviewPdf(@Valid @RequestBody OrderByDrinksRequestDto orderByDrinksRequestDto) {
        byte[] pdf = orderPdfService.generateOrderByDrinksPreviewPdf(orderByDrinksRequestDto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order-preview.pdf")
                .body(pdf);
    }


    @Operation(
            summary = "Previsualizar una orden por invitados y duración",
            description = "Calcula la distribución de cócteles y los insumos necesarios sin guardar la orden."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden calculada correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de la orden inválidos"),
            @ApiResponse(responseCode = "404", description = "Cóctel o producto no encontrado"),
            @ApiResponse(responseCode = "422", description = "No se pudo cumplir una regla de negocio")
    })
    @PostMapping("/preview")
    public ResponseEntity<OrderResponseDto> previewOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto
    ) {
        OrderResponseDto response = orderService.previewOrder(orderRequestDto);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Previsualizar una orden por cantidad exacta",
            description = """
                Calcula los insumos necesarios a partir de cantidades exactas
                de cada cóctel sin guardar la orden.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden calculada correctamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos de la orden inválidos"),
            @ApiResponse(responseCode = "404", description = "Cóctel o producto no encontrado"),
            @ApiResponse(responseCode = "422", description = "No se pudo cumplir una regla de negocio")
    })
    @PostMapping("/by-drinks/preview")
    public ResponseEntity<OrderResponseDto> previewOrderByDrinks(
            @Valid @RequestBody OrderByDrinksRequestDto orderByDrinksRequestDto
    ) {
        OrderResponseDto response = orderService.previewOrderByDrinks(orderByDrinksRequestDto);
        return ResponseEntity.ok(response);
    }
}
