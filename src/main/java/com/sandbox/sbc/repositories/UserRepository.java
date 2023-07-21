package com.sandbox.sbc.repositories;

import com.sandbox.sbc.entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<DbUser, Id> {

    @Query(nativeQuery = true, value = "SELECT username FROM users WHERE username :=username")
    Optional<DbUser> findUserByUsername(String username);
}
