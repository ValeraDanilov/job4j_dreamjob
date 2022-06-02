package ru.job4j.dreamjob.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@ThreadSafe
public class PostController {

    private final PostService postService;
    private final CityService cityService;
    private static final String VALUE = "redirect:/posts";

    public PostController(PostService postService, CityService cityService) {
        this.postService = postService;
        this.cityService = cityService;
    }

    @GetMapping("/posts")
    public String posts(Model model, HttpSession session) {
        model.addAttribute("posts", this.postService.findAll());
        sessions(model, session);
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPost(Model model, HttpSession session) {
        model.addAttribute("post", new Post(0, "Заполните поле", "Заполните поле", null, null, false));
        model.addAttribute("cities", cityService.getAllCities());
        sessions(model, session);
        return "addPost";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post) {
        post.setCity(this.cityService.findById(post.getCity().getId()));
        post.setCreated(LocalDateTime.now());
        this.postService.create(post);
        return VALUE;
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute Post post) {
        post.setCity(this.cityService.findById(post.getCity().getId()));
        this.postService.update(post);
        return VALUE;
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("cities", cityService.getAllCities());
        return "updatePost";
    }

    @GetMapping("/formDeletePost/{postId}")
    public String formDeletePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("post", postService.findById(id));
        return "deletePost";
    }

    @PostMapping("/deletePost")
    public String deletePost(@ModelAttribute Post post, @RequestParam("delete") boolean delete) {
        if (delete) {
            this.postService.delete(post);
        }
        return VALUE;
    }

    private void sessions(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }
}
