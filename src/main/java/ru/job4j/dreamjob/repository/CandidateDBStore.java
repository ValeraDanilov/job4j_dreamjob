package ru.job4j.dreamjob.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Repository
public class CandidateDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());

    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    int id = it.getInt("id");
                    String name = it.getString("name");
                    byte[] photo = it.getBytes("photo");
                    boolean visible = it.getBoolean("visible");
                    String description = it.getString("description");
                    LocalDateTime created = it.getTimestamp("created").toLocalDateTime();
                    candidates.add(new Candidate(id, name, photo, visible, description, created));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        candidates.sort(Comparator.comparing(Candidate::getId));
        return candidates;
    }

    public Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidate(name, photo, visible, description, created) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setBytes(2, candidate.getPhoto());
            ps.setBoolean(3, candidate.isVisible());
            ps.setString(4, candidate.getDescription());
            ps.setTimestamp(5, Timestamp.valueOf(candidate.getCreated()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("update candidate set name = ?, photo = ?, description = ?, visible = ? where id = ?",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setBytes(2, candidate.getPhoto());
            ps.setString(3, candidate.getDescription());
            ps.setBoolean(4, candidate.isVisible());
            ps.setInt(5, candidate.getId());
            ps.executeUpdate();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public void delete(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement pr = cn.prepareStatement("delete from candidate where id = ?")) {
            pr.setInt(1, candidate.getId());
            pr.executeUpdate();
        } catch (SQLException se) {
            LOG.error(se.getMessage(), se);
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    int idCandidate = it.getInt("id");
                    String name = it.getString("name");
                    byte[] photo = it.getBytes("photo");
                    boolean visible = it.getBoolean("visible");
                    String description = it.getString("description");
                    LocalDateTime created = it.getTimestamp("created").toLocalDateTime();
                    return new Candidate(idCandidate, name, photo, visible, description, created);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}
