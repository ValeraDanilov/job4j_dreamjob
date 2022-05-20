package ru.job4j.dreamjob.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@ThreadSafe
public class UserController {

    private final UserService userService;
    private static final String VALUE = "Заполните поле";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", this.userService.findAll());
        return "users";
    }

    @GetMapping("/registration")
    public String addUser(Model model) {
        model.addAttribute("user", new User(0, VALUE, VALUE, VALUE, null));
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        user.setCreated(LocalDateTime.now());
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует");
            return "fail";
        }
        return "success";
    }
}
