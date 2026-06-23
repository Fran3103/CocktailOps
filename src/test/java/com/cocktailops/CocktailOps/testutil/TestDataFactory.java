package com.cocktailops.CocktailOps.testutil;

import com.cocktailops.CocktailOps.dto.orderDto.OrderCocktailsWeightDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderRequestDto;
import com.cocktailops.CocktailOps.entitie.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class TestDataFactory {

    private TestDataFactory() {
        // Utility class: no se instancia.
    }

    public static Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    public static Product createProduct(
            Long id,
            String name,
            Category category,
            String unit,
            BigDecimal unitSize
    ) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setUnit(unit);
        product.setUnitSize(unitSize);
        product.setActive(true);
        product.setImageUrl("https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/" + name.toLowerCase() + ".png");
        product.setImageAlt(name);
        return product;
    }

    public static Product createVodka() {
        Category alcohol = createCategory(10L, "Alcohol");

        return createProduct(
                1L,
                "Vodka",
                alcohol,
                "ml",
                new BigDecimal("750")
        );
    }

    public static Product createRum() {
        Category alcohol = createCategory(10L, "Alcohol");

        return createProduct(
                2L,
                "Ron",
                alcohol,
                "ml",
                new BigDecimal("750")
        );
    }

    public static Cocktail createCocktail(
            Long id,
            String name,
            List<CocktailIngredient> ingredients
    ) {
        Cocktail cocktail = new Cocktail();
        cocktail.setId(id);
        cocktail.setName(name);
        cocktail.setIngredients(ingredients);
        return cocktail;
    }

    public static CocktailIngredient createCocktailIngredient(
            Product product,
            BigDecimal amount,
            MeasureUnit unit
    ) {
        CocktailIngredient ingredient = new CocktailIngredient();
        ingredient.setProduct(product);
        ingredient.setAmount(amount);
        ingredient.setUnit(unit);
        return ingredient;
    }

    public static Cocktail createVodkaCocktailWithIngredients() {
        Product vodka = createVodka();

        CocktailIngredient ingredient = createCocktailIngredient(
                vodka,
                new BigDecimal("50"),
                MeasureUnit.ML
        );

        Cocktail cocktail = createCocktail(
                1L,
                "Vodka Tonic",
                new ArrayList<>()
        );

        ingredient.setCocktail(cocktail);
        cocktail.getIngredients().add(ingredient);

        return cocktail;
    }

    public static Order createBasicOrder(Long id) {
        Order order = new Order();
        order.setId(id);
        order.setMode(OrderMode.TIME);
        order.setGuests(50);
        order.setDrinksPerPerson(2);
        order.setDurationHours(4);
        order.setStatus("Draft");
        order.setTotalDrinks(400);
        order.setCocktails(new ArrayList<>());
        order.setOrderItems(new ArrayList<>());
        return order;
    }

    public static OrderCocktail createOrderCocktail(
            Order order,
            Cocktail cocktail,
            Integer drinks
    ) {
        OrderCocktail orderCocktail = new OrderCocktail();
        orderCocktail.setOrder(order);
        orderCocktail.setCocktail(cocktail);
        orderCocktail.setDrinks(drinks);
        return orderCocktail;
    }

    public static OrderItem createOrderItem(
            Order order,
            Product product,
            Integer quantity
    ) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnit(product.getUnit());
        return orderItem;
    }

    public static Order createOrderWithOneCocktailAndOneItem(Long id) {
        Order order = createBasicOrder(id);

        Cocktail cocktail = createVodkaCocktailWithIngredients();
        Product product = cocktail.getIngredients().get(0).getProduct();

        OrderCocktail orderCocktail = createOrderCocktail(order, cocktail, 100);
        OrderItem orderItem = createOrderItem(order, product, 7);

        order.getCocktails().add(orderCocktail);
        order.getOrderItems().add(orderItem);

        return order;
    }

    public static OrderCocktailsWeightDto createOrderCocktailsWeightDto(
            Long cocktailId,
            Integer weight
    ) {
        return new OrderCocktailsWeightDto(cocktailId, weight);
    }

    public static List<OrderCocktailsWeightDto> createValidOrderCocktailsWeightList() {
        return List.of(
                createOrderCocktailsWeightDto(1L, 1)
        );
    }

    public static OrderRequestDto createOrderRequestDto(
            Integer guests,
            Integer durationHours,
            List<OrderCocktailsWeightDto> cocktails
    ) {
        return new OrderRequestDto(
                guests,
                durationHours,
                cocktails
        );
    }

    public static OrderRequestDto createValidOrderRequestDto() {
        return createOrderRequestDto(
                50,
                4,
                createValidOrderCocktailsWeightList()
        );
    }
}