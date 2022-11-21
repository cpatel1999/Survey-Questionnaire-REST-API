package com.application.springboot.restapi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

//JpaRepository<Entity, type_of_Id_field>
public interface UserDetailsRestRepository extends PagingAndSortingRepository<UserDetails, Long> {
    List<UserDetails> findByRole(String role);
}
