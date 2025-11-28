package com.example.cdcservice.controller;

import com.example.cdcservice.entity.Users;
import com.example.cdcservice.model.UserCreateRequest;
import com.example.cdcservice.repo.UsersRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersRepository userRepo;

    public UsersController(UsersRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping
    public Users createUser(
            @RequestBody UserCreateRequest request
    ) {
        Users user = Users.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return userRepo.save(user);
    }

    @PutMapping("/update/{id}")
    public Users updateUser(
            @RequestBody UserCreateRequest request,
            @PathVariable Long id
    ) {
        Users existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        existingUser.setUpdatedAt(Instant.now());
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhoneNumber(request.getPhoneNumber());

        return userRepo.save(existingUser);
    }

    @PatchMapping("/toggle/{id}")
    public Users toggleActive(
            @PathVariable Long id
    ) {
        Users existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        existingUser.setActive(!existingUser.isActive());
        existingUser.setUpdatedAt(Instant.now());

        return userRepo.save(existingUser);
    }
}
