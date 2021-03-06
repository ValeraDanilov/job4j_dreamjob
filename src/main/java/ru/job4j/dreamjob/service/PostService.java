package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.jdbc.PostDBStore;
import ru.job4j.dreamjob.model.Post;

import java.util.List;

@Service
@ThreadSafe
public class PostService {

    private final PostDBStore store;
    private final CityService cityService;

    public PostService(PostDBStore store, CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    public List<Post> findAll() {
        List<Post> posts = store.findAll();
        posts.forEach(
                post -> post.setCity(
                        cityService.findById(post.getCity().getId())
                )
        );
        return posts;
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

    public void delete(Post post) {
        this.store.delete(post);
    }
}
