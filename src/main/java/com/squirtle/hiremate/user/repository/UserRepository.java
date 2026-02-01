package com.squirtle.hiremate.user.repository;

import com.squirtle.hiremate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "Select * from user_table where email = :email",nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
}
