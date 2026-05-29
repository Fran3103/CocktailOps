package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.productDto.ProductRequestDto;
import com.cocktailops.CocktailOps.dto.productDto.ProductResponseDto;
import com.cocktailops.CocktailOps.entitie.Category;
import com.cocktailops.CocktailOps.entitie.Product;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;


    @Test
    void findById_whenProductExists_thenReturnProductDto() {
        // Arrange: preparación del escenario previo a la ejecución.
        // Aquí se crean y configuran los objetos necesarios, datos de prueba y los mocks
        // para que devuelvan las respuestas esperadas (por ejemplo: crear un producto
        // esperado y configurar productService para que lo devuelva).


        Long productId = 1L;

        Category category = new Category();
        category.setId(10L);

        Product product = new Product();
        product.setId(productId);
        product.setCategory(category);
        product.setName("Vodka");
        product.setUnit("ml");
        product.setImageAlt("Vodka Sernova");
        product.setImageUrl("https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/vodka_sernova.png");
        product.setActive(true);
        product.setUnitSize(new BigDecimal(750));

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act: ejecución de la acción o método bajo prueba.
        // Normalmente se invoca el método del servicio que queremos probar y se
        // captura su resultado (por ejemplo: productServiceImpl.findById(id)).


        ProductResponseDto result = productServiceImpl.findById(productId);

        // Assert: verificación de resultados y comportamientos.
        // Se comprueba que el resultado coincide con lo esperado mediante aserciones
        // (assertEquals, assertTrue, etc.) y se pueden verificar interacciones con
        // los mocks usando verify(mock).


        assertNotNull(result);
        assertEquals(1L,result.productId());
        assertEquals("Vodka", result.name());
        assertEquals("ml", result.unit());
        assertEquals("Vodka Sernova", result.imageAlt());
        assertTrue(result.active());
        assertEquals(750d, result.unitSize().doubleValue());
        assertEquals(10L, result.category());
        assertEquals("https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/vodka_sernova.png", result.imageUrl());

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryService);
    }

    @Test
    void findById_whenProductDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        Long productId = 14L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

       // Act

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productServiceImpl.findById(productId);
        });

        //Assert

        assertEquals("Product not found with id: " + productId, exception.getMessage());


        //verify
        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryService);
    }

}
