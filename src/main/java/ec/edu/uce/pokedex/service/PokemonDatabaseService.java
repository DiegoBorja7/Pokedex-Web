package ec.edu.uce.pokedex.service;

import org.springframework.stereotype.Service;

@Service
public class PokemonDatabaseService {
    private final PokemonService pokemonService;

    public PokemonDatabaseService(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    public void populateDatabase() {
        if (pokemonService.getTotalRecordsInDatabase() == 0) {
            pokemonService.fetchAndSaveAllPokemons();
            System.out.println("Base de datos poblada con éxito!");
        } else {
            System.out.println("La base de datos ya tiene registros.\nOmitiendo población.");
        }
    }
}
