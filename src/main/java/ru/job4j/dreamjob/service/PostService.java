package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.util.List;

public class PostService {

    private final PostStore store = PostStore.instOf();

    public List<Post> findAll() {
        return this.store.findAll().stream().toList();
    }

    public void create(Post post) {
        this.store.create(post);
    }

    public Post findById(int id) {
        return this.store.findById(id);
    }

    public void update(Post post) {
        this.store.update(post);
    }
}
