package com.application.springboot.restapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserDetailsResource {

    @Autowired
    UserDetailsRepository repository;

    @RequestMapping("/Users")
    public List<UserDetails> retrieveAllUsers() {
        return repository.findAll();
    }

    @RequestMapping("/Users/{userId}")
    public String retrieveUserName(@PathVariable long userId) {
        Optional<UserDetails> user = repository.findById(userId);
        if(user.isEmpty()){
            return null;
        }
        return user.get().getName();
    }

    @RequestMapping(value = "/Users", method = RequestMethod.POST)
    public ResponseEntity<Object> addUser(@RequestBody UserDetails userDetails){
        UserDetails user = repository.save(userDetails);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{questionId}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
