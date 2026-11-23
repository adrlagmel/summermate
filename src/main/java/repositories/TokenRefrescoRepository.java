package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.TokenRefresco;

@Repository
public interface TokenRefrescoRepository extends JpaRepository<TokenRefresco, Integer> {

	@Query("select t from TokenRefresco t join fetch t.userAccount where t.hash = ?1")
	TokenRefresco findByHash(String hash);

	@Modifying
	@Query("update TokenRefresco t set t.revocado = true where t.familia = ?1")
	int revocarFamilia(String familia);

	@Modifying
	@Query("update TokenRefresco t set t.revocado = true where t.userAccount.id = ?1")
	int revocarDeCuenta(int userAccountId);

}
