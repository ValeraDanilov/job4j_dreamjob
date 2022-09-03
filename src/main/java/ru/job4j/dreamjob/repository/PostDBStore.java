package ru.job4j.dreamjob.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import org.slf4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class PostDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    int id = it.getInt("id");
                    String name = it.getString("name");
                    String description = it.getString("description");
                    LocalDateTime created = it.getTimestamp("created").toLocalDateTime();
                    boolean visible = it.getBoolean("visible");
                    posts.add(new Post(id, name, description, new City(it.getInt("city_id"), null), created, visible));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        posts.sort(Comparator.comparing(Post::getId));
        return posts;
    }

    public Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name, description, city_id, created, visible) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getCity().getId());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.setBoolean(5, post.isVisible());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("update post set name = ?, description = ?, city_id = ?, visible = ? where id = ?",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getCity().getId());
            ps.setBoolean(4, post.isVisible());
            ps.setInt(5, post.getId());
            ps.executeUpdate();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public void delete(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement pr = cn.prepareStatement("delete from post where id = ?")) {
            pr.setInt(1, post.getId());
            pr.executeUpdate();
        } catch (SQLException se) {
            LOG.error(se.getMessage(), se);
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    int idPost = it.getInt("id");
                    String name = it.getString("name");
                    String description = it.getString("description");
                    LocalDateTime created = it.getTimestamp("created").toLocalDateTime();
                    boolean visible = it.getBoolean("visible");
                    return new Post(idPost, name, description, new City(it.getInt("city_id"), null), created, visible);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}
