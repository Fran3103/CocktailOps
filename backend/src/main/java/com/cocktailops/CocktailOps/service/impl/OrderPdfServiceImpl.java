package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.orderDto.*;
import com.cocktailops.CocktailOps.entitie.Role;
import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.security.CurrentUserService;
import com.cocktailops.CocktailOps.service.IOrderService;
import com.cocktailops.CocktailOps.service.IOrderPdfService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.cocktailops.CocktailOps.exception.PdfGenerationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPdfServiceImpl implements IOrderPdfService {

    private final TemplateEngine templateEngine;

    private final IOrderService orderService;

    private final CurrentUserService currentUserService;


    @Override
    public byte[] generateOrderPdf(Long orderId){

        log.info("Generating PDF for order with id: {}", orderId);

        OrderResponseDto responseDto = orderService.getOrderById(orderId);

        validatePdfAccess(responseDto);

        byte[] pdf = buildPdf(responseDto);

        log.info("PDF generated successfully for order with id: {}", orderId);

        return pdf;
    }

    @Override
    public byte[] generateOrderPreviewPdf(OrderRequestDto orderRequestDto) {
        OrderResponseDto responseDto = orderService.previewOrder(orderRequestDto);

        return buildPdf(responseDto);
    }

    @Override
    public byte[] generateOrderByDrinksPreviewPdf(OrderByDrinksRequestDto orderByDrinksRequestDto)  {
        OrderResponseDto responseDto = orderService.previewOrderByDrinks(orderByDrinksRequestDto);

        return buildPdf(responseDto);
    }

    private OrderPdfDto toPdfDto(OrderResponseDto o) {

        // Determinar el modo de cálculo del pedido (por tiempo o por bebidas)
        String mode = (o.guests() != null && o.durationHours() != null) ? "TIME" : "DRINKS";

        // Calcular el total de bebidas necesarias según el modo del pedido
        Integer totalDrinks;
        if ("TIME".equals(mode)) {
            if (o.guests() == null || o.drinksPerPerson() == null || o.durationHours() == null) {
                throw new IllegalStateException("TIME order requires guests, drinksPerPerson and durationHours");
            }
            totalDrinks = o.guests() * o.drinksPerPerson() * o.durationHours();
        } else {
            totalDrinks = (o.cocktail() == null ? 0 : o.cocktail().stream()
                    .mapToInt(c -> c.quantity() == null ? 0 : c.quantity())
                    .sum());
        }

        // Transformar la lista de cócteles del pedido a un formato adecuado para el PDF
        List<OrderCocktailPdfDto> cocktails = (o.cocktail() == null ? List.of() : o.cocktail().stream()
                .map(c -> new OrderCocktailPdfDto(
                        c.cocktailId(),
                        c.cocktailName(),
                        c.quantity()
                ))
                .toList());
        // Transformar la lista de ítems del pedido a un formato adecuado para el PDF, calculando el total a comprar
        List<OrderItemsPdfDto> items = (o.items() == null ? List.of() : o.items().stream()
                .map(i -> {
                    Integer packs = i.packsToBuy();
                    BigDecimal packSize = i.packSize();

                    BigDecimal totalToBuy =
                            (packs == null || packSize == null)
                                    ? BigDecimal.ZERO
                                    : packSize.multiply(BigDecimal.valueOf(packs));

                    return new OrderItemsPdfDto(
                            i.productId(),
                            i.productName(),
                            packs,
                            packSize,
                            i.measureUnit(),
                            totalToBuy
                    );
                })
                .toList());

        return new OrderPdfDto(
                o.id(),
                mode,
                formatPdfDate(o.createdAt()),
                o.guests(),
                o.drinksPerPerson(),
                o.durationHours(),
                totalDrinks,
                cocktails,
                items
        );
    }

    private void validatePdfAccess(OrderResponseDto order) {
        User currentUser = currentUserService.getCurrentUserOptional().orElse(null);

        boolean isAdmin = currentUser != null && currentUser.getRole() == Role.ADMIN;

        boolean isOwner = currentUser != null && order.userId() != null
                && order.userId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You do not have permission to access this order PDF");
        }
    }

    private String formatPdfDate(Instant createdAt) {
        if (createdAt == null) {
            return "Fecha no disponible";
        }

        return DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.of("America/Argentina/Buenos_Aires"))
                .format(createdAt);
    }

    private byte[] buildPdf(OrderResponseDto responseDto){

        OrderPdfDto orderPdfDto = toPdfDto(responseDto);

        Context context = new Context();
        context.setVariable("order", orderPdfDto);

        String html = templateEngine.process("order-pdf", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.useFastMode();
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new   PdfGenerationException("Error generating PDF", e);
        }
    }
}

