package com.alchemistry.utils;

import com.alchemistry.entities.User;
import com.alchemistry.entities.UserRoles;
import com.alchemistry.security.jwt.JwtUser;
import com.alchemistry.transformers.impl.JwtUserTransformer;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class AlchemyUtils {

    public static User getUserFromContextHolder() {
        return JwtUserTransformer.jwtToUser(
                (JwtUser) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal());
    }

    public static User setRole(UserRoles role, User user) {
        List<UserRoles> userRoles = new ArrayList<>();
        userRoles.add(role);
        user.setRoles(userRoles);
        return user;
    }

    public static boolean ifRemoveItem(List<Object> userItems, Object item) {
        return userItems.removeIf(i -> i.equals(item));
    }
}
