package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.OrderCocktailPdfDto;
import com.cocktailops.CocktailOps.dto.OrderItemsPdfDto;
import com.cocktailops.CocktailOps.dto.OrderPdfDto;
import com.cocktailops.CocktailOps.dto.OrderResponseDto;
import com.cocktailops.CocktailOps.service.IOrderService;
import com.cocktailops.CocktailOps.service.IOrderPdfService;
import com.cocktailops.CocktailOps.service.IProductService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPdfServiceImpl implements IOrderPdfService {

    private final TemplateEngine templateEngine;

    private final IOrderService orderService;

    private final IProductService productService;

    private static final DateTimeFormatter PDF_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.of("America/Argentina/Buenos_Aires"));

    @Override
    public byte[] generateOrderPdf(Long orderId) throws BadRequestException {


        OrderResponseDto responseDto = orderService.getOrderById(orderId);

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
            throw new BadRequestException("Error generating PDF", e);
        }
    }

    private OrderPdfDto toPdfDto(OrderResponseDto o) {

        String mode = (o.guests() != null && o.durationHours() != null) ? "TIME" : "DRINKS";

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

        List<OrderCocktailPdfDto> cocktails = (o.cocktail() == null ? List.of() : o.cocktail().stream()
                .map(c -> new OrderCocktailPdfDto(
                        c.cocktailId(),
                        c.cocktailName(),
                        c.quantity()
                ))
                .toList());

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
}

