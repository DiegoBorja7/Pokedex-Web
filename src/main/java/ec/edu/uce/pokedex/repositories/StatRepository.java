package ec.edu.uce.pokedex.repositories;

import ec.edu.uce.pokedex.entities.StatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<StatEntity, Long> {
    StatEntity findByName(String name);
}