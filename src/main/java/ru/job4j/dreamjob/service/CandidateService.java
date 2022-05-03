package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateStore;

import java.util.List;

public class CandidateService {

    private final CandidateStore store = CandidateStore.instOf();

    public List<Candidate> findAll() {
        return this.store.findAll().stream().toList();
    }

    public void create(Candidate candidate) {
        this.store.create(candidate);
    }

    public Candidate findById(int id) {
        return this.store.findById(id);
    }

    public void update(Candidate candidate) {
        this.store.update(candidate);
    }
}
