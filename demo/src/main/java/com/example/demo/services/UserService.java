package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Permission;
import com.example.demo.entities.PermissionType;
import com.example.demo.entities.User;
import com.example.demo.repositories.IUserRepository;
import com.example.demo.repositories.IPermissionRepository;

@Service
public class UserService {

    private final IUserRepository userRepo;
    private final IPermissionRepository permissionRepo;

    public UserService(IUserRepository userRepo, IPermissionRepository permissionRepo) {
        this.userRepo = userRepo;
        this.permissionRepo = permissionRepo;
    }

    // Registrazione con ruolo specificato
    public User register(String username, String password, String email, PermissionType role) {
        // Controlla se username o email sono già usati
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username già esistente!");
        }
        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email già registrata!");
        }

        // Cerca o crea il permesso
        Permission permission = permissionRepo.findByType(role)
                .orElseGet(() -> {
                    Permission p = new Permission();
                    p.setType(role);
                    return permissionRepo.save(p);
                });

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // in chiaro per ora (da migliorare con BCrypt)
        user.setEmail(email);
        user.setPermission(permission);

        return userRepo.save(user);
    }

    // Login
    public User login(String username, String password) {
        Optional<User> opt = userRepo.findByUsername(username);
        if (opt.isPresent()) {
            User user = opt.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public Optional<User> findById(Integer id) {
        return userRepo.findById(id);
    }
    
    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void deleteById(Integer id) {
        userRepo.deleteById(id);
    }
}