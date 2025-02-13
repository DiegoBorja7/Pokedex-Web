package ec.edu.uce.pokedex.model;

import java.util.List;

public class PokemonList {
    private int count;
    private String next;
    private String previous;
    private List<PokemonListItem> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PokemonListItem> getResults() {
        return results;
    }

    public void setResults(List<PokemonListItem> results) {
        this.results = results;
    }
}
