package com.nandus.rakesh.blog.demo.blogdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nandus.rakesh.blog.demo.blogdemo.model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	UserAccount findByUserName(String userName);
}
