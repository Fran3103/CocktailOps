package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.orderDto.OrderCocktailPdfDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderItemsPdfDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderPdfDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderResponseDto;
import com.cocktailops.CocktailOps.entitie.Role;
import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.security.CurrentUserService;
import com.cocktailops.CocktailOps.service.IOrderService;
import com.cocktailops.CocktailOps.service.IOrderPdfService;
import com.cocktailops.CocktailOps.service.IProductService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
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
    public byte[] generateOrderPdf(Long orderId) throws BadRequestException {

        log.info("Generating PDF for order with id: {}", orderId);

        // Obtener los datos del pedido
        OrderResponseDto responseDto = orderService.getOrderById(orderId);

        validatePdfAccess(responseDto);
        // Transformar los datos a un formato adecuado para el PDF
        OrderPdfDto orderPdfDto = toPdfDto(responseDto);
        // Renderizar el PDF usando Thymeleaf y OpenHTMLToPDF
        Context context = new Context();
        // Agregar los datos del pedido al contexto de Thymeleaf
        context.setVariable("order", orderPdfDto);

        // Renderizar la plantilla Thymeleaf con los datos del pedido
        String html = templateEngine.process("order-pdf", context);

        // Generar el PDF a partir del HTML renderizado
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.useFastMode();
            builder.run();
            log.info("PDF generated successfully for order with id: {}", orderId);
            return outputStream.toByteArray();
        } catch (Exception e) {
                log.error("Error generating PDF for order with id: {}", orderId, e);
            throw new BadRequestException("Error generating PDF", e);
        }
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
                o.createdAt() == null ? "" : o.createdAt().toString(),
                o.guests(),
                o.drinksPerPerson(),
                o.durationHours(),
                totalDrinks,
                cocktails,
                items
        );
    }

    private void validatePdfAccess(OrderResponseDto order) {
        User currentUser = currentUserService.getCurrentUser();

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        boolean isOwner = order.userId() != null
                && order.userId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You do not have permission to access this order PDF");
        }
    }
}

