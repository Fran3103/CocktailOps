package com.cocktailops.CocktailOps.security;

import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final IUserRepository repository;

    public User getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){

            throw new AccessDeniedException("User is not authenticated");
        }

        String email = authentication.getName();

        return repository.findByEmail(email)
                .orElseThrow(()-> new AccessDeniedException("Authenticated user not found"));
    }

}
