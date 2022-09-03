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
import java.util.concurrent.atomic.AtomicInteger;

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
        model.addAttribute("cities", this.cityService.getAllCities());
        sessions(model, session);
        return "posts";
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
        model.addAttribute("post", this.postService.findById(id));
        model.addAttribute("cities", this.cityService.getAllCities());
        return "updatePost";
    }

    @GetMapping("/formPostId/{postId}")
    public String formDeletePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("postsId", this.postService.findById(id));
        return "post";
    }

    @GetMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable("postId") int id) {
        this.postService.delete(this.postService.findById(id));
        return VALUE;
    }

    private void sessions(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Guest");
        }
        model.addAttribute("user", user);
    }
}
