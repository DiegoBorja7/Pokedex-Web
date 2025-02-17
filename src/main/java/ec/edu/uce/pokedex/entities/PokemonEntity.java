package ec.edu.uce.pokedex.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pokemon")
public class PokemonEntity {
    @Id
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PokemonTypeEntity> types;

    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PokemonStatEntity> stats;

    @Column(nullable = false)
    private String photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PokemonTypeEntity> getTypes() {
        return types;
    }

    public void setTypes(List<PokemonTypeEntity> types) {
        this.types = types;
    }

    public List<PokemonStatEntity> getStats() {
        return stats;
    }

    public void setStats(List<PokemonStatEntity> stats) {
        this.stats = stats;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
