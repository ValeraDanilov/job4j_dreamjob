package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class PostStore {

    private final AtomicInteger index = new AtomicInteger(4);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Junior", LocalDateTime.now()));
        posts.put(2, new Post(2, "Middle Java Job", "Middle", LocalDateTime.now()));
        posts.put(3, new Post(3, "Senior Java Job", "Senior", LocalDateTime.now()));
    }


    public Collection<Post> findAll() {
        return posts.values();
    }

    public void create(Post post) {
        post.setId(index.getAndIncrement());
        post.setCreate(LocalDateTime.now());
        this.posts.putIfAbsent(post.getId(), post);
    }

    public Post findById(int id) {
        return this.posts.get(id);
    }

    public void update(Post post) {
        this.posts.replace(post.getId(), post);
    }
}
