package com.alchemistry.conrollers;

import com.alchemistry.dto.modelsdto.UserDto;
import com.alchemistry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/alchemy/api/v1/admin/")
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("list")
    public ResponseEntity<List<UserDto>> getAll() {
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            throw new RuntimeException("Getting user List ERROR");
        }
    }

    @GetMapping("id/{user_id}")
    public ResponseEntity<UserDto> getById(@PathVariable(value = "user_id") String id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (Exception e) {
            throw new RuntimeException("Getting user by id ERROR");
        }
    }

    @GetMapping("name/{user_name}")
    public ResponseEntity<UserDto> getByName(@PathVariable(value = "user_name") String name) {
        try {
            return ResponseEntity.ok(userService.getByName(name));
        } catch (Exception e) {
            throw new RuntimeException("Getting user by name ERROR");
        }
    }

    @DeleteMapping("delete/{user_id}")
    public ResponseEntity.BodyBuilder delete(@PathVariable(value = "user_id") String id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Deleting user ERROR");
        }
    }
}
