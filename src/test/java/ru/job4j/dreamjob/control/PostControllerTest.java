package ru.job4j.dreamjob.control;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostControllerTest {

    @Before
    public void setUp() {
        System.out.println("Before method");
    }

    @After
    public void tearDown() {
        System.out.println("After method");
    }

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post"),
                new Post(2, "New post")
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("posts"));
    }

    @Test
    public void whenAddPosts() {
        Post posts = new Post(0, "Заполните поле", "Заполните поле", null, null, false);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        when(postService.findById(0)).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.addPost(model, session);
        verify(model).addAttribute("post", posts);
        verify(model).addAttribute("cities", cityService.getAllCities());
        assertThat(page, is("addPost"));
    }


    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "New post", "", new City(1, "Киев"), LocalDateTime.now(), true);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input);
        verify(postService).create(input);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenUpdatePost() {
        Post input = new Post(1, "post", "", new City(1, "Киев"), LocalDateTime.now(), true);
        Post newInput = new Post(1, "New post", "new", new City(2, "Харьков"), LocalDateTime.now(), true);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        postController.createPost(input);
        String update = postController.updatePost(newInput);
        verify(postService).update(input);
        assertThat(update, is("redirect:/posts"));
    }

    @Test
    public void whenFormUpdatePost() {
        Post input = new Post(1, "post", "", new City(1, "Киев"), LocalDateTime.now(), true);
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        postController.createPost(input);
        String formUpdate = postController.formUpdatePost(model, input.getId());
        verify(model).addAttribute("post", postService.findById(input.getId()));
        verify(model).addAttribute("cities", cityService.getAllCities());
        assertThat(formUpdate, is("updatePost"));
    }


    @Test
    public void whenDeletePost() {
        Post input = new Post(1, "post", "", new City(1, "Киев"), LocalDateTime.now(), true);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        postController.createPost(input);
        String update = postController.deletePost(input, true);
        verify(postService).delete(input);
        assertThat(update, is("redirect:/posts"));
    }

    @Test
    public void whenFormDeletePost() {
        Post input = new Post(1, "post", "", new City(1, "Киев"), LocalDateTime.now(), true);
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        postController.createPost(input);
        String update = postController.formDeletePost(model, input.getId());
        assertThat(update, is("deletePost"));
    }

}
