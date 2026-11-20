-- volumen.sql
--
-- Añade un volumen de datos realista sobre los datos de PopulateDatabase.xml
-- para medir el rendimiento (con 6 eventos y 8 usuarios no se nota nada).
--
--   docker exec -i summermate-db psql -U summermate -d summermate < scripts/volumen.sql
--
-- Genera: 500 usuarios, 100 playas, 400 negocios, 600 eventos con unos 6.000
-- inscripciones, 10.000 reservas y 2.000 valoraciones de playa. Los usuarios
-- nuevos (vol1 ... vol500) tienen la misma contraseña que usuario1.
--
-- Los ids salen de las secuencias de Hibernate con nextval(). Cada nextval
-- reserva un bloque de 50 (pooled-lo), así que no choca con los ids que
-- Hibernate genere después.

begin;

-- Usuarios --------------------------------------------------------------------

create temporary table vol_usuario as
select g as n, nextval('useraccount_seq') as cuenta, nextval('actor_seq') as actor
from generate_series(1, 500) g;

insert into useraccount (id, version, username, password)
select v.cuenta, 0, 'vol' || v.n, (select password from useraccount where username = 'usuario1')
from vol_usuario v;

insert into useraccount_authorities (useraccount_id, authority)
select cuenta, 'USUARIO' from vol_usuario;

insert into actor (id, version, nombre, apellidos, email, useraccount_id)
select actor, 0, 'Nombre' || n, 'Apellido' || (n % 97), 'vol' || n || '@example.com', cuenta
from vol_usuario;

insert into cliente (id, direccion, fechanacimiento, nacionalidad, sexo, telefono)
select actor, 'Calle ' || n, date '1990-01-01' + n, 'Española', case when n % 2 = 0 then 'HOMBRE' else 'MUJER' end, '+34600' || lpad(n::text, 6, '0')
from vol_usuario;

insert into usuario (id, estadoactual)
select actor, 'Disfrutando del verano' from vol_usuario;

insert into carpeta (id, version, nombre, actor_id)
select nextval('carpeta_seq'), 0, c.nombre, v.actor
from vol_usuario v cross join (values ('Entrada'), ('Enviados')) c(nombre);

-- Playas (del primer administrador) --------------------------------------------

create temporary table vol_playa as
select g as n, nextval('playa_seq') as id from generate_series(1, 100) g;

insert into playa (id, version, nombre, descripcion, composicion, servicios, extension,
	via, nombrevia, numerovia, codigopostal, ciudad, provincia, latitud, longitud,
	valoracionmedia, administrador_id)
select p.id, 0, 'Playa ' || p.n, 'Descripción de la playa ' || p.n, 'Arena', 'Duchas, socorrista', 500 + p.n,
	'Paseo', 'Marítimo', p.n, 11000 + p.n,
	(array['Cádiz', 'Chipiona', 'Conil', 'Tarifa', 'Málaga', 'Almería', 'Huelva', 'Rota'])[1 + p.n % 8], 'Andalucía',
	36.0 + (p.n % 20) * 0.1, -6.5 + (p.n % 30) * 0.1,
	0.0, (select min(id) from administrador)
from vol_playa p;

-- Negocios (repartidos entre los empresarios existentes) -----------------------

create temporary table vol_negocio as
select g as n, nextval('negocio_seq') as id,
	(select array_agg(id order by id) from empresario) as empresarios,
	(select array_agg(id order by id) from vol_playa) as playas
from generate_series(1, 400) g;

insert into negocio (id, version, nombre, tipo, descripcion, telefono, paginaweb, aforo,
	valoracionmedia, negocioactivo, empresario_id, playa_id)
select id, 0, 'Negocio ' || n, 'Chiringuito', 'Descripción del negocio ' || n, '+34956' || lpad(n::text, 6, '0'), null, 200,
	0.0, true, empresarios[1 + n % array_length(empresarios, 1)], playas[1 + n % array_length(playas, 1)]
from vol_negocio;

-- Eventos e inscripciones ------------------------------------------------------

create temporary table vol_evento as
select g as n, nextval('evento_seq') as id,
	(select array_agg(id order by id) from vol_negocio) as negocios
from generate_series(1, 600) g;

insert into evento (id, version, codigo, nombre, descripcion, precio, fecharegistro, fechacelebracion, negocio_id)
select id, 0, 'V' || lpad(n::text, 5, '0'), 'Evento ' || n, 'Descripción del evento ' || n, n % 20,
	now() - interval '30 days', now() + (n % 120) * interval '1 day', negocios[1 + n % array_length(negocios, 1)]
from vol_evento;

insert into evento_usuario (eventos_id, usuarios_id)
select e.id, u.actor
from vol_evento e join vol_usuario u on (u.n + e.n) % 50 = 0;

-- Reservas (a partir de mañana, de 13:00 a 22:00) --------------------------------

create temporary table vol_reserva as
select g as n, nextval('reserva_seq') as id,
	(select array_agg(id order by id) from vol_negocio) as negocios,
	(select array_agg(actor order by actor) from vol_usuario) as usuarios
from generate_series(1, 10000) g;

insert into reserva (id, version, codigo, comensales, comentarios, fumadores, fecha, fechacreacion, negocio_id, usuario_id)
select id, 0, 'R' || lpad(n::text, 8, '0'), 1 + n % 6, 'Reserva ' || n, false,
	date_trunc('day', now()) + (1 + n % 60) * interval '1 day' + (13 + n % 10) * interval '1 hour',
	now() - interval '1 day',
	negocios[1 + n % array_length(negocios, 1)], usuarios[1 + n % array_length(usuarios, 1)]
from vol_reserva;

-- Valoraciones de playa --------------------------------------------------------

insert into valoracionplaya (id, version, titulo, comentario, puntuacion, fecha, playa_id, usuario_id)
select nextval('valoracionplaya_seq'), 0, 'Valoración ' || g, 'Comentario ' || g, g % 11, now() - interval '1 day',
	(select array_agg(id order by id) from vol_playa)[1 + g % 100],
	(select array_agg(actor order by actor) from vol_usuario)[1 + g % 500]
from generate_series(1, 2000) g;

commit;

analyze;
