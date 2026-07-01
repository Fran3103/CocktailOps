package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.authDto.AuthResponseDto;
import com.cocktailops.CocktailOps.dto.authDto.LoginRequestDto;
import com.cocktailops.CocktailOps.dto.authDto.RegisterRequestDto;
import com.cocktailops.CocktailOps.entitie.Role;
import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.exception.BadRequestException;
import com.cocktailops.CocktailOps.exception.DuplicateResourceException;
import com.cocktailops.CocktailOps.repository.IUserRepository;
import com.cocktailops.CocktailOps.security.JwtService;
import com.cocktailops.CocktailOps.service.IAuthService;
import com.cocktailops.CocktailOps.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IAuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    @Override
    public AuthResponseDto register(RegisterRequestDto dto) {

        if (userRepository.existsByEmail(dto.email())){
            throw new DuplicateResourceException("User with email " + dto.email() + " already exists");
        }

        User user = new User();

        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER);
        user.setShop(null);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return toResponse(savedUser, token);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(dto.password(), user.getPassword());

        if (!passwordMatches) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return toResponse(user, token);
    }


    private AuthResponseDto toResponse(User user, String token) {
        return new AuthResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                token
        );
    }
}
