package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.ShopRequestDto;
import com.cocktailops.CocktailOps.dto.ShopResponseDto;
import com.cocktailops.CocktailOps.entitie.Shop;
import com.cocktailops.CocktailOps.exception.DuplicateResourceException;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.IShopRepository;
import com.cocktailops.CocktailOps.service.IShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements IShopService {

    private  final IShopRepository iShopRepository;

    @Override
    public ShopResponseDto findById(long id) {
        Shop shop = iShopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id " + id + " not found"));

        return new ShopResponseDto(shop.getId(), shop.getName(), shop.getSlug());
    }

    @Override
    public ShopResponseDto findByName(String name) {
        Shop shop = iShopRepository.findByName(name);
        if (shop == null) {
            throw new ResourceNotFoundException("Shop with name " + name + " not found");
        }

        return new ShopResponseDto(shop.getId(), shop.getName(), shop.getSlug());
    }




    @Override
    public ShopResponseDto createShop(ShopRequestDto Dto) {

      if(iShopRepository.existsBySlug(Dto.slug())){
                    throw new DuplicateResourceException("Shop with slug " + Dto.slug() + " already exists");
                }

        Shop shop = new Shop();

        shop.setName(Dto.name());
        shop.setSlug(Dto.slug());

        Shop savedShop = iShopRepository.save(shop);

        return new ShopResponseDto(savedShop.getId(), savedShop.getName(), savedShop.getSlug());
    }

    @Override
    public ShopResponseDto updateShop(Long id, ShopRequestDto Dto) {
        Shop shop = iShopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id " + id + " not found"));


        shop.setName(Dto.name() != null ? Dto.name() : shop.getName());
        shop.setSlug(Dto.slug() != null ? Dto.slug() : shop.getSlug());

        Shop updatedShop = iShopRepository.save(shop);
        return new ShopResponseDto(updatedShop.getId(), updatedShop.getName(), updatedShop.getSlug());
    }

    @Override
    public void deleteShop(Long id) {
        Shop shop = iShopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop with id " + id + " not found"));

        iShopRepository.delete(shop);


    }

    @Override
    public List<ShopResponseDto> findAll() {

        List<Shop> shops = iShopRepository.findAll();
        return shops.stream()
                .map(shop -> new ShopResponseDto(shop.getId(), shop.getName(), shop.getSlug()))
                .toList();
    }
}
