package ru.job4j.dreamjob.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.CandidateService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@ThreadSafe
public class CandidateController {

    private final CandidateService store;
    private static final String VALUE = "candidate";
    private static final String PATS = "redirect:/candidates";

    public CandidateController(CandidateService store) {
        this.store = store;
    }

    @GetMapping("/candidates")
    public String candidates(Model model, HttpSession session) {
        model.addAttribute("candidates", this.store.findAll());
        sessions(model, session);
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model, HttpSession session) {
        model.addAttribute(VALUE, new Candidate(0, "Заполните поле", null, false, "Заполните поле", null));
        sessions(model, session);
        return "addCandidate";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute Candidate updateCandidate) {
        Candidate oldCandidate = store.findById(updateCandidate.getId());
        updateCandidate.setPhoto(oldCandidate.getPhoto());
        this.store.update(updateCandidate);
        return PATS;
    }

    @GetMapping("/photoCandidate/{candidateId}")
    public ResponseEntity<Resource> download(@PathVariable("candidateId") Integer candidateId) {
        Candidate candidate = store.findById(candidateId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(candidate.getPhoto()));
    }

    @PostMapping("/createCandidate")
    public String createCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        candidate.setPhoto(file.getBytes());
        candidate.setCreated(LocalDateTime.now());
        store.create(candidate);
        return PATS;
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdateCandidate(Model model, @PathVariable("candidateId") int id) {
        model.addAttribute(VALUE, this.store.findById(id));
        return "updateCandidate";
    }

    @GetMapping("/formDeletePhoto/{candidateId}")
    public String formDeletePhoto(Model model, @PathVariable("candidateId") int id) {
        model.addAttribute(VALUE, this.store.findById(id));
        return "updateCandidatePhoto";
    }

    @PostMapping("/updateCandidatePhoto")
    public String updateCandidatePhoto(@ModelAttribute Candidate updateCandidate,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("delete") boolean del) throws IOException {
        String id = String.valueOf(updateCandidate.getId());
        Candidate oldCandidate = store.findById(updateCandidate.getId());
        updateCandidate.setName(oldCandidate.getName());
        updateCandidate.setDesc(oldCandidate.getDesc());
        if (del && file.isEmpty()) {
            updateCandidate.setPhoto(oldCandidate.getPhoto());
        } else {
            updateCandidate.setPhoto(file.getBytes());
        }
        this.store.update(updateCandidate);
        return String.format("redirect:/formUpdateCandidate/%s", id);
    }

    @GetMapping("/formDeleteCandidate/{candidateId}")
    public String formDeleteCandidate(Model model, @PathVariable("candidateId") int id) {
        model.addAttribute(VALUE, store.findById(id));
        return "deleteCandidate";
    }

    @PostMapping("/deleteCandidate")
    public String deleteCandidate(@ModelAttribute Candidate candidate, @RequestParam("delete") boolean delete) {
        if (delete) {
            this.store.delete(candidate);
        }
        return PATS;
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
