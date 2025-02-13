package ec.edu.uce.pokedex.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PokemonStat {
    @JsonProperty("base_stat")
    private int baseStat;
    private Stat stat;

    public int getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(int baseStat) {
        this.baseStat = baseStat;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }
}
