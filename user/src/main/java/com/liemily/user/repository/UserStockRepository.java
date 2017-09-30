package com.liemily.user.repository;

import com.liemily.user.domain.UserStock;
import com.liemily.user.domain.UserStockId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserStockRepository extends JpaRepository<UserStock, UserStockId> {
    @Query
    List<UserStock> findByUsername(String username, Pageable pageable);

    @Query
    List<UserStock> findByUsernameAndSymbolContainingIgnoreCase(String username, String symbol, Pageable stocksPageable);

    @Query("FROM UserStock WHERE username = ?1 AND stockView.company.name LIKE %?2%")
    List<UserStock> findByUsernameAndNameContainingIgnoreCase(String username, String name, Pageable stocksPageable);
}
