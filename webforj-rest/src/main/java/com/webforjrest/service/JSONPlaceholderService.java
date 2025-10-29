package com.webforjrest.service;

import com.webforjrest.model.Post;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

/**
 * Service for consuming JSONPlaceholder API.
 * Fetches post data from the public JSONPlaceholder API.
 */
@Service
public class JSONPlaceholderService {

    private final RestClient restClient;

    public JSONPlaceholderService() {
        this.restClient = RestClient.create("https://jsonplaceholder.typicode.com");
    }

    /**
     * Fetches posts with pagination support.
     * JSONPlaceholder supports _start and _limit query parameters.
     *
     * @param limit Number of posts to fetch
     * @param offset Starting index (0-based)
     * @return List of Post objects
     */
    public List<Post> fetchPosts(int limit, int offset) {
        List<Post> posts = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/posts")
                        .queryParam("_start", offset)
                        .queryParam("_limit", limit)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return posts;
    }

    /**
     * Returns the total count of posts available.
     * Fetches all posts and returns the count.
     *
     * @return Total number of posts
     */
    public int getPostCount() {
        List<Post> allPosts = restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        int count = allPosts != null ? allPosts.size() : 0;
        return count;
    }

    /**
     * Fetches a single post by ID.
     *
     * @param id Post ID
     * @return Optional containing the post if found
     */
    public Optional<Post> fetchPostById(Long id) {
        try {
            Post post = restClient.get()
                    .uri("/posts/" + id)
                    .retrieve()
                    .body(Post.class);
            return Optional.ofNullable(post);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
