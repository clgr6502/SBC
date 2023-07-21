package com.sandbox.sbc.repositories;

import com.sandbox.sbc.entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DbUserRepository extends JpaRepository<DbUser, Long> {

    Optional<DbUser> findByUsername(String username);
}
