package ec.edu.uce.pokedex.service;

import ec.edu.uce.pokedex.model.Pokemon;
import ec.edu.uce.pokedex.model.PokemonList;
import ec.edu.uce.pokedex.model.PokemonListItem;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonService {
    private final String POKEAPI_URL = "https://pokeapi.co/api/v2/pokemon/";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Pokemon> getPokemons(int offset, int limit) {
        String url = POKEAPI_URL + "?offset=" + offset + "&limit=" + limit;
        PokemonList response = restTemplate.getForObject(url, PokemonList.class);
        List<Pokemon> pokemons = new ArrayList<>();

        if (response != null && response.getResults() != null) {
            for (PokemonListItem item : response.getResults()) {
                Pokemon pokemon = getPokemonDetailFromUrl(item.getUrl());
                if (pokemon != null) {
                    pokemon.setPhoto("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.getId() + ".png");
                    pokemons.add(pokemon);
                }
            }
        }
        return pokemons;
    }

    public Pokemon getPokemonDetail(int id) {
        String url = POKEAPI_URL + id;
        Pokemon pokemon = restTemplate.getForObject(url, Pokemon.class);
        if (pokemon != null) {
            pokemon.setPhoto("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.getId() + ".png");
        }
        return pokemon;
    }

    private Pokemon getPokemonDetailFromUrl(String url) {
        return restTemplate.getForObject(url, Pokemon.class);
    }

    public int getTotalRecords() {
        String url = POKEAPI_URL + "?limit=1";
        PokemonList response = restTemplate.getForObject(url, PokemonList.class);
        return (response != null) ? response.getCount() : 0;
    }}
