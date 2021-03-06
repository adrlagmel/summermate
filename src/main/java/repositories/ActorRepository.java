package repositories;

import java.util.Collection;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
	
	@Query("select a from Actor a where a.userAccount.id=?1")
	Actor findByUserAccountId(int id);
	
	@Query("select distinct a from Actor a where a.apellidos like %?1%")
	Collection<Actor> searchUsuarioForApellidos(String apellidos);
	
	@Query("select a from Actor a where a.userAccount.id!=?1")
	Collection<Actor> findAllMessages(int actorId);
}