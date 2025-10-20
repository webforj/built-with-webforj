package com.webforjrest.frontend.data;

import com.webforj.data.repository.DelegatingRepository;
import com.webforjrest.frontend.models.Post;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * DelegatingRepository implementation for Posts from JSONPlaceholder API.
 *
 * This repository demonstrates the three required functions:
 * 1. Find function - fetches paginated data based on criteria
 * 2. Count function - returns total number of items (as int)
 * 3. Find by key function - fetches a single item by ID
 */
@Component
public class PostDelegatingRepository extends DelegatingRepository<Post, Object> {

    public PostDelegatingRepository(JSONPlaceholderService jsonPlaceholderService) {
        super(
            // 1. Find function: RepositoryCriteria -> Stream<Post>
            // Extracts pagination info and fetches data from the API
            criteria -> {
                int limit = criteria.getLimit();
                int offset = criteria.getOffset();

                List<Post> posts = jsonPlaceholderService.fetchPosts(limit, offset);
                return posts != null ? posts.stream() : Stream.empty();
            },

            // 2. Count function: RepositoryCriteria -> int
            // Returns total count for pagination calculations
            criteria -> jsonPlaceholderService.getPostCount(),

            // 3. Find by key function: Object -> Optional<Post>
            // Fetches a single post by ID (cast key to Long)
            key -> {
                if (key instanceof Long) {
                    return jsonPlaceholderService.fetchPostById((Long) key);
                }
                return Optional.empty();
            }
        );
    }
}
