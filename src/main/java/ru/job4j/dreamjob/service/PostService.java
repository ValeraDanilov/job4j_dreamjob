package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.repository.PostDBStore;
import ru.job4j.dreamjob.model.Post;

import java.util.List;
import java.util.NoSuchElementException;

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
        if (this.store.findById(post.getId()) == null) {
            throw new NoSuchElementException("Invalid item id ");
        }
        this.store.update(post);
    }

    public void delete(Post post) {
        if (this.store.findById(post.getId()) == null) {
            throw new NoSuchElementException("Invalid item id ");
        }
        this.store.delete(post);
    }
}
