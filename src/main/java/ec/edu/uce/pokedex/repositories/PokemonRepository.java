package ec.edu.uce.pokedex.repositories;

import ec.edu.uce.pokedex.entities.PokemonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<PokemonEntity, Integer> {
    Page<PokemonEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    PokemonEntity findTopByOrderByIdDesc();
}
