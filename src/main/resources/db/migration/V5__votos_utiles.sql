-- V5__votos_utiles.sql
--
-- Votos de "comentario útil" en las valoraciones de negocio (memoria del TFG).

create sequence VotoUtil_SEQ start with 1 increment by 50;

create table VotoUtil (
    id integer not null,
    version integer not null,
    cliente_id integer not null references Cliente (id) on delete cascade,
    valoracionNegocio_id integer not null references ValoracionNegocio (id) on delete cascade,
    primary key (id),
    constraint uk_votoutil_cliente_valoracion unique (cliente_id, valoracionNegocio_id)
);

create index idx_votoutil_valoracionnegocio on VotoUtil (valoracionNegocio_id);

-- El contador empieza en cero y lo mantiene la aplicación.
update ValoracionNegocio set comentarioUtil = 0 where comentarioUtil is null;
alter table ValoracionNegocio alter column comentarioUtil set default 0;
