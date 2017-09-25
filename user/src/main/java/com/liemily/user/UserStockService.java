package com.liemily.user;

import com.liemily.user.domain.UserStock;
import com.liemily.user.repository.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Lazy
public class UserStockService {
    private UserStockRepository userStockRepository;

    @Autowired
    public UserStockService(UserStockRepository userStockRepository) {
        this.userStockRepository = userStockRepository;
    }

    public List<UserStock> findByUsername(String username, Pageable pageable) {
        return userStockRepository.findByUsername(username, pageable);
    }

    public void save(UserStock userStock) {
        userStockRepository.save(userStock);
    }

    public void save(Collection<UserStock> userStocks) {
        userStockRepository.save(userStocks);
    }

}
