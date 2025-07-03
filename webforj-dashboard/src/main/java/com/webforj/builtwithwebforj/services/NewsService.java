package com.webforj.builtwithwebforj.services;

import java.util.ArrayList;
import java.util.List;

import com.webforj.builtwithwebforj.models.NewsArticle;

public class NewsService {
    
    public List<NewsArticle> getMockCryptoNews() {
        List<NewsArticle> articles = new ArrayList<>();
        
        articles.add(new NewsArticle(
            "Bitcoin Surges Past $50,000 as Institutional Interest Grows",
            "Major financial institutions continue to show interest in Bitcoin as the cryptocurrency reaches new highs for the year. Analysts point to growing adoption and regulatory clarity.",
            "CoinDesk",
            "2 hours ago",
            "https://example.com/bitcoin-surge",
            "https://images.unsplash.com/photo-1639762681485-074b7f938ba0?w=400&h=250&fit=crop"
        ));
        
        articles.add(new NewsArticle(
            "Ethereum 2.0 Staking Hits New Milestone",
            "The Ethereum network has reached a new all-time high in staked ETH, with over 28 million ETH now locked in staking contracts, representing significant confidence in the network's future.",
            "The Block",
            "4 hours ago",
            "https://example.com/ethereum-staking",
            "https://images.unsplash.com/photo-1621761191319-c6fb62004040?w=400&h=250&fit=crop"
        ));
        
        articles.add(new NewsArticle(
            "SEC Approves First Spot Bitcoin ETF Applications",
            "In a landmark decision, the Securities and Exchange Commission has approved multiple spot Bitcoin ETF applications, marking a significant milestone for cryptocurrency adoption.",
            "Reuters",
            "6 hours ago",
            "https://example.com/sec-bitcoin-etf",
            "https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=400&h=250&fit=crop"
        ));
        
        articles.add(new NewsArticle(
            "Solana Network Sees Record Transaction Volume",
            "The Solana blockchain processed over 65 million transactions in a single day, showcasing the network's scalability and growing ecosystem of decentralized applications.",
            "CryptoSlate",
            "8 hours ago",
            "https://example.com/solana-record",
            "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?w=400&h=250&fit=crop"
        ));
        
        articles.add(new NewsArticle(
            "Major Bank Launches Cryptocurrency Custody Service",
            "One of the world's largest banks announced the launch of its digital asset custody service, catering to institutional clients looking to securely store cryptocurrencies.",
            "Bloomberg",
            "12 hours ago",
            "https://example.com/bank-crypto-custody",
            "https://images.unsplash.com/photo-1560472355-536de3962603?w=400&h=250&fit=crop"
        ));
        
        articles.add(new NewsArticle(
            "DeFi Protocol TVL Reaches $100 Billion",
            "The total value locked in decentralized finance protocols has surpassed $100 billion, indicating renewed confidence in the DeFi sector after recent market volatility.",
            "DeFi Pulse",
            "1 day ago",
            "https://example.com/defi-tvl",
            "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=400&h=250&fit=crop"
        ));
        
        articles.add(new NewsArticle(
            "Ripple Wins Key Legal Battle Against SEC",
            "In a significant development, Ripple Labs has won a crucial legal victory in its ongoing case with the SEC, with implications for the broader cryptocurrency industry.",
            "Forbes",
            "1 day ago",
            "https://example.com/ripple-sec",
            "https://images.unsplash.com/photo-1518546305927-5a555bb7020d?w=400&h=250&fit=crop"
        ));
        
        articles.add(new NewsArticle(
            "Central Bank Digital Currency Pilot Launches in Europe",
            "The European Central Bank has begun its pilot program for a digital euro, testing the infrastructure needed for a potential central bank digital currency.",
            "Financial Times",
            "2 days ago",
            "https://example.com/digital-euro",
            "https://images.unsplash.com/photo-1566228015668-4c45dbc4e2f5?w=400&h=250&fit=crop"
        ));
        
        return articles;
    }
}