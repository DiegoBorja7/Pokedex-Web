package ec.edu.uce.pokedex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PokedexController {
    @GetMapping("/pokedex")
    public String home(Model model) {
        model.addAttribute("title", "Pokedex DB");
        return "index";
    }
}