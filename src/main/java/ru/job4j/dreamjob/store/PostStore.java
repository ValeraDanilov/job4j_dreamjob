package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {

    private static final PostStore INST = new PostStore();
    private AtomicInteger index = new AtomicInteger(4);

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Junior", "09.04.22"));
        posts.put(2, new Post(2, "Middle Java Job", "Middle", "08.04.22"));
        posts.put(3, new Post(3, "Senior Java Job", "Senior", "07.04.22"));
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void create(Post post) {
        post.setId(index.getAndIncrement());
        this.posts.putIfAbsent(post.getId(), post);
    }

    public Post findById(int id) {
        return this.posts.get(id);
    }

    public void update(Post post) {
        Post updatePost = this.findById(post.getId());
        updatePost.setName(post.getName());
    }
}
