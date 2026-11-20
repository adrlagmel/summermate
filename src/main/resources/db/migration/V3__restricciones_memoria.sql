-- V3__restricciones_memoria.sql
--
-- Restricciones de la memoria del TFG que el esquema no aplicaba (Fase 6).

-- Todo negocio tiene un empresario.
alter table Negocio alter column empresario_id set not null;

-- Aforo entre 1 y 500 (la memoria daba 0-200 y 1-200 en sitios distintos).
alter table Negocio drop constraint if exists negocio_aforo_check;
alter table Negocio add constraint negocio_aforo_check check (aforo between 1 and 500);

-- Comensales entre 1 y 10.
alter table Reserva drop constraint if exists reserva_comensales_check;
alter table Reserva add constraint reserva_comensales_check check (comensales between 1 and 10);

-- Precio en euros con céntimos exactos y nunca negativo.
alter table Evento alter column precio type numeric(7,2) using round(coalesce(precio, 0)::numeric, 2);
alter table Evento alter column precio set not null;
alter table Evento add constraint evento_precio_check check (precio >= 0);

-- El CIF es obligatorio; los que falten se marcan para que se corrijan.
update Empresario set cif = 'PENDIENTE' where cif is null or cif = '';
alter table Empresario alter column cif set not null;

-- El sexo pasa a ser opcional y con valores cerrados.
alter table Cliente alter column sexo drop not null;
update Cliente set sexo = null where sexo not in ('MUJER', 'HOMBRE', 'OTRO');
alter table Cliente add constraint cliente_sexo_check check (sexo in ('MUJER', 'HOMBRE', 'OTRO'));
