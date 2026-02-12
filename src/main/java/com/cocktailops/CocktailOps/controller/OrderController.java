package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.OrderByDrinksRequestDto;
import com.cocktailops.CocktailOps.dto.OrderRequestDto;
import com.cocktailops.CocktailOps.dto.OrderResponseDto;
import com.cocktailops.CocktailOps.service.IOrderService;
import com.cocktailops.CocktailOps.service.IOrderPdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {


    private final IOrderService orderService;

    private final IOrderPdfService orderPdfService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderRequestDto dto) {
        OrderResponseDto created = orderService.createOrder(dto);
        URI location = URI.create("/orders/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) throws BadRequestException {

        byte[] pdf = orderPdfService.generateOrderPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename("order"+ id + ".pdf")
                        .build()
        );

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @PostMapping("/by-drinks")
    public ResponseEntity<OrderResponseDto> createByDrinks(@RequestBody OrderByDrinksRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderByDrinks(dto));
    }


}
