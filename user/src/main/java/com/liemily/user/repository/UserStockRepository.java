package com.liemily.user.repository;

import com.liemily.user.domain.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserStockRepository extends JpaRepository<UserStock, String> {
    @Query
    List<UserStock> findByUsername(String username);
}
