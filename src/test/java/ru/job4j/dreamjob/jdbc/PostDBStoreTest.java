package ru.job4j.dreamjob.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PostDBStoreTest {

    private PostDBStore store;

    @BeforeEach
    void setUp() {
        System.out.println("Before method");
        this.store = new PostDBStore(new Main().loadPool());
    }

    @AfterEach
    void tearDown() {
        System.out.println("After method");
        List<Post> posts = this.store.findAll();
        for (Post post : posts)
            this.store.delete(post);
    }

    @Test
    void whenCreatePost() {
        Post post = new Post(0, "Заполните поле", "Заполните поле", new City(0, "Киев"), LocalDateTime.now(), false);
        this.store.create(post);
        Post postInDb = this.store.findById(post.getId());
        System.out.println(this.store.findAll());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    void whenDeletePost() {
        Post post = new Post(0, "Заполните поле", "Заполните поле", new City(0, "Киев"), LocalDateTime.now(), false);
        this.store.create(post);
        this.store.delete(post);
        assertThat(this.store.findById(post.getId()), nullValue());
    }

    @Test
    void whenFindAllPost() {
        Post first = new Post(0, "Java", "Java", new City(0, "Киев"), LocalDateTime.now(), false);
        Post second = new Post(0, "C++", "C++", new City(1, "Днепр"), LocalDateTime.now(), false);
        Post third = new Post(0, "C", "C", new City(2, "Херсон"), LocalDateTime.now(), false);
        this.store.create(first);
        this.store.create(second);
        this.store.create(third);
        List<Post> posts = List.of(first, second, third);
        assertThat(store.findAll(), is(posts));
    }

    @Test
    void whenUpdatePost() {
        Post first = new Post(0, "Java", "Java", new City(0, "Киев"), LocalDateTime.now(), false);
        this.store.create(first);
        Post second = new Post(first.getId(), "C++", "C++", new City(1, "Днепр"), LocalDateTime.now(), false);
        this.store.update(second);
        assertThat(this.store.findById(first.getId()), is(second));
    }
}
