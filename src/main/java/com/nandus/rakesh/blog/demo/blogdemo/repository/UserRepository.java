package com.nandus.rakesh.blog.demo.blogdemo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nandus.rakesh.blog.demo.blogdemo.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUserName(String username);
    
}
