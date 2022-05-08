package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.util.List;

@Service
@ThreadSafe
public class PostService {

    private final PostStore store;

    public PostService(PostStore store) {
        this.store = store;
    }

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
