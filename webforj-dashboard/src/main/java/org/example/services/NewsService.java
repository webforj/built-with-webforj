package org.example.services;

import org.example.models.NewsArticle;
import java.util.ArrayList;
import java.util.List;

public class NewsService {
    
    public List<NewsArticle> getMockCryptoNews() {
        List<NewsArticle> articles = new ArrayList<>();
        
        articles.add(new NewsArticle(
            "Bitcoin Surges Past $50,000 as Institutional Interest Grows",
            "Major financial institutions continue to show interest in Bitcoin as the cryptocurrency reaches new highs for the year. Analysts point to growing adoption and regulatory clarity.",
            "CoinDesk",
            "2 hours ago",
            "https://example.com/bitcoin-surge"
        ));
        
        articles.add(new NewsArticle(
            "Ethereum 2.0 Staking Hits New Milestone",
            "The Ethereum network has reached a new all-time high in staked ETH, with over 28 million ETH now locked in staking contracts, representing significant confidence in the network's future.",
            "The Block",
            "4 hours ago",
            "https://example.com/ethereum-staking"
        ));
        
        articles.add(new NewsArticle(
            "SEC Approves First Spot Bitcoin ETF Applications",
            "In a landmark decision, the Securities and Exchange Commission has approved multiple spot Bitcoin ETF applications, marking a significant milestone for cryptocurrency adoption.",
            "Reuters",
            "6 hours ago",
            "https://example.com/sec-bitcoin-etf"
        ));
        
        articles.add(new NewsArticle(
            "Solana Network Sees Record Transaction Volume",
            "The Solana blockchain processed over 65 million transactions in a single day, showcasing the network's scalability and growing ecosystem of decentralized applications.",
            "CryptoSlate",
            "8 hours ago",
            "https://example.com/solana-record"
        ));
        
        articles.add(new NewsArticle(
            "Major Bank Launches Cryptocurrency Custody Service",
            "One of the world's largest banks announced the launch of its digital asset custody service, catering to institutional clients looking to securely store cryptocurrencies.",
            "Bloomberg",
            "12 hours ago",
            "https://example.com/bank-crypto-custody"
        ));
        
        articles.add(new NewsArticle(
            "DeFi Protocol TVL Reaches $100 Billion",
            "The total value locked in decentralized finance protocols has surpassed $100 billion, indicating renewed confidence in the DeFi sector after recent market volatility.",
            "DeFi Pulse",
            "1 day ago",
            "https://example.com/defi-tvl"
        ));
        
        articles.add(new NewsArticle(
            "Ripple Wins Key Legal Battle Against SEC",
            "In a significant development, Ripple Labs has won a crucial legal victory in its ongoing case with the SEC, with implications for the broader cryptocurrency industry.",
            "Forbes",
            "1 day ago",
            "https://example.com/ripple-sec"
        ));
        
        articles.add(new NewsArticle(
            "Central Bank Digital Currency Pilot Launches in Europe",
            "The European Central Bank has begun its pilot program for a digital euro, testing the infrastructure needed for a potential central bank digital currency.",
            "Financial Times",
            "2 days ago",
            "https://example.com/digital-euro"
        ));
        
        return articles;
    }
}