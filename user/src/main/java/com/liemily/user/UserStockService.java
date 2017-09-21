package com.liemily.user;

import com.liemily.user.domain.UserStock;
import com.liemily.user.repository.UserStockRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class UserStockService {
    private UserStockRepository userStockRepository;

    public List<UserStock> findByUsername(String username) {
        return userStockRepository.findByUsername(username);
    }
}
