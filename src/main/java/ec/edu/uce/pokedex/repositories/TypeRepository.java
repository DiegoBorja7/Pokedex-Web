package ec.edu.uce.pokedex.repositories;

import ec.edu.uce.pokedex.entities.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
    TypeEntity findByName(String name);
}