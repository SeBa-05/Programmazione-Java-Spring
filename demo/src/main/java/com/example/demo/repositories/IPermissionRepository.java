package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Permission;
import com.example.demo.entities.PermissionType;

public interface IPermissionRepository extends JpaRepository<Permission, Integer> {

    Optional<Permission> findByType(PermissionType type);
}