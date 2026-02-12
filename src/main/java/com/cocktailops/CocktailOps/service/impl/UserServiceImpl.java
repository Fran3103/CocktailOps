package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.UserRequestDto;
import com.cocktailops.CocktailOps.dto.UserResponseDto;
import com.cocktailops.CocktailOps.entitie.Shop;
import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.exception.DuplicateResourceException;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.IShopRepository;
import com.cocktailops.CocktailOps.repository.IUserRepository;
import com.cocktailops.CocktailOps.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IShopRepository shopRepository;


    @Override
    public UserResponseDto findByEmail(String email) {

        UserResponseDto user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User with email " + email + "not found");
        }

        return user;

    }

    // falta implementar la busca del shop, si no hay shop avisar que ese shop no existe.
    @Override
    public UserResponseDto findByShop(Long shop) {
        UserResponseDto user = userRepository.findByShop(shop);

        if (user == null) {
            throw new ResourceNotFoundException("User with shop " + shop + "not found");
        }

        return user;
    }

    @Override
    public UserResponseDto findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));



        return new UserResponseDto( user.getFirstName(),user.getLastName(), user.getShop().getId(), user.getId(), user.getRole(), user.getEmail());
    }

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.email())){
            throw new DuplicateResourceException("User with email " + userRequestDto.email() + " already exists");
        }

        User user = new User();

        Shop shop = shopRepository.findById(userRequestDto.shop())
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id " + userRequestDto.shop() + " not found"));

        user.setEmail(userRequestDto.email());
        user.setFirstName(userRequestDto.firstName());
        user.setLastName(userRequestDto.lastName());
        user.setShop(shop);
        user.setPassword(userRequestDto.password());
        user.setRole(userRequestDto.role());


        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getShop().getId(),  savedUser.getId(), savedUser.getRole(),  savedUser.getEmail());
    }

    public UserResponseDto update(long id, UserRequestDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        if (dto.firstName() != null) user.setFirstName(dto.firstName());
        if (dto.lastName()  != null) user.setLastName(dto.lastName());


        if (dto.shop() != null) {;
            Shop shop = shopRepository.findById(dto.shop())
                    .orElseThrow(() -> new ResourceNotFoundException("Shop with id " + dto.shop() + " not found"));
            user.setShop(shop);
        }

        if (dto.password() != null) user.setPassword(dto.password());
        if (dto.role() != null) user.setRole(dto.role());

        User saved = userRepository.save(user);

        return new UserResponseDto(
                saved.getFirstName(),
                saved.getLastName(),
                saved.getShop().getId(),
                saved.getId(),
                saved.getRole(),
                saved.getEmail()
        );
    }

    @Override
    public void delete(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));

        userRepository.delete(user);
    }

    @Override
    public List<UserResponseDto> findAll(){
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(u -> new UserResponseDto(u.getFirstName(), u.getLastName(), u.getShop().getId(), u.getId(), u.getRole(), u.getEmail() ))
                .toList();

    }
}
