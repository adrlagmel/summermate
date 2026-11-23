-- V9__tokens_api.sql
--
-- Fase 8: tokens de renovación de la API del móvil (solo su hash).

create sequence TokenRefresco_SEQ start with 1 increment by 50;

create table TokenRefresco (
    id integer not null,
    version integer not null,
    hash varchar(64) not null unique,
    familia varchar(36) not null,
    creado timestamp(6) with time zone not null,
    expira timestamp(6) with time zone not null,
    revocado boolean not null,
    userAccount_id integer not null references UserAccount (id) on delete cascade,
    primary key (id)
);

create index idx_tokenrefresco_familia on TokenRefresco (familia);
create index idx_tokenrefresco_useraccount on TokenRefresco (userAccount_id);
