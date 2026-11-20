-- V2__busqueda_trigram.sql
--
-- Las búsquedas por ciudad (negocios) y por apellidos (usuarios) usan
-- lower(columna) LIKE lower('%texto%'). Un índice B-tree normal no sirve con un
-- comodín al principio; un índice GIN de trigramas (extensión pg_trgm) sí.
-- Pendiente desde la Fase 3.
--
-- pg_trgm es una extensión "de confianza" desde PostgreSQL 13: la puede crear
-- el dueño de la base de datos sin ser superusuario.

create extension if not exists pg_trgm;

create index idx_playa_ciudad_trgm on Playa using gin (lower(ciudad) gin_trgm_ops);
create index idx_actor_apellidos_trgm on Actor using gin (lower(apellidos) gin_trgm_ops);
