package com.webforj.builtwithwebforj.dashboard.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webforj.builtwithwebforj.dashboard.models.Cryptocurrency;
import com.webforj.builtwithwebforj.dashboard.repository.CryptocurrencyRepository;

@Service
public class CryptocurrencyService {

  private final CryptocurrencyRepository repository;

  @Autowired
  public CryptocurrencyService(CryptocurrencyRepository repository) {
    this.repository = repository;
  }

  private Random random = new Random();

  public List<Cryptocurrency> generateCryptocurrencies() {
    // Fetch all cryptocurrencies from the database
    List<Cryptocurrency> cryptocurrencies = repository.findAll();
    
    // Convert to detached entities for real-time updates (copy constructor approach)
    List<Cryptocurrency> detachedCryptos = new ArrayList<>();
    for (Cryptocurrency crypto : cryptocurrencies) {
      Cryptocurrency detached = new Cryptocurrency(crypto.getSymbol(), crypto.getName(), 
                                                  crypto.getCurrentPrice(), crypto.getMarketCap(), 
                                                  crypto.getVolume24h(), crypto.getRank());
      // Copy other properties
      detached.setId(crypto.getId());
      detached.setHigh24h(crypto.getHigh24h());
      detached.setLow24h(crypto.getLow24h());
      detached.setCirculatingSupply(crypto.getCirculatingSupply());
      detached.setTotalSupply(crypto.getTotalSupply());
      detached.setPriceChangePercentage24h(crypto.getPriceChangePercentage24h());
      detached.setPriceChange24h(crypto.getPriceChange24h());
      
      // Create new ArrayList for price history to avoid Hibernate proxy issues
      detached.setPriceHistory(new ArrayList<>(crypto.getPriceHistory()));
      
      detachedCryptos.add(detached);
    }

    return detachedCryptos;
  }

  public void updatePrices(List<Cryptocurrency> cryptocurrencies) {
    for (Cryptocurrency crypto : cryptocurrencies) {
      // Simulate price movements (-2% to +2% per update)
      double changePercent = -0.02 + (random.nextDouble() * 0.04);
      double currentPrice = crypto.getCurrentPrice();
      double newPrice = currentPrice * (1 + changePercent);

      // Ensure price doesn't go negative
      newPrice = Math.max(newPrice, 0.0001);

      crypto.setCurrentPrice(newPrice);

      // Update market cap based on new price
      double newMarketCap = newPrice * crypto.getCirculatingSupply();
      crypto.setMarketCap(newMarketCap);

      // Simulate volume changes
      double volumeChange = 0.95 + (random.nextDouble() * 0.1);
      crypto.setVolume24h(crypto.getVolume24h() * volumeChange);

      // Update high/low if necessary
      if (newPrice > crypto.getHigh24h()) {
        crypto.setHigh24h(newPrice);
      }
      if (newPrice < crypto.getLow24h()) {
        crypto.setLow24h(newPrice);
      }
    }
  }
}