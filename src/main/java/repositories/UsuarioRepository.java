package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	@Query("select a from Usuario a where a.userAccount.id=?1")
	Usuario findByUserAccountId(int id);
	
	@Query("select distinct a from Usuario a")
	Collection<Usuario> findAllUsuarios();
	
}