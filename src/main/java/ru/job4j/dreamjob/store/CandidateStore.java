package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class CandidateStore {

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger index = new AtomicInteger(4);


    public Collection<Candidate> findAll() {
        return this.candidates.values();
    }

    public void create(Candidate candidate) {
        candidate.setId(this.index.getAndIncrement());
        candidate.setCreated(LocalDateTime.now());
        this.candidates.putIfAbsent(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return this.candidates.get(id);
    }

    public void update(Candidate candidate) {
        this.candidates.replace(candidate.getId(), candidate);
    }
}
