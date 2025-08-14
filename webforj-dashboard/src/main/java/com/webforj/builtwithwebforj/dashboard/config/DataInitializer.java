package com.webforj.builtwithwebforj.dashboard.config;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.webforj.builtwithwebforj.dashboard.models.Cryptocurrency;
import com.webforj.builtwithwebforj.dashboard.models.NewsArticle;
import com.webforj.builtwithwebforj.dashboard.repository.CryptocurrencyRepository;
import com.webforj.builtwithwebforj.dashboard.repository.NewsRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Data initializer that loads initial data into the database on application startup.
 * 
 * @Component - Makes this a Spring-managed bean
 * @Transactional - Ensures all database operations happen in a single transaction
 * 
 * The constructor runs automatically when Spring creates this component,
 * loading initial data into the H2 database.
 */
@Component
@Transactional
public class DataInitializer {

    private final CryptocurrencyRepository cryptoRepository;
    private final NewsRepository newsRepository;

    // Constructor injection - Spring automatically provides the repositories
    public DataInitializer(CryptocurrencyRepository cryptoRepository, 
                          NewsRepository newsRepository) {
        this.cryptoRepository = cryptoRepository;
        this.newsRepository = newsRepository;
        
        // Initialize data when the component is created
        initializeCryptocurrencies();
        initializeNews();
    }

    private void initializeCryptocurrencies() {
        // Check if data already exists to avoid duplicates
        if (cryptoRepository.count() > 0) {
            return;
        }

        // Sample cryptocurrency data
        List<Cryptocurrency> cryptos = Arrays.asList(
            new Cryptocurrency("BTC", "Bitcoin", 107356.60, 2140000000000.0, 13260000000.0, 1),
            new Cryptocurrency("ETH", "Ethereum", 2638.47, 318750000000.0, 9350000000.0, 2),
            new Cryptocurrency("USDT", "Tether", 1.00, 153010000000.0, 31500000000.0, 3),
            new Cryptocurrency("XRP", "XRP", 2.26, 132940000000.0, 926830000.0, 4),
            new Cryptocurrency("BNB", "BNB", 684.31, 98550000000.0, 461830000.0, 5),
            new Cryptocurrency("SOL", "Solana", 170.49, 88770000000.0, 2220000000.0, 6),
            new Cryptocurrency("USDC", "USDC", 1.00, 61370000000.0, 4980000000.0, 7),
            new Cryptocurrency("DOGE", "Dogecoin", 0.22, 32900000000.0, 859790000.0, 8),
            new Cryptocurrency("ADA", "Cardano", 0.74, 26380000000.0, 304620000.0, 9),
            new Cryptocurrency("AVAX", "Avalanche", 23.04, 9710000000.0, 194480000.0, 10)
        );

        // Save all cryptocurrencies to database
        cryptoRepository.saveAll(cryptos);
    }

    private void initializeNews() {
        // Check if data already exists
        if (newsRepository.count() > 0) {
            return;
        }

        // Sample news articles
        List<NewsArticle> articles = Arrays.asList(
            new NewsArticle(
                "Bitcoin Surges Past $107,000 as Institutional Interest Grows",
                "Major financial institutions continue to show interest in Bitcoin as the cryptocurrency reaches new highs for the year.",
                "CoinDesk",
                "2 hours ago",
                "https://example.com/bitcoin-surge",
                "https://images.unsplash.com/photo-1639762681485-074b7f938ba0?w=400&h=250&fit=crop"
            ),
            new NewsArticle(
                "Ethereum 2.0 Staking Reaches New Milestone",
                "The Ethereum network sees record participation in staking as the ecosystem continues to evolve.",
                "CryptoNews",
                "4 hours ago",
                "https://example.com/ethereum-staking"
            ),
            new NewsArticle(
                "Federal Reserve Discusses Digital Dollar Progress",
                "Central bank digital currency discussions intensify as the Fed releases new research on potential implementation.",
                "Reuters",
                "6 hours ago",
                "https://example.com/fed-digital-dollar"
            ),
            new NewsArticle(
                "DeFi Protocol Launches Revolutionary Lending Platform",
                "New decentralized finance platform promises to transform how users interact with lending and borrowing services.",
                "DeFi Times",
                "8 hours ago",
                "https://example.com/defi-launch"
            ),
            new NewsArticle(
                "Crypto Market Analysis: Bulls Take Control",
                "Technical indicators suggest continued positive momentum across major cryptocurrency markets.",
                "TradingView",
                "1 day ago",
                "https://example.com/market-analysis"
            )
        );

        // Save all articles to database
        newsRepository.saveAll(articles);
    }
}