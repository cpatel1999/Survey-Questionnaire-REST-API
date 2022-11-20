package com.application.springboot.restapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

//JpaRepository<Entity, type_of_Id_field>
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
