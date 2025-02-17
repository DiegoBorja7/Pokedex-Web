package ec.edu.uce.pokedex.service;

import ec.edu.uce.pokedex.entities.*;
import ec.edu.uce.pokedex.model.*;
import ec.edu.uce.pokedex.repositories.PokemonRepository;
import ec.edu.uce.pokedex.repositories.StatRepository;
import ec.edu.uce.pokedex.repositories.TypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonService {
    private final String POKEAPI_URL = "https://pokeapi.co/api/v2/pokemon/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final PokemonRepository pokemonRepository;
    private final TypeRepository typeRepository;
    private final StatRepository statRepository;

    public PokemonService(PokemonRepository pokemonRepository, TypeRepository typeRepository, StatRepository statRepository) {
        this.pokemonRepository = pokemonRepository;
        this.typeRepository = typeRepository;
        this.statRepository = statRepository;
    }

    // Metodo para obtener los Pokémon de la API
    public List<Pokemon> getPokemonsFromAPI(int offset, int limit) {
        String url = POKEAPI_URL + "?offset=" + offset + "&limit=" + limit;
        try {
            PokemonList response = restTemplate.getForObject(url, PokemonList.class);
            List<Pokemon> pokemons = new ArrayList<>();

            if (response != null && response.getResults() != null) {
                for (PokemonListItem item : response.getResults()) {
                    Pokemon pokemon = getPokemonDetailFromUrl(item.getUrl());
                    if (pokemon != null) {
                        pokemon.setPhoto(getPokemonPhotoUrl(pokemon.getId()));
                        pokemons.add(pokemon);
                    }
                }
            }
            return pokemons;
        } catch (RestClientException e) {
            System.err.println("Error al obtener datos de la API: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Metodo para obtener el detalle de un Pokémon desde la API
    public Pokemon getPokemonDetail(int id) {
        String url = POKEAPI_URL + id;
        try {
            Pokemon pokemon = restTemplate.getForObject(url, Pokemon.class);
            if (pokemon != null) {
                pokemon.setPhoto(getPokemonPhotoUrl(pokemon.getId()));
            }
            return pokemon;
        } catch (RestClientException e) {
            System.err.println("Error al obtener detalles del Pokémon: " + e.getMessage());
            return null;
        }
    }

    private Pokemon getPokemonDetailFromUrl(String url) {
        try {
            return restTemplate.getForObject(url, Pokemon.class);
        } catch (RestClientException e) {
            System.err.println("Error al obtener detalles desde URL: " + e.getMessage());
            return null;
        }
    }

    private String getPokemonPhotoUrl(int id) {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + id + ".png";
    }

    public int getTotalRecords() {
        String url = POKEAPI_URL + "?limit=1";
        try {
            PokemonList response = restTemplate.getForObject(url, PokemonList.class);
            return (response != null) ? response.getCount() : 0;
        } catch (RestClientException e) {
            System.err.println("Error al obtener el total de registros: " + e.getMessage());
            return 0;
        }
    }

    // Metodo para obtener los Pokémons de la base de datos
    @Transactional
    public List<Pokemon> getPokemons(int offset, int limit) {
        List<PokemonEntity> pokemonEntities = pokemonRepository.findAll(PageRequest.of(offset / limit, limit,
                Sort.by(Sort.Order.asc("id")))).getContent();

        List<Pokemon> pokemons = new ArrayList<>();
        for (PokemonEntity entity : pokemonEntities) {
            pokemons.add(convertEntityToModel(entity));
        }

        return pokemons;
    }

    // Metodo para convertir la entidad de Pokémon a modelo (para devolverlo a la API)
    private Pokemon convertEntityToModel(PokemonEntity entity) {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(entity.getId());
        pokemon.setName(entity.getName());
        pokemon.setPhoto(entity.getPhoto());

        // Convertir tipos
        List<PokemonType> types = new ArrayList<>();
        for (PokemonTypeEntity typeEntity : entity.getTypes()) {
            Type type = new Type();
            type.setName(typeEntity.getType().getName());

            PokemonType pokemonType = new PokemonType();
            pokemonType.setType(type);
            pokemonType.setSlot(typeEntity.getSlot());
            types.add(pokemonType);
        }
        pokemon.setTypes(types);

        // Convertir estadísticas
        List<PokemonStat> stats = new ArrayList<>();
        for (PokemonStatEntity statEntity : entity.getStats()) {
            Stat stat = new Stat();
            stat.setName(statEntity.getStat().getName());

            PokemonStat pokemonStat = new PokemonStat();
            pokemonStat.setStat(stat);
            pokemonStat.setBaseStat(statEntity.getBaseStat());
            stats.add(pokemonStat);
        }
        pokemon.setStats(stats);

        return pokemon;
    }

    private PokemonEntity convertToEntity(Pokemon pokemon) {
        PokemonEntity entity = new PokemonEntity();
        entity.setId(pokemon.getId());
        entity.setName(pokemon.getName());
        entity.setPhoto(pokemon.getPhoto());

        // Manejo de tipos
        List<PokemonTypeEntity> typeEntities = new ArrayList<>();
        if (pokemon.getTypes() != null) {
            for (PokemonType type : pokemon.getTypes()) {
                TypeEntity existingType = typeRepository.findByName(type.getType().getName());
                if (existingType == null) {
                    existingType = new TypeEntity();
                    existingType.setName(type.getType().getName());
                    typeRepository.save(existingType);
                }
                PokemonTypeEntity typeEntity = new PokemonTypeEntity();
                typeEntity.setType(existingType);
                typeEntity.setPokemon(entity);
                typeEntities.add(typeEntity);
            }
        }
        entity.setTypes(typeEntities);

        // Manejo de estadísticas
        List<PokemonStatEntity> statEntities = new ArrayList<>();
        if (pokemon.getStats() != null) {
            for (PokemonStat stat : pokemon.getStats()) {
                StatEntity existingStat = statRepository.findByName(stat.getStat().getName());
                if (existingStat == null) {
                    existingStat = new StatEntity();
                    existingStat.setName(stat.getStat().getName());
                    statRepository.save(existingStat);
                }
                PokemonStatEntity statEntity = new PokemonStatEntity();
                statEntity.setStat(existingStat);
                statEntity.setPokemon(entity);
                statEntity.setBaseStat(stat.getBaseStat());
                statEntities.add(statEntity);
            }
        }
        entity.setStats(statEntities);

        return entity;
    }

    public int getTotalRecordsInDatabase() {
        return (int) pokemonRepository.count(); // Cuenta los registros en la base de datos
    }

    /**
     * Metodo para obtener los Pokémon y guardarlos en la base de datos
     */
    @Transactional
    public void fetchAndSaveAllPokemons() {
        int total = getTotalRecords(); // Obtiene el total de Pokémon
        int batchSize = 500; // Definir tamaño del lote
        int offset = 0;

        System.out.println("Total de Pokémon en la API: " + total);  // Verificar el total

        while (offset < total) {
            List<Pokemon> pokemons = getPokemonsFromAPI(offset, batchSize);
            List<PokemonEntity> entities = new ArrayList<>();

            // 🔹 Convertir a `PokemonEntity`
            for (Pokemon pokemon : pokemons) {
                entities.add(convertToEntity(pokemon));
            }

            //Guardar en la base de datos
            pokemonRepository.saveAll(entities);
            offset += batchSize;
        }
    }

    @Transactional
    public List<Pokemon> searchPokemonByName(String name, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit); // Calculamos la página basada en el offset y limit
        Page<PokemonEntity> pageResult = pokemonRepository.findByNameContainingIgnoreCase(name, pageable);

        return pageResult.stream()
                .map(this::convertEntityToModel)
                .collect(Collectors.toList());
    }

    public int getLastPokemonIdFromDatabase() {
        PokemonEntity lastPokemon = pokemonRepository.findTopByOrderByIdDesc();
        return lastPokemon != null ? lastPokemon.getId() : 1; // Devuelve 1 si no hay registros
    }
}
