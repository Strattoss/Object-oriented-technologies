package pl.edu.agh.to.school;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GreetingController {
    @GetMapping
    public List<String> greeting() {
        return List.of("Technologie", "Obiektowe", "Witaj!");
    }
}
