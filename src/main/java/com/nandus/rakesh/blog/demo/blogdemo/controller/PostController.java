package com.nandus.rakesh.blog.demo.blogdemo.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nandus.rakesh.blog.demo.blogdemo.entity.User;
import com.nandus.rakesh.blog.demo.blogdemo.exception.ResourceNotFoundException;
import com.nandus.rakesh.blog.demo.blogdemo.model.Post;
import com.nandus.rakesh.blog.demo.blogdemo.model.UserAccount;
import com.nandus.rakesh.blog.demo.blogdemo.repository.PostRepository;
import com.nandus.rakesh.blog.demo.blogdemo.repository.UserAccountRepository;
import com.nandus.rakesh.blog.demo.blogdemo.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Post> getAllPosts() {
    	//List<UserAccount> accL= userAccountRepository.findAll();
    	//accL.stream().forEach(Post::getUserAccount);
    	//return accL;
        return postRepository.findAll();
    }
    
    @GetMapping("/posts/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Post> getAllPostsForUser(@PathVariable Long userId) {
    	return postRepository.findByUserAccountId(userId);
    	//return (List<Post>) postRepository.findById(userId).get();
        //return postRepository.findAll();
    }

    @PostMapping("/posts")
    public Post createPost(@Valid @RequestBody Post post, Principal principal) {
    	//
    	User user=getLoggedInUser(principal);
    	String userName=user.getUserName();
    	if(userAccountRepository.findByUserName(userName) ==null){
    		UserAccount acc=new UserAccount();
    		acc.setUserName(userName);
    		userAccountRepository.save(acc);
    	}
    	UserAccount uacc=userAccountRepository.findByUserName(userName);
    	return userAccountRepository.findById(uacc.getId()).map(uac -> {
            post.setUserAccount(uac);
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("UserName " + userName + " not found"));
    	//
       // return postRepository.save(post);
    }

    @PutMapping("/posts/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
        return postRepository.findById(postId).map(post -> {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            post.setContent(postRequest.getContent());
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }


    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }
    
    private User getLoggedInUser(Principal principal) {
        return userRepository.findByUserName(principal.getName()).get();
    }

}