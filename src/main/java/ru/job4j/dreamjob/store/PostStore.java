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

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void create(Post post) {
        post.setId(index.getAndIncrement());
        post.setCreated(LocalDateTime.now());
        this.posts.putIfAbsent(post.getId(), post);
    }

    public Post findById(int id) {
        return this.posts.get(id);
    }

    public void update(Post post) {
        this.posts.replace(post.getId(), post);
    }
}
