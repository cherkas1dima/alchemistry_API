package com.alchemistry.utils;

import com.alchemistry.entities.Ingredient;
import com.alchemistry.entities.IngredientType;
import com.alchemistry.entities.User;
import com.alchemistry.entities.UserRoles;
import com.alchemistry.security.jwt.JwtUser;
import com.alchemistry.transformers.impl.JwtUserTransformer;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public static Integer changeAmount(Integer amount, Integer value, boolean isIncrement) {
        return isIncrement
                ? amount + value
                : amount - value;
    }

    public static boolean ifContains(List<Object> list, Object entity) {
        return list.contains(entity);
    }

    public static boolean isElixir(Ingredient item) {
        return item.getType().equals(IngredientType.ELIXIR);
    }

    public static boolean consumingProbability(IngredientType type) {
        switch (type) {
            case POWDER:
            case HERB: return new Random().nextInt(25) == 0;
            case ELIXIR:
            case LIQUID: return new Random().nextInt(50) == 0;
            default: return true;
        }
    }
}
