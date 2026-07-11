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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        assertEquals(10L, result.categoryId());
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
        assertEquals(10L, result.categoryId());
        assertEquals("Alcohol", result.categoryName());


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
        assertEquals(10L, result.categoryId());
        assertEquals("Spirits", result.categoryName());

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


    @Test
    void delete_whenProductExists_deletesProduct() {
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productServiceImpl.delete(productId);

        verify(productRepository).findById(productId);
        verify(productRepository).delete(product);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void delete_whenProductDoesNotExist_throwsResourceNotFoundException() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productServiceImpl.delete(productId)
        );

        assertEquals("Product not found with id: " + productId, exception.getMessage());


        verify(productRepository).findById(productId);
        verify(productRepository, never()).delete(any(Product.class));
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }


    @Test
    void findAll_whenProductExists_returnsResponseProductsDtoList() {

        Category category = new Category();
        category.setId(10L);
        category.setName("Alcohol");

        Product product = new Product();
        product.setId(1L);
        product.setName("Vodka");
        product.setCategory(category);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Ron");
        product2.setCategory(category);

        Product product3 = new Product();
        product3.setId(3L);
        product3.setName("Whisky");
        product3.setCategory(category);

        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        products.add(product3);

        when(productRepository.findAllWithCategory()).thenReturn(products);

        List<ProductResponseDto> result = productServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(1L, result.get(0).productId());
        assertEquals("Vodka", result.get(0).name());
        assertEquals(10L, result.get(0).categoryId());
        assertEquals("Alcohol", result.get(0).categoryName());

        assertEquals(2L, result.get(1).productId());
        assertEquals("Ron", result.get(1).name());
        assertEquals(10L, result.get(1).categoryId());
        assertEquals("Alcohol", result.get(1).categoryName());

        assertEquals(3L, result.get(2).productId());
        assertEquals("Whisky", result.get(2).name());
        assertEquals(10L, result.get(2).categoryId());
        assertEquals("Alcohol", result.get(2).categoryName());

        verify(productRepository).findAllWithCategory();
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void findAll_whenProductsDoesNotExist_ReturnsEmptyList() {

        when(productRepository.findAllWithCategory()).thenReturn(Collections.emptyList());

        List<ProductResponseDto> result = productServiceImpl.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository).findAllWithCategory();
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }


    @Test
    void findByName_whenProductExists_returnsResponseProductDto() {
        String name = "Vodka";

        Category category = new Category();
        category.setId(10L);
        category.setName("Alcohol");

        Product product = new Product();
        product.setId(1L);
        product.setName("Vodka");
        product.setCategory(category);
        product.setUnit("ml");
        product.setImageUrl("https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/vodka_sernova.png");
        product.setImageAlt("Vodka Sernova");
        product.setActive(true);
        product.setUnitSize(new BigDecimal("750"));

        when(productRepository.findByName(name)).thenReturn(Optional.of(product));

        ProductResponseDto result = productServiceImpl.findByName(name);

        assertNotNull(result);
        assertEquals(1L, result.productId());
        assertEquals("Vodka", result.name());
        assertEquals(10L, result.categoryId());
        assertEquals("Alcohol", result.categoryName());
        assertEquals("ml", result.unit());
        assertEquals("https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/vodka_sernova.png", result.imageUrl());
        assertEquals("Vodka Sernova", result.imageAlt());
        assertTrue(result.active());
        assertEquals(new BigDecimal("750"), result.unitSize());

        verify(productRepository).findByName(name);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }


    @Test
    void findByName_whenProductDoesNotExist_throwsResourceNotFoundException() {
        String name = "Vodka";

        when(productRepository.findByName(name)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productServiceImpl.findByName(name);
        });

        assertEquals("Product not found by name: " + name , exception.getMessage());

        verify(productRepository).findByName(name);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void findByCategoryName_whenProductExists_returnsProductResponseDto() {

        String categoryName1 = "alcohol";

        Category alcohol = new Category();
        alcohol.setId(10L);
        alcohol.setName("Alcohol");

        Category sinAlcohol = new Category();
        sinAlcohol.setId(20L);
        sinAlcohol.setName("Sin Alcohol");

        Product product = new Product();
        product.setCategory(alcohol);
        product.setId(1L);
        product.setName("Vodka");
        product.setUnit("ml");
        product.setImageAlt("Vodka Sernova");
        product.setImageUrl("https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/vodka_sernova.png");
        product.setActive(true);
        product.setUnitSize(new BigDecimal("750"));

        Product product2 = new Product();
        product2.setCategory(sinAlcohol);
        product2.setId(2L);
        product2.setName("Jugo");
        product2.setUnit("ml");
        product2.setImageAlt("Jugo de Naranja");
        product2.setImageUrl("https://res.cloudinary.com/dzj8q4qeu/image/upload/v1700000000/products/jugo_naranja.png");
        product2.setActive(true);
        product2.setUnitSize(new BigDecimal("250"));

        List<Product> productList = List.of(product, product2);

        when(productRepository.findAllWithCategory()).thenReturn(productList);

        List<ProductResponseDto> result = productServiceImpl.findByCategoryName(categoryName1);

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(1L, result.get(0).productId());
        assertEquals("Vodka", result.get(0).name());
        assertEquals(10L, result.get(0).categoryId());
        assertEquals("Alcohol", result.get(0).categoryName());
        assertEquals("ml", result.get(0).unit());
        assertEquals("Vodka Sernova", result.get(0).imageAlt());
        assertTrue(result.get(0).active());
        assertEquals(new BigDecimal("750"), result.get(0).unitSize());

        verify(productRepository).findAllWithCategory();
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }


    @Test
    void findByCategoryName_whenCategoryDoesNotExist_returnsEmptyList() {

        String categoryName = "alcohol";

        Category sinAlcohol = new Category();
        sinAlcohol.setId(1L);
        sinAlcohol.setName("Sin Alcohol");

        Product product = new Product();
        product.setCategory(sinAlcohol);
        product.setId(1L);
        product.setName("Vodka");
        product.setUnit("ml");
        product.setImageAlt("Vodka Sernova");
        product.setActive(true);
        product.setUnitSize(new BigDecimal("750"));

        List<Product> productList = List.of(product);

        when(productRepository.findAllWithCategory()).thenReturn(productList);

        List<ProductResponseDto> result = productServiceImpl.findByCategoryName(categoryName);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository).findAllWithCategory();
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void findByCategoryName_whenNoProductsExist_returnsEmptyList() {

        String categoryName = "alcohol";

        when(productRepository.findAllWithCategory()).thenReturn(Collections.emptyList());

        List<ProductResponseDto> result = productServiceImpl.findByCategoryName(categoryName);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository).findAllWithCategory();
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(categoryRepository);
    }
}
