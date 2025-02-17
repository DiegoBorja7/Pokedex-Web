package ec.edu.uce.pokedex;

import ec.edu.uce.pokedex.service.PokemonDatabaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PokedexWebApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PokedexWebApplication.class, args);
        PokemonDatabaseService pokemonDatabaseService = context.getBean(PokemonDatabaseService.class);

        // Ejecutar el metodo para poblar la base de datos
        pokemonDatabaseService.populateDatabase();
    }

}
