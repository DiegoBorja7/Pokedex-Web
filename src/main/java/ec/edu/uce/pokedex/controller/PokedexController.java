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
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "limit", defaultValue = "30") int limit) {
        int offset;
        int totalRecords = pokemonService.getTotalRecords();
        int totalPages = (int) Math.ceil(totalRecords / (double) limit); //Calculamos el total de páginas
        int windowSize = 10;
        int startPage;
        int endPage;

        // Validar el rango de página
        if (page < 1)
            page = 1;
        else if (page > totalPages)
            page = totalPages;

        // Calcular el offset para la consulta
        offset = (page - 1) * limit;
        List<Pokemon> pokemons = pokemonService.getPokemons(offset, limit);

        // Configurar la ventana de paginación
        startPage = page;
        endPage = page + windowSize - 1;
        if (endPage > totalPages) {
            endPage = totalPages;
            startPage = Math.max(1, endPage - windowSize + 1);
        }

        model.addAttribute("pokemons", pokemons);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "index";
    }

    @GetMapping("/pokemon/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        Pokemon pokemon = pokemonService.getPokemonDetail(id);
        model.addAttribute("pokemon", pokemon);
        return "details";
    }
}