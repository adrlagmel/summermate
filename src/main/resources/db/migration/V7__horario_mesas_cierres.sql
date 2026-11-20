-- V7__horario_mesas_cierres.sql
--
-- Fase 6: calendario del negocio. Sustituye a CalendarioNegocio, que solo
-- copiaba cada reserva con su fecha y hora y que ninguna pantalla usaba.
-- La tabla CalendarioNegocio se queda sin uso (los datos de ejemplo V1000
-- todavía insertan en ella); la ocupación se calcula de las reservas.

-- Horario y duración de cada reserva (antes fijos: de 12:00 a 24:00, 1 hora)
alter table Negocio add column horaApertura time not null default '12:00';
alter table Negocio add column horaCierre time not null default '00:00';
alter table Negocio add column duracionReserva integer not null default 60
	check (duracionReserva between 15 and 240);

create sequence Mesa_SEQ start with 1 increment by 50;

create table Mesa (
    id integer not null,
    version integer not null,
    nombre varchar(40) not null,
    capacidad integer not null check (capacidad between 1 and 20),
    negocio_id integer not null references Negocio (id),
    primary key (id)
);

create index idx_mesa_negocio on Mesa (negocio_id);

alter table Reserva add column mesa_id integer references Mesa (id) on delete set null;
create index idx_reserva_mesa on Reserva (mesa_id);

create sequence Cierre_SEQ start with 1 increment by 50;

create table Cierre (
    id integer not null,
    version integer not null,
    fecha date not null,
    motivo varchar(120),
    negocio_id integer not null references Negocio (id),
    primary key (id),
    constraint uk_cierre_negocio_fecha unique (negocio_id, fecha)
);

-- La ocupación de una franja usa idx_reserva_negocio_fecha (V1).
