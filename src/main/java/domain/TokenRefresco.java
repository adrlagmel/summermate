/* TokenRefresco.java
 *
 * Token de renovación de la API del móvil (Fase 8). Solo se guarda su hash
 * SHA-256: quien lea la base de datos no puede usarlo. Cada uso lo revoca y
 * emite otro de la misma familia; si llega uno ya revocado, alguien lo ha
 * copiado y se revoca la familia entera (cierra la sesión en ese móvil).
 */

package domain;

import java.time.Instant;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import security.UserAccount;

@Entity
@Table(indexes = { @Index(name = "idx_tokenrefresco_familia", columnList = "familia"),
	@Index(name = "idx_tokenrefresco_useraccount", columnList = "userAccount_id") })
@Access(AccessType.PROPERTY)
public class TokenRefresco extends DomainEntity {

	private String		hash;
	private String		familia;
	private Instant		creado;
	private Instant		expira;
	private boolean		revocado;
	private UserAccount	userAccount;


	@NotNull
	@Column(unique = true, length = 64)
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@NotNull
	@Column(length = 36)
	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	@NotNull
	public Instant getCreado() {
		return creado;
	}

	public void setCreado(Instant creado) {
		this.creado = creado;
	}

	@NotNull
	public Instant getExpira() {
		return expira;
	}

	public void setExpira(Instant expira) {
		this.expira = expira;
	}

	public boolean isRevocado() {
		return revocado;
	}

	public void setRevocado(boolean revocado) {
		this.revocado = revocado;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

}
