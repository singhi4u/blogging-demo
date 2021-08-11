package com.nandus.rakesh.blog.demo.blogdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nandus.rakesh.blog.demo.blogdemo.model.Comment;
import com.nandus.rakesh.blog.demo.blogdemo.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByUserAccountId(Long accountId);
}