package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Empresario;

@Repository
public interface EmpresarioRepository extends JpaRepository<Empresario, Integer> {
	
	@Query("select e from Empresario e where e.userAccount.id=?1")
	Empresario findByUserAccountId(int id);
}
