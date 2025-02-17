package ec.edu.uce.pokedex.controller;

import ec.edu.uce.pokedex.entities.PokemonEntity;
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
        int totalRecords = pokemonService.getTotalRecordsInDatabase();
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
        startPage = Math.max(1, page - windowSize / 2);  // Centrar la ventana de páginas alrededor de la página actual
        endPage = Math.min(startPage + windowSize - 1, totalPages);

        // Ajustar el rango si está al final de la paginación
        if (endPage - startPage < windowSize - 1) {
            startPage = Math.max(1, endPage - windowSize + 1);
        }

        model.addAttribute("pokemons", pokemons);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query") String query,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "limit", defaultValue = "30") int limit,
                         Model model) {
        int offset;
        int totalRecords = pokemonService.getTotalRecordsInDatabase();
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
        List<Pokemon> pokemons = pokemonService.searchPokemonByName(query, offset, limit);

        // Configurar la ventana de paginación
        startPage = Math.max(1, page - windowSize / 2);  // Centrar la ventana de páginas alrededor de la página actual
        endPage = Math.min(startPage + windowSize - 1, totalPages);

        // Ajustar el rango si está al final de la paginación
        if (endPage - startPage < windowSize - 1) {
            startPage = Math.max(1, endPage - windowSize + 1);
        }

        model.addAttribute("pokemons", pokemons);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "index :: pokemonList";
    }



    @GetMapping("/pokemon/{id}")
    public String details(@PathVariable("id") Integer  id, Model model) {
        int lastPokemonId = pokemonService.getLastPokemonIdFromDatabase();

        // Si el ID es null o menor que 1, redirigir al primer Pokémon
        if (id == null || id < 1) {
            id = 1; // Asegurarse de que el ID siempre sea un valor válido (al menos 1)
        }

        // Si el ID es 1 y se quiere ir al anterior, redirigir al último Pokémon
        if (id == 1) {
            model.addAttribute("previousId", lastPokemonId);
        } else {
            // Para cualquier otro Pokémon, obtener el ID anterior y siguiente
            model.addAttribute("previousId", id - 1);
        }

        // Si el ID es mayor al último ID en la base de datos, redirigir al primero
        if (id > lastPokemonId) {
            id = 1;
        }

        // Obtener detalles del Pokémon
        Pokemon pokemon = pokemonService.getPokemonDetail(id);
        model.addAttribute("pokemon", pokemon);
        model.addAttribute("totalPokemon", lastPokemonId); // Usar el ID del último Pokémon

        // Botón siguiente
        if (id < lastPokemonId) {
            model.addAttribute("nextId", id + 1);
        } else {
            // Si estás en el último, no hay siguiente, pero puedes ponerlo en el primer Pokémon si quieres circular
            model.addAttribute("nextId", 1);
        }

        return "details";
    }


}