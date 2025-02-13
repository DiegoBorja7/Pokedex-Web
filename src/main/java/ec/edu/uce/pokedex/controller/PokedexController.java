package ec.edu.uce.pokedex.controller;

import ec.edu.uce.pokedex.model.Pokemon;
import ec.edu.uce.pokedex.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/pokedex")
@Controller
public class PokedexController {
    @Autowired
    private PokemonService pokemonService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "offset", defaultValue = "0") int offset,
                        @RequestParam(name = "limit", defaultValue = "30") int limit) {
        List<Pokemon> pokemons = pokemonService.getPokemons(offset, limit);
        model.addAttribute("pokemons", pokemons);
        return "index";
    }

    @GetMapping("/pokemon/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        Pokemon pokemon = pokemonService.getPokemonDetail(id);
        model.addAttribute("pokemon", pokemon);
        return "details";
    }
}