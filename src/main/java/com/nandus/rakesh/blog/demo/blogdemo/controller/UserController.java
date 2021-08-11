package com.nandus.rakesh.blog.demo.blogdemo.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nandus.rakesh.blog.demo.blogdemo.common.UserConstant;
import com.nandus.rakesh.blog.demo.blogdemo.entity.User;
import com.nandus.rakesh.blog.demo.blogdemo.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String joinGroup(@RequestBody User user) {
        user.setRoles(UserConstant.DEFAULT_ROLE);//USER
        String encryptedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        repository.save(user);
        return "Hi " + user.getUserName() + " Welcome to Nandus Chicken blogging site group !";
    }
    //If loggedin user is ADMIN -> ADMIN OR MODERATOR
    //If loggedin user is MODERATOR -> MODERATOR

    @GetMapping("/access/{userId}/{userRole}")
    //@Secured("ADMIN")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public String giveAccessToUser(@PathVariable int userId, @PathVariable String userRole, Principal principal) {
        User user = repository.findById(userId).get();
        List<String> activeRoles = getRolesByLoggedInUser(principal);
        String newRole = "";
        if (activeRoles.contains(userRole)) {
            newRole = user.getRoles() + "," + userRole;
            user.setRoles(newRole);
        }
        repository.save(user);
        return "Hi " + user.getUserName() + " New Role assign to you by " + principal.getName();
    }

    @GetMapping
    @Secured("ADMIN")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> loadUsers() {
        return repository.findAll();
    }

    @GetMapping("/login")
   // @PreAuthorize("hasAuthority('USER')")
    public String testUserAccess() {
        return "Your Login was successful !";
    }

    private List<String> getRolesByLoggedInUser(Principal principal) {
        String roles = getLoggedInUser(principal).getRoles();
        List<String> assignRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());
        if (assignRoles.contains("ADMIN")) {
            return Arrays.stream(UserConstant.ADMIN_ACCESS).collect(Collectors.toList());
        }
        if (assignRoles.contains("MODERATOR")) {
            return Arrays.stream(UserConstant.MODERATOR_ACCESS).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private User getLoggedInUser(Principal principal) {
        return repository.findByUserName(principal.getName()).get();
    }
}
