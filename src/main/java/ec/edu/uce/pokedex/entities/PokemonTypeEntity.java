package ec.edu.uce.pokedex.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_type")
public class PokemonTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int slot;

    @ManyToOne
    @JoinColumn(name = "pokemon_id", nullable = false)
    private PokemonEntity pokemon;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", nullable = false)
    private TypeEntity type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public PokemonEntity getPokemon() {
        return pokemon;
    }

    public void setPokemon(PokemonEntity pokemon) {
        this.pokemon = pokemon;
    }

    public TypeEntity getType() {
        return type;
    }

    public void setType(TypeEntity type) {
        this.type = type;
    }
}
