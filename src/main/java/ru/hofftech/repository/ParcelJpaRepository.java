package ru.hofftech.repository;

import ru.hofftech.entity.ParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParcelJpaRepository extends JpaRepository<ParcelEntity, Long> {
    Optional<ParcelEntity> findByName(String name);
    boolean existsByName(String name);
    void deleteByName(String name);
}