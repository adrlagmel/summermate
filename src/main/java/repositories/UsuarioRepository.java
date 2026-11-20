package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Negocio;
import domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	@Query("select a from Usuario a where a.userAccount.id=?1")
	Usuario findByUserAccountId(int id);
	
	@Query("select distinct a from Usuario a")
	Collection<Usuario> findAllUsuarios();
	
	@Query("select distinct u from Usuario u where lower(u.apellidos) like lower(concat('%', ?1, '%'))")
	Collection<Usuario> searchUsuarioForApellidos(String apellidos);

	// Fase 3: versiones paginadas (Pageable) para los listados ------------

	Page<Usuario> findAll(Pageable pageable);

}
