package com.liemily.user.repository;

import com.liemily.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Emily Li on 01/10/2017.
 */
public interface UserRepository extends JpaRepository<User, String> {
}
