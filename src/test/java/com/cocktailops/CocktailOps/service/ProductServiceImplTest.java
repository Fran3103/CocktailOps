package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.productDto.ProductRequestDto;
import com.cocktailops.CocktailOps.dto.productDto.ProductResponseDto;
import com.cocktailops.CocktailOps.entitie.Category;
import com.cocktailops.CocktailOps.entitie.Product;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.ICategoryRepository;
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private ICategoryRepository categoryRepository;

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
        verifyNoInteractions(categoryRepository);
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
    }

    @Test
    void create_whenRequestIsValid_savesAndReturnsProductDto() {
        Category category = new Category();
        category.setId(10L);
        category.setName("Alcohol");



        Product product = new Product();


        product.setId(1L);
        product.setName("Vodka");
        product.setUnit("ml");
        product.setImageAlt("Vodka Sernova");
        product.setImageUrl("https://res.cloudinary.com/dzj8q4q");
        product.setActive(true);
        product.setUnitSize(new BigDecimal("750"));
        product.setCategory(category);

        ProductRequestDto requestDto = new ProductRequestDto(
                1L,
                "Vodka",
                10L,
                "ml",
                new BigDecimal("750"),
                true,
                "https://res.cloudinary.com/dzj8q4q",
                "Vodka Sernova"
        );

        when(productRepository.existsByName("Vodka")).thenReturn(false);
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);


        ProductResponseDto result = productServiceImpl.create(requestDto);


        assertNotNull(result);
        assertEquals(1L,result.productId());
        assertEquals("Vodka", result.name());
        assertEquals("ml", result.unit());
        assertEquals("Vodka Sernova", result.imageAlt());
        assertTrue(result.active());
        assertEquals(new BigDecimal("750"), result.unitSize());
        assertEquals(10L, result.category());


        verify(productRepository).existsByName("Vodka");
        verify(categoryRepository).findById(10L);
        verify(productRepository).save(any(Product.class));
        verifyNoMoreInteractions(productRepository, categoryRepository);

    }

    @Test
    void create_whenProductNameAlreadyExists_thenThrowResourceNotFoundException() {
        ProductRequestDto requestDto = new ProductRequestDto(
                1L,
                "Vodka",
                10L,
                "ml",
                new BigDecimal("750"),
                true,
                "https://res.cloudinary.com/dzj8q4q",
                "Vodka Sernova"
        );

        when(productRepository.existsByName("Vodka")).thenReturn(true);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productServiceImpl.create(requestDto);
        });

        assertEquals("Product with name Vodka already exists", exception.getMessage());

        verify(productRepository).existsByName("Vodka");
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void create_whenCategoryDoesNotExist_thenThrowResourceNotFoundException() {
        ProductRequestDto requestDto = new ProductRequestDto(
                1L,
                "Vodka",
                10L,
                "ml",
                new BigDecimal("750"),
                true,
                "https://res.cloudinary.com/dzj8q4q",
                "Vodka Sernova"
        );

        when(productRepository.existsByName("Vodka")).thenReturn(false);
        when(categoryRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productServiceImpl.create(requestDto);
        });

        assertEquals("Category with id 10 not found", exception.getMessage());

        verify(productRepository).existsByName("Vodka");
        verify(categoryRepository).findById(10L);
        verifyNoMoreInteractions(productRepository, categoryRepository);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void update_whenProductAndCategoryExist_updatesAndReturnsProductDto() {

        Long productId = 1L;


        Category category = new Category();
        category.setId(10L);
        category.setName("Spirits");

        Product product = new Product();
        product.setId(productId);
        product.setCategory(category);
        product.setName("Vodka");
        product.setUnit("ml");
        product.setImageAlt("Vodka Sernova");
        product.setImageUrl("https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/vodka_sernova.png");
        product.setActive(true);
        product.setUnitSize(new BigDecimal(750));

        ProductRequestDto updateDto = new ProductRequestDto(
                1L,
                "Vodka Updated",
                10L,
                "ml",
                new BigDecimal("750"),
                true,
                "https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/vodka_sernova_updated.png",
                "Vodka Sernova"
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);


        ProductResponseDto result = productServiceImpl.update(productId, updateDto);

        assertNotNull(result);
        assertEquals(1L,result.productId());
        assertEquals("Vodka Updated", result.name());
        assertEquals("ml", result.unit());
        assertEquals("Vodka Sernova", result.imageAlt());
        assertTrue(result.active());
        assertEquals(750d, result.unitSize().doubleValue());
        assertEquals(10L, result.category());

        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(10L);
        verify(productRepository).save(any(Product.class));
        verifyNoMoreInteractions(productRepository, categoryRepository);

    }


    @Test
    void update_whenCategoryDoesNotExist_thenThrowResourceNotFoundException() {

        Long productId = 1L;

        Category oldCategory = new Category();
        oldCategory.setId(5L);
        oldCategory.setName("Old category");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Vodka");
        existingProduct.setCategory(oldCategory);
        existingProduct.setUnit("ml");
        existingProduct.setUnitSize(new BigDecimal("700"));
        existingProduct.setActive(true);
        existingProduct.setImageUrl("old-url");
        existingProduct.setImageAlt("old-alt");

        ProductRequestDto requestDto = new ProductRequestDto(
                1L,
                "Vodka actualizado",
                10L,
                "ml",
                new BigDecimal("750"),
                true,
                "https://res.cloudinary.com/dzj8q4q",
                "Vodka Sernova actualizado"
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(10L)).thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productServiceImpl.update(productId, requestDto)
        );

        assertEquals("Category with id 10 not found", exception.getMessage());

        // Verify
        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(10L);
        verify(productRepository, never()).save(any(Product.class));
        verifyNoMoreInteractions(productRepository, categoryRepository);

    }

    @Test
    void update_whenProductDoesNotExist_throwsResourceNotFoundException(){

        Long productId = 1L;

        ProductRequestDto requestDto = new ProductRequestDto(
                1L,
                "Vodka actualizado",
                10L,
                "ml",
                new BigDecimal("750"),
                true,
                "https://res.cloudinary.com/dzj8q4q",
                "Vodka Sernova actualizado"
        );

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productServiceImpl.update(productId, requestDto)
        );

        assertEquals("Product not found with id: " + productId, exception.getMessage());

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }
}
