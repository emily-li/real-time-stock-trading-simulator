package com.liemily.user.service;

import com.liemily.user.domain.UserStock;
import com.liemily.user.domain.UserStockId;
import com.liemily.user.repository.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public List<UserStock> getUserStocksOrderByCompanyName(String username) {
        return userStockRepository.findByUsernameOrderByStockViewCompanyName(username);
    }

    public List<UserStock> getUserStocksBySymbol(String username, String symbol, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndSymbolContainingIgnoreCase(username, symbol, stocksPageable);
    }

    public List<UserStock> getUserStocksByName(String username, String name, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndStockViewCompanyNameContainingIgnoreCase(username, name, stocksPageable);
    }

    public List<UserStock> getUserStocksByGainsLessThan(String username, BigDecimal gains, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndStockViewGainsLessThan(username, gains, stocksPageable);
    }

    public List<UserStock> getUserStocksByGainsGreaterThan(String username, BigDecimal gains, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndStockViewGainsGreaterThan(username, gains, stocksPageable);
    }

    public List<UserStock> getUserStocksByValueLessThan(String username, BigDecimal value, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndStockViewStockValueLessThan(username, value, stocksPageable);
    }

    public List<UserStock> getUserStocksByValueGreaterThan(String username, BigDecimal value, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndStockViewStockValueGreaterThan(username, value, stocksPageable);
    }

    public List<UserStock> getUserStocksByVolumeLessThan(String username, int volume, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndVolumeLessThan(username, volume, stocksPageable);
    }

    public List<UserStock> getUserStocksByVolumeGreaterThan(String username, int volume, Pageable stocksPageable) {
        return userStockRepository.findByUsernameAndVolumeGreaterThan(username, volume, stocksPageable);
    }

    public void save(UserStock userStock) {
        userStockRepository.save(userStock);
    }

    public void save(Collection<UserStock> userStocks) {
        userStockRepository.save(userStocks);
    }
}
