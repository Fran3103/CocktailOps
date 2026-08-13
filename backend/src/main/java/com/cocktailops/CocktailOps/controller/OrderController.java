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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Orders", description = "Creación de órdenes y cálculo de insumos")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {


    private final IOrderService orderService;

    private final IOrderPdfService orderPdfService;

    @Operation(
            summary = "Crear una orden",
            description = "Calcula packs a comprar en base a invitados/duración y pesos por cocktail." +
                    "El peso indica cuanta  importancia se le da a ese cocktail,  mas peso, mas cocktails.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden creada",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validación inválida"),
            @ApiResponse(responseCode = "404", description = "Cocktail/Product no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderRequestDto dto) {
        OrderResponseDto created = orderService.createOrder(dto);
        URI location = URI.create("/orders/" + created.id());
        return ResponseEntity.created(location).body(created);
    }


    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders() {
        List<OrderResponseDto> ordes = orderService.getMyOrders();
        return ResponseEntity.ok(ordes);
    }

    @Operation(
            summary = "Traer orden por Id",
            description = "Calcula packs a comprar en base a invitados/duración y pesos por cocktail." +
                    "El peso indica cuanta  importancia se le da a ese cocktail,  mas peso, mas cocktails.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden obtenida"),
            @ApiResponse(responseCode = "400", description = "Validación inválida"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(
            summary = "Traer todas las ordenes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ordenes obtenidas"),
            @ApiResponse(responseCode = "400", description = "Validación inválida"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    @Operation(summary = "Descargar PDF de una orden")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pdf creado"),
            @ApiResponse(responseCode = "400", description = "Validación inválida"),
            @ApiResponse(responseCode = "404", description = "Cocktail/Product no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
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
            summary = "Crear una orden por cantidad de cocktails",
            description = "Calcula packs a comprar en base a cantidad total de cocktials indicados por el usuario y cantidad  por cocktail." +
                    "El usuario indica el total de cocktails  que quiere y la cantidad de cada cocktail.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden creada",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validación inválida"),
            @ApiResponse(responseCode = "404", description = "Cocktail/Product no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos")
    })
    @PostMapping("/by-drinks")
    public ResponseEntity<OrderResponseDto> createByDrinks(@Valid @RequestBody OrderByDrinksRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderByDrinks(dto));
    }

    @PostMapping("/preview/pdf")
    public ResponseEntity<byte[]> generatePreviewPdf(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        byte[] pdf = orderPdfService.generateOrderPreviewPdf(orderRequestDto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order-preview.pdf")
                .body(pdf);
    }

    @PostMapping("/by-drinks/preview/pdf")
    public ResponseEntity<byte[]> generateByDrinksPreviewPdf(@Valid @RequestBody OrderByDrinksRequestDto orderByDrinksRequestDto) {
        byte[] pdf = orderPdfService.generateOrderByDrinksPreviewPdf(orderByDrinksRequestDto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order-preview.pdf")
                .body(pdf);
    }

    @PostMapping("/preview")
    public ResponseEntity<OrderResponseDto> previewOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto
    ) {
        OrderResponseDto response = orderService.previewOrder(orderRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/by-drinks/preview")
    public ResponseEntity<OrderResponseDto> previewOrderByDrinks(
            @Valid @RequestBody OrderByDrinksRequestDto orderByDrinksRequestDto
    ) {
        OrderResponseDto response = orderService.previewOrderByDrinks(orderByDrinksRequestDto);
        return ResponseEntity.ok(response);
    }
}
