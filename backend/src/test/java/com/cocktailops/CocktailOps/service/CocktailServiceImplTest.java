package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailIngredientRequestDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailRequestDto;
import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailResponseDto;
import com.cocktailops.CocktailOps.entitie.Cocktail;
import com.cocktailops.CocktailOps.entitie.MeasureUnit;
import com.cocktailops.CocktailOps.entitie.PreparationType;
import com.cocktailops.CocktailOps.entitie.Product;
import com.cocktailops.CocktailOps.exception.DuplicateResourceException;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.ICocktailIngredientRepository;
import com.cocktailops.CocktailOps.repository.ICocktailRepository;
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.service.impl.CocktailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.cocktailops.CocktailOps.testutil.TestDataFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CocktailServiceImplTest {

    @Mock
    private ICocktailRepository cocktailRepository;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ICocktailIngredientRepository ingredientRepository;

    @InjectMocks
    private CocktailServiceImpl cocktailService;


    @Test
    void create_whenValid_returnsCocktailWithIngredients() {
        Product rum = TestDataFactory.createRum();
        CocktailIngredientRequestDto ingredient =
                new CocktailIngredientRequestDto(
                        rum.getId(),
                        new BigDecimal("1.5"),
                        MeasureUnit.OZ
                );

        CocktailRequestDto request = new CocktailRequestDto(
                "Daiquiri",
                "Cocktail de prueba",
                PreparationType.SHAKEN,
                null,
                "Daiquiri",
                List.of(ingredient)
        );

        when(cocktailRepository.existsByName("Daiquiri"))
                .thenReturn(false);

        when(productRepository.findById(rum.getId()))
                .thenReturn(Optional.of(rum));

        when(cocktailRepository.save(any(Cocktail.class)))
                .thenAnswer(invocation -> {
                    Cocktail cocktail = invocation.getArgument(0);
                    cocktail.setId(10L);
                    return cocktail;
                });

        CocktailResponseDto result = cocktailService.create(request);

        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals("Daiquiri", result.name());
        assertEquals(PreparationType.SHAKEN, result.preparationType());

        assertEquals(1, result.ingredients().size());
        assertEquals(
                rum.getId(),
                result.ingredients().get(0).productId()
        );

        verify(cocktailRepository).save(any(Cocktail.class));
    }


    @Test
    void create_whenNameAlreadyExists_throwsDuplicateResourceException() {

        CocktailRequestDto request = new CocktailRequestDto(
                "Mojito",
                "Cocktail duplicado",
                PreparationType.SHAKEN,
                null,
                "Mojito",
                List.of()
        );

        when(cocktailRepository.existsByName("Mojito"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> cocktailService.create(request)
        );

        verify(cocktailRepository, never()).save(any());
        verifyNoInteractions(productRepository);
    }


    @Test
    void getById_whenNotFound_throwsResourceNotFoundException() {

        when(cocktailRepository.findByWithIngredients(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> cocktailService.getById(99L)
        );
    }


    @Test
    void addIngredient_whenAlreadyExists_throwsDuplicateResourceException() {

        Cocktail cocktail = TestDataFactory.createCocktail(
                1L,
                "Mojito",
                List.of()
        );

        Product rum = TestDataFactory.createRum();

        CocktailIngredientRequestDto ingredient =
                new CocktailIngredientRequestDto(
                        rum.getId(),
                        new BigDecimal("1.5"),
                        MeasureUnit.OZ
                );

        when(cocktailRepository.findById(1L))
                .thenReturn(Optional.of(cocktail));

        when(productRepository.findById(rum.getId()))
                .thenReturn(Optional.of(rum));

        when(
                ingredientRepository.existsByCocktailIdAndProductId(
                        1L,
                        rum.getId()
                )
        ).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> cocktailService.addIngredientToCocktail(
                        1L,
                        List.of(ingredient)
                )
        );

        verify(ingredientRepository, never()).save(any());
    }


    @Test
    void removeIngredient_whenNotFound_throwsResourceNotFoundException() {

        when(
                ingredientRepository.existsByCocktailIdAndProductId(
                        1L,
                        2L
                )
        ).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> cocktailService.removeIngredientFromCocktail(1L, 2L)
        );

        verify(
                ingredientRepository,
                never()
        ).deleteByCocktailIdAndProductId(anyLong(), anyLong());
    }
}