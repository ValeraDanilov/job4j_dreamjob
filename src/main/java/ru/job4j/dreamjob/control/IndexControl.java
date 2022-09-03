package ru.job4j.dreamjob.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@ThreadSafe
public class IndexControl {
    private final PostService postService;
    private final CandidateService store;

    public IndexControl(PostService postService, CandidateService store) {
        this.postService = postService;
        this.store = store;
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("posts", postService.findAll()
                .stream()
                .filter(post -> date(post.getCreated()))
                .toList());
        model.addAttribute("candidates", store.findAll()
                .stream()
                .filter(candidate -> date(candidate.getCreated()))
                .toList());
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Guest");
        }
        model.addAttribute("user", user);
        return "index";
    }

    private boolean date(LocalDateTime date) {
        return date.getYear() == LocalDateTime.now().getYear()
                && date.getMonth() == LocalDateTime.now().getMonth()
                && date.getDayOfMonth() == LocalDateTime.now().getDayOfMonth();
    }
}
