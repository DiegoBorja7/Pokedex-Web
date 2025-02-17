package ec.edu.uce.pokedex.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_stat")
public class PokemonStatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_stat")
    private int baseStat;

    @ManyToOne
    @JoinColumn(name = "pokemon_id", nullable = false)
    private PokemonEntity pokemon;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stat_id", nullable = false)
    private StatEntity stat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(int baseStat) {
        this.baseStat = baseStat;
    }

    public PokemonEntity getPokemon() {
        return pokemon;
    }

    public void setPokemon(PokemonEntity pokemon) {
        this.pokemon = pokemon;
    }

    public StatEntity getStat() {
        return stat;
    }

    public void setStat(StatEntity stat) {
        this.stat = stat;
    }
}
