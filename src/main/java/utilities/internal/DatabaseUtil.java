/* DatabaseUtilities.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package utilities.internal;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.jpa.HibernatePersistenceProvider;

import utilities.DatabaseConfig;
import domain.DomainEntity;

public class DatabaseUtil {
	
	public DatabaseUtil() {
		openEntityManager(connectionProperties());
	}

	private void openEntityManager(Map<String, String> settings) {
		persistenceProvider = new HibernatePersistenceProvider();
		entityManagerFactory = persistenceProvider.createEntityManagerFactory(DatabaseConfig.PersistenceUnit, settings);
		entityManager = entityManagerFactory.createEntityManager();

		properties = entityManagerFactory.getProperties();
		databaseUrl = findProperty("javax.persistence.jdbc.url");
		databaseName = StringUtils.substringBefore(StringUtils.substringAfterLast(databaseUrl, "/"), "?");
		databaseDialectName = findProperty("hibernate.dialect");

		entityTransaction = entityManager.getTransaction();
	}
	
	private HibernatePersistenceProvider persistenceProvider;	
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private Map<String, Object> properties;
	private String databaseUrl;
	private String databaseName;
	private String databaseDialectName;
	private EntityTransaction entityTransaction;
		
	public HibernatePersistenceProvider getPersistenceProvider() {
		return persistenceProvider;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getDatabaseDialectName() {
		return databaseDialectName;
	}

	public EntityTransaction getEntityTransaction() {
		return entityTransaction;
	}

	public void recreateDatabase() throws Throwable {
		List<String> schemaScript;
		Map<String, String> settings;

		// En PostgreSQL no se puede borrar la base de datos a la que se está
		// conectado, así que se vacía el esquema "public"...
		schemaScript = new ArrayList<String>();
		schemaScript.add("drop schema if exists public cascade");
		schemaScript.add("create schema public");
		executeScript(schemaScript);

		// ... y se vuelve a abrir la unidad de persistencia pidiendo a JPA que
		// cree las tablas (generación de esquema estándar de JPA 2.1).
		close();
		settings = connectionProperties();
		settings.put("javax.persistence.schema-generation.database.action", "create");
		openEntityManager(settings);
	}

	// Lee la conexión de propiedades del sistema o de variables de entorno,
	// con los mismos nombres y valores por defecto que spring/config/data.xml.
	private static Map<String, String> connectionProperties() {
		Map<String, String> result;

		result = new HashMap<String, String>();
		result.put("javax.persistence.jdbc.url", setting("DB_URL", "jdbc:postgresql://localhost:5432/summermate"));
		result.put("javax.persistence.jdbc.user", setting("DB_USER", "summermate"));
		result.put("javax.persistence.jdbc.password", setting("DB_PASSWORD", "summermate"));

		return result;
	}

	private static String setting(String name, String defaultValue) {
		String result;

		result = System.getProperty(name);
		if (StringUtils.isBlank(result))
			result = System.getenv(name);
		if (StringUtils.isBlank(result))
			result = defaultValue;

		return result;
	}

	private void executeScript(final List<String> script) {
		Session session;
		session = entityManager.unwrap(Session.class);		
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				Statement statement;
				
				statement = connection.createStatement();
				for (String line : script) {
					statement.execute(line);					
				}
				connection.commit();
			}
		});
	}

	public void openTransaction() {
		entityTransaction.begin();
	}

	public void commitTransaction() {
		entityTransaction.commit();
	}
	
	public void rollbackTransaction() {
		entityTransaction.rollback();
	}

	public void persist(DomainEntity entity) {
		entityManager.persist(entity);		
		entityManager.flush();
	}

	public void close() {
		if (entityTransaction.isActive())
			entityTransaction.rollback();
		if (entityManager.isOpen())
			entityManager.close();
		if (entityManagerFactory.isOpen())
			entityManagerFactory.close();
	}
	

	
	private String findProperty(String property) {
		String result;		
		Object value;

		value = properties.get(property);
		if (value == null)
			throw new RuntimeException(String.format("Property `%s' not found", property));
		if (!(value instanceof String))
			throw new RuntimeException(String.format("Property `%s' is not a string", property));
		result = (String) value;
		if (StringUtils.isBlank(result))
			throw new RuntimeException(String.format("Property `%s' is blank", property));

		return result;
	}

	@SuppressWarnings("unused")
	private void printProperties(Map<String, Object> properties) {
		for (Entry<String, Object> entry : properties.entrySet())
			System.out.println(String.format("%s=`%s'", entry.getKey(), entry.getValue()));
	}

	public int executeUpdate(String line) {
		int result;
		Query query;

		query = entityManager.createQuery(line);
		result = query.executeUpdate();
		
		return result;
	}
	
	public List<?> executeSelect(String line) {
		List<?> result;
		Query query;
		
		query = entityManager.createQuery(line);		
		result = (List<?>)query.getResultList();
		
		return result;
	}

}
