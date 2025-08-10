package app.repository;

import app.entities.AreaEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AreaRepository extends CrudRepository<AreaEntity, Integer> {
    Optional<AreaEntity> findByName(String name);
}
