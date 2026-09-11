package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.authDto.LoginRequestDto;
import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.exception.InvalidCredentialsException;
import com.cocktailops.CocktailOps.repository.IUserRepository;
import com.cocktailops.CocktailOps.security.JwtService;
import com.cocktailops.CocktailOps.service.impl.IAuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IAuthServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private IAuthServiceImpl authService;

    @Test
    void login_whenEmailDoesNotExist_throwsInvalidCredentialsException() {

        LoginRequestDto dto = new LoginRequestDto(
                "test@mail.com",
                "password123"
        );

        when(userRepository.findByEmail(dto.email()))
                .thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(dto)
        );

        assertEquals("Invalid email or password", exception.getMessage());

        verify(userRepository).findByEmail(dto.email());
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void login_whenPasswordIsIncorrect_throwsInvalidCredentialsException() {

        LoginRequestDto dto = new LoginRequestDto(
                "test@mail.com",
                "wrongPassword"
        );

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(dto.email()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(dto.password(), user.getPassword()))
                .thenReturn(false);

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(dto)
        );

        assertEquals("Invalid email or password", exception.getMessage());

        verify(userRepository).findByEmail(dto.email());
        verify(passwordEncoder).matches(dto.password(), user.getPassword());
        verifyNoInteractions(jwtService);
    }
}