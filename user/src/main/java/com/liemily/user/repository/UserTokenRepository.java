package com.liemily.user.repository;

import com.liemily.user.domain.UserToken;
import com.liemily.user.domain.UserTokenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Emily Li on 08/10/2017.
 */
public interface UserTokenRepository extends JpaRepository<UserToken, UserTokenId> {
    @Query
    UserToken findByToken(String token);
}
