package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private AtomicInteger index = new AtomicInteger(4);
    private LocalDateTime time = LocalDateTime.now();

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Junior Java", "Junior", LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute())));
        candidates.put(2, new Candidate(2, "Middle Java", "Middle", LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute())));
        candidates.put(3, new Candidate(3, "Senior Java", "Senior", LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute())));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return this.candidates.values();
    }

    public void create(Candidate candidate) {
        candidate.setId(this.index.getAndIncrement());
        candidate.setCreated(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute()));
        this.candidates.putIfAbsent(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return this.candidates.get(id);
    }

    public void update(Candidate candidate) {
        this.candidates.replace(candidate.getId(), candidate);
    }
}
