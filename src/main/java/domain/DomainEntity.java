/* DomainEntity.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 * Fase 2: antes era una @Entity con herencia TABLE_PER_CLASS, así que todas
 * las entidades compartían una única tabla generadora de ids (un cuello de
 * botella con bloqueos) y Hibernate podía tener que unir con UNION todas las
 * tablas. Ahora es una @MappedSuperclass: cada entidad tiene su propia tabla
 * y su propia secuencia de PostgreSQL.
 */

package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class DomainEntity {

	// Constructors -----------------------------------------------------------

	public DomainEntity() {
		super();
	}


	// Identification ---------------------------------------------------------

	private int	id;
	private int	version;


	// Una secuencia por entidad (por ejemplo, negocio_seq). Hibernate reserva
	// los ids de 50 en 50 (optimizador pooled-lo), así que no consulta la
	// secuencia en cada inserción y puede agrupar los INSERT en lotes.
	@Id
	@GeneratedValue(generator = "secuencia")
	@GenericGenerator(name = "secuencia", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
		@Parameter(name = "prefer_sequence_per_entity", value = "true"),
		@Parameter(name = "sequence_name_suffix", value = "_seq"),
		@Parameter(name = "increment_size", value = "50"),
		@Parameter(name = "optimizer", value = "pooled-lo")
	})
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}


	// Equality ---------------------------------------------------------------
	//
	// Dos entidades son iguales si son del mismo tipo (o una es subclase de la
	// otra, como Actor y Usuario) y tienen el mismo id. Una entidad nueva (id 0)
	// solo es igual a sí misma. Hibernate.getClass desenvuelve los proxies
	// perezosos, que son subclases generadas en tiempo de ejecución.
	//
	// hashCode es constante a propósito: el id cambia al persistir, y si el hash
	// dependiera de él una entidad se "perdería" dentro de un HashSet. Antes
	// devolvía el id, y equals aceptaba además un Integer, lo que rompía la
	// simetría (a.equals(5) pero no 5.equals(a)).

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object other) {
		Class<?> thisClass, otherClass;

		if (this == other)
			return true;
		if (!(other instanceof DomainEntity))
			return false;

		DomainEntity entity = (DomainEntity) other;
		if (this.getId() == 0 || entity.getId() == 0 || this.getId() != entity.getId())
			return false;

		thisClass = Hibernate.getClass(this);
		otherClass = Hibernate.getClass(entity);

		return thisClass.isAssignableFrom(otherClass) || otherClass.isAssignableFrom(thisClass);
	}

}
