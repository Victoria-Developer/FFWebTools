package app.repository;

import app.entities.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<AreaEntity, Integer> {
    Optional<AreaEntity> findByName(String name);
}
