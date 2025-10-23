package com.springboot.jwtlogin.data.repository;

import com.springboot.jwtlogin.data.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByUid(String uid);

}
