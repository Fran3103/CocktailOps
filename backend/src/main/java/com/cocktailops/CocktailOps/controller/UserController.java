package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.userDto.UserRequestDto;
import com.cocktailops.CocktailOps.dto.userDto.UserResponseDto;
import com.cocktailops.CocktailOps.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name =  "Users", description = "Crea y gestiona usuarios ")
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
            @RequestParam(required = false) Long shop,
            @RequestParam(required = false) Long userId
    ) {

        if (email != null) return ResponseEntity.ok(List.of(userService.findByEmail(email)));
        if (shop != null) return ResponseEntity.ok(List.of(userService.findByShop(shop)));
        if (userId != null) return ResponseEntity.ok(List.of(userService.findById(userId)));

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
