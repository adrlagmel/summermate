-- V6__cuenta_premium.sql
--
-- Cuenta premium del empresario (Fase 6) y sus pagos.

alter table Empresario add column premiumHasta date;

create sequence PagoPremium_SEQ start with 1 increment by 50;

create table PagoPremium (
    id integer not null,
    version integer not null,
    referencia varchar(255) not null unique,
    plan varchar(255) not null check (plan in ('MENSUAL', 'ANUAL')),
    importe numeric(7,2) not null check (importe >= 0),
    estado varchar(255) not null check (estado in ('PENDIENTE', 'PAGADO', 'CANCELADO')),
    fecha timestamp(6) not null,
    empresario_id integer not null references Empresario (id) on delete cascade,
    primary key (id)
);

create index idx_pagopremium_empresario on PagoPremium (empresario_id);
