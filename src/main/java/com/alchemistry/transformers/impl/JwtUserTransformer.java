package com.alchemistry.transformers.impl;

import com.alchemistry.entities.User;
import com.alchemistry.entities.UserRoles;
import com.alchemistry.security.jwt.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserTransformer {

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getName(),
                user.getE_mail(),
                user.getPassword(),
                user.getCoins(),
                user.getElixirs(),
                user.getIngredients(),
                mapToGrantedAuthorities(new ArrayList<>(user.getRoles()))
        );
    }

    public static User jwtToUser(JwtUser jwtUser) {
        return User.anUser()
                .setId(jwtUser.getId())
                .setName(jwtUser.getUsername())
                .setE_mail(jwtUser.getEmail())
                .setPassword(jwtUser.getPassword())
                .setCoins(jwtUser.getCoins())
                .setElixirs(jwtUser.getElixirs())
                .setIngredients(jwtUser.getIngredients())
                .build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<UserRoles> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
