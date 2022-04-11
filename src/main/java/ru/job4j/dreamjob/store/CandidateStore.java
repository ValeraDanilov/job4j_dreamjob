package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidate = new ConcurrentHashMap<>();

    private CandidateStore() {
        candidate.put(1, new Candidate(1, "Junior Java", "Junior", "09.04.22"));
        candidate.put(2, new Candidate(2, "Middle Java", "Middle", "08.04.22"));
        candidate.put(3, new Candidate(3, "Senior Java", "Senior", "07.04.22"));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidate.values();
    }
}
