package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class CandidateDBStoreTest {

    private CandidateDBStore store;

    @BeforeEach
    void setUp() {
        System.out.println("Before method");
        this.store = new CandidateDBStore(new Main().loadPool());
    }

    @AfterEach
    void tearDown() {
        System.out.println("After method");
        List<Candidate> candidates = this.store.findAll();
        for (Candidate candidate : candidates)
            this.store.delete(candidate);
    }

    @Test
    void whenCreatePost() {
        Candidate candidate = new Candidate(0, "Заполните поле", null, false, "Заполните поле", LocalDateTime.now());
        this.store.create(candidate);
        Candidate candidateInDb = this.store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    void whenDeletePost() {
        Candidate candidate = new Candidate(0, "Заполните поле", null, false, "Заполните поле", LocalDateTime.now());
        this.store.create(candidate);
        this.store.delete(candidate);
        assertThat(this.store.findById(candidate.getId()), nullValue());
    }

    @Test
    void whenFindAllPost() {
        Candidate first = new Candidate(0, "C", null, false, "Заполните поле", LocalDateTime.now());
        Candidate second = new Candidate(0, "C++", null, false, "Заполните поле", LocalDateTime.now());
        Candidate third = new Candidate(0, "Java", null, false, "Заполните поле", LocalDateTime.now());
        this.store.create(first);
        this.store.create(second);
        this.store.create(third);
        List<Candidate> posts = List.of(first, second, third);
        assertThat(this.store.findAll(), is(posts));
    }

    @Test
    void whenUpdatePost() {
        Candidate first = new Candidate(0, "Java", null, false, "Заполните поле", LocalDateTime.now());
        this.store.create(first);
        Candidate second = new Candidate(first.getId(), "C++", null, false, "Заполните поле", LocalDateTime.now());
        this.store.update(second);
        assertThat(this.store.findById(first.getId()), is(second));
    }
}
