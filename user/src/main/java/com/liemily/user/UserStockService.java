package com.liemily.user;

import com.liemily.user.domain.UserStock;
import com.liemily.user.domain.UserStockId;
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

    public UserStock getUserStock(String username, String symbol) {
        return userStockRepository.findOne(new UserStockId(username, symbol));
    }

    public List<UserStock> getUserStocks(String username, Pageable pageable) {
        return userStockRepository.findByUsername(username, pageable);
    }

    public List<UserStock> getUserStocksBySymbol(String username, String symbol, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndSymbolContainingIgnoreCase(username, symbol, stocksPageable);
    }

    public void save(UserStock userStock) {
        userStockRepository.save(userStock);
    }

    public void save(Collection<UserStock> userStocks) {
        userStockRepository.save(userStocks);
    }
}
