package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Playa;

@Repository
public interface PlayaRepository extends JpaRepository<Playa, Integer> {

}