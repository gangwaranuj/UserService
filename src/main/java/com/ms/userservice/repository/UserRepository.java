package com.ms.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.userservice.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

}
