package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private AtomicInteger index = new AtomicInteger(4);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Junior Java", "Junior", "09.04.22"));
        candidates.put(2, new Candidate(2, "Middle Java", "Middle", "08.04.22"));
        candidates.put(3, new Candidate(3, "Senior Java", "Senior", "07.04.22"));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return this.candidates.values();
    }

    public void create(Candidate candidate) {
        candidate.setId(this.index.getAndIncrement());
        this.candidates.putIfAbsent(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return this.candidates.get(id);
    }

    public void update(Candidate candidate) {
        this.candidates.replace(candidate.getId(), candidate);
    }
}
