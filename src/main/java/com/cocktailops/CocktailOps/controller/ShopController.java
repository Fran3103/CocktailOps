package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.ShopRequestDto;
import com.cocktailops.CocktailOps.dto.ShopResponseDto;
import com.cocktailops.CocktailOps.service.IShopService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final IShopService shopService;


    @PostMapping()
    ResponseEntity<ShopResponseDto> addShop(@RequestBody ShopRequestDto shopRequestDto){

        ShopResponseDto shopResponseDto = shopService.createShop(shopRequestDto);

        return ResponseEntity.ok(shopResponseDto);
    }

    @GetMapping()
    ResponseEntity <List<ShopResponseDto>> getAllShops(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) Long id
    ){

        if(name != null && !name.isBlank()) return ResponseEntity.ok(List.of(shopService.findByName(name)));
        if (id != null) return ResponseEntity.ok(List.of(shopService.findById(id)));

        return ResponseEntity.ok(shopService.findAll());
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    ResponseEntity<ShopResponseDto> updateShop(@PathVariable Long id, @RequestBody ShopRequestDto Dto) {
        ShopResponseDto updatedShop = shopService.updateShop(id, Dto);
        return ResponseEntity.ok(updatedShop);
    }
}
