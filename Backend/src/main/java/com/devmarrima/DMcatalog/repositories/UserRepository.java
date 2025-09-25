package com.devmarrima.DMcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devmarrima.DMcatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
