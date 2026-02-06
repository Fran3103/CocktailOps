package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.UserRequestDto;
import com.cocktailops.CocktailOps.dto.UserResponseDto;
import com.cocktailops.CocktailOps.entitie.Shop;
import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.service.IUserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;


    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto createUser = userService.save(userRequestDto);

        URI location = URI.create("/user/" + createUser.id());


        return ResponseEntity.created(location).body(createUser);
    }


    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Shop shop
    ) {

        if (email != null) return ResponseEntity.ok(List.of(userService.findByEmail(email)));
        if (shop != null) return ResponseEntity.ok(List.of(userService.findByShop(shop)));

        List<UserResponseDto> res = userService.findAll();

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updateUser = userService.update(id, userRequestDto);
        return ResponseEntity.ok(updateUser);
    }
}
