-- V1__esquema_inicial.sql
--
-- Fase 4: esquema de la base de datos tal y como quedó al terminar la Fase 3
-- (antes lo generaba Hibernate al arrancar). Generado con Hibernate 6 a partir
-- de las entidades. A partir de aquí, cualquier cambio del esquema es una
-- migración nueva (V2, V3...), nunca una edición de esta.
--
-- Los nombres no van entre comillas, así que PostgreSQL los guarda en
-- minúsculas (Actor -> actor, Actor_SEQ -> actor_seq). Las restricciones CHECK
-- las deriva Hibernate de las anotaciones de validación y de los enumerados.

create sequence Actor_SEQ start with 1 increment by 50;

create sequence CalendarioNegocio_SEQ start with 1 increment by 50;

create sequence Carpeta_SEQ start with 1 increment by 50;

create sequence DenunciaValoracion_SEQ start with 1 increment by 50;

create sequence Evento_SEQ start with 1 increment by 50;

create sequence Imagen_SEQ start with 1 increment by 50;

create sequence Mensaje_SEQ start with 1 increment by 50;

create sequence Negocio_SEQ start with 1 increment by 50;

create sequence PeticionNegocio_SEQ start with 1 increment by 50;

create sequence Playa_SEQ start with 1 increment by 50;

create sequence Reserva_SEQ start with 1 increment by 50;

create sequence UserAccount_SEQ start with 1 increment by 50;

create sequence ValoracionNegocio_SEQ start with 1 increment by 50;

create sequence ValoracionPlaya_SEQ start with 1 increment by 50;

create table Actor (
    id integer not null,
    userAccount_id integer not null unique,
    version integer not null,
    apellidos varchar(255) not null,
    email varchar(255) not null,
    nombre varchar(255) not null,
    primary key (id)
);

create table Administrador (
    id integer not null,
    primary key (id)
);

create table CalendarioNegocio (
    id integer not null,
    negocio_id integer not null,
    version integer not null,
    fechaFin timestamp(6) not null,
    fechaInicio timestamp(6) not null,
    anotacionesReserva varchar(255),
    codigoNegocio varchar(255) not null,
    primary key (id)
);

create table Carpeta (
    actor_id integer not null,
    id integer not null,
    version integer not null,
    nombre varchar(255) not null,
    primary key (id)
);

create table Cliente (
    fechaNacimiento date not null,
    id integer not null,
    direccion varchar(255) not null,
    nacionalidad varchar(255) not null,
    sexo varchar(255) not null,
    telefono varchar(255) not null,
    primary key (id)
);

create table DenunciaValoracion (
    cliente_id integer not null,
    id integer not null,
    valoracionNegocio_id integer not null,
    version integer not null,
    comentario varchar(255) not null,
    tipo varchar(255) not null check (tipo in ('FALSEDAD','RECHAZO','BURLA')),
    titulo varchar(255) not null,
    primary key (id)
);

create table Empresario (
    id integer not null,
    cif varchar(255),
    primary key (id)
);

create table Evento (
    id integer not null,
    imagen_id integer unique,
    negocio_id integer not null,
    precio float(53),
    version integer not null,
    fechaCelebracion timestamp(6) not null,
    fechaRegistro timestamp(6) not null,
    codigo varchar(255) not null,
    descripcion varchar(255) not null,
    nombre varchar(255) not null,
    primary key (id)
);

create table Evento_Usuario (
    eventos_id integer not null,
    usuarios_id integer not null
);

create table Imagen (
    id integer not null,
    tamano integer not null check (tamano>=1),
    version integer not null,
    tipo varchar(255) not null,
    datos bytea not null,
    primary key (id)
);

create table Mensaje (
    beneficiario_id integer not null,
    carpeta_id integer not null,
    id integer not null,
    remitente_id integer not null,
    version integer not null,
    fechaEnvio timestamp(6) not null,
    asunto varchar(255) not null,
    cuerpo varchar(255) not null,
    primary key (id)
);

create table Negocio (
    aforo integer not null check ((aforo>=0) and (aforo<=200)),
    empresario_id integer,
    id integer not null,
    imagen_id integer unique,
    negocioActivo boolean,
    playa_id integer,
    valoracionMedia float(53),
    version integer not null,
    descripcion varchar(255) not null,
    nombre varchar(255) not null,
    paginaWeb varchar(255),
    telefono varchar(255) not null,
    tipo varchar(255) not null,
    primary key (id)
);

create table PeticionNegocio (
    administrador_id integer,
    empresario_id integer not null unique,
    id integer not null,
    version integer not null,
    fecha timestamp(6) not null,
    codigo varchar(255) not null,
    comentarios varchar(255),
    estado varchar(255) not null check (estado in ('PENDIENTE','ACEPTADO','RECHAZADO')),
    titulo varchar(255) not null,
    primary key (id),
    unique (codigo)
);

create table Playa (
    administrador_id integer not null,
    codigoPostal integer not null check ((codigoPostal>=1000) and (codigoPostal<=99999)),
    extension integer not null check (extension>=0),
    id integer not null,
    imagen_id integer unique,
    latitud float(53) check ((latitud>=-90) and (latitud<=90)),
    longitud float(53) check ((longitud>=-180) and (longitud<=180)),
    numeroVia integer check (numeroVia>=0),
    valoracionMedia float(53),
    version integer not null,
    ciudad varchar(255) not null,
    composicion varchar(255) not null,
    descripcion varchar(255) not null,
    nombre varchar(255) not null,
    nombreVia varchar(255) not null,
    provincia varchar(255) not null,
    servicios varchar(255) not null,
    via varchar(255) not null,
    primary key (id)
);

create table Reserva (
    comensales integer check (comensales>=1),
    fumadores boolean,
    id integer not null,
    negocio_id integer not null,
    usuario_id integer not null,
    version integer not null,
    fecha timestamp(6) not null,
    fechaCreacion timestamp(6) not null,
    codigo varchar(255) not null,
    comentarios varchar(255),
    primary key (id)
);

create table UserAccount (
    id integer not null,
    version integer not null,
    username varchar(32) unique,
    password varchar(255) not null,
    primary key (id)
);

create table UserAccount_authorities (
    UserAccount_id integer not null,
    authority varchar(255)
);

create table Usuario (
    id integer not null,
    imagen_id integer unique,
    estadoActual varchar(255),
    primary key (id)
);

create table ValoracionNegocio (
    comentarioUtil integer check (comentarioUtil>=0),
    id integer not null,
    puntuacion integer not null check ((puntuacion>=0) and (puntuacion<=10)),
    reserva_id integer not null unique,
    version integer not null,
    fecha timestamp(6) not null,
    comentario varchar(255) not null,
    titulo varchar(255) not null,
    primary key (id)
);

create table ValoracionPlaya (
    id integer not null,
    playa_id integer not null,
    puntuacion integer not null check ((puntuacion>=0) and (puntuacion<=10)),
    usuario_id integer not null,
    version integer not null,
    fecha timestamp(6) not null,
    comentario varchar(255) not null,
    titulo varchar(255) not null,
    primary key (id)
);

create index idx_calendarionegocio_negocio 
   on CalendarioNegocio (negocio_id);

create index idx_calendarionegocio_codigonegocio 
   on CalendarioNegocio (codigoNegocio);

create index idx_carpeta_actor 
   on Carpeta (actor_id);

create index idx_denunciavaloracion_cliente 
   on DenunciaValoracion (cliente_id);

create index idx_denunciavaloracion_valoracionnegocio 
   on DenunciaValoracion (valoracionNegocio_id);

create index idx_evento_negocio 
   on Evento (negocio_id);

create index idx_evento_fechacelebracion 
   on Evento (fechaCelebracion);

create index idx_evento_usuario_evento 
   on Evento_Usuario (eventos_id);

create index idx_evento_usuario_usuario 
   on Evento_Usuario (usuarios_id);

create index idx_mensaje_carpeta 
   on Mensaje (carpeta_id);

create index idx_mensaje_remitente 
   on Mensaje (remitente_id);

create index idx_mensaje_beneficiario 
   on Mensaje (beneficiario_id);

create index idx_negocio_empresario 
   on Negocio (empresario_id);

create index idx_negocio_playa 
   on Negocio (playa_id);

create index idx_peticionnegocio_administrador 
   on PeticionNegocio (administrador_id);

create index idx_peticionnegocio_estado 
   on PeticionNegocio (estado);

create index idx_playa_administrador 
   on Playa (administrador_id);

create index idx_reserva_negocio_fecha 
   on Reserva (negocio_id, fecha);

create index idx_reserva_usuario_fecha 
   on Reserva (usuario_id, fecha);

create index idx_reserva_codigo 
   on Reserva (codigo);

create index idx_useraccount_authorities_useraccount 
   on UserAccount_authorities (UserAccount_id);

create index idx_valoracionplaya_playa 
   on ValoracionPlaya (playa_id);

create index idx_valoracionplaya_usuario 
   on ValoracionPlaya (usuario_id);

alter table if exists Actor 
   add constraint FKjn6go7umcllha8ho9hctcnar4 
   foreign key (userAccount_id) 
   references UserAccount;

alter table if exists Administrador 
   add constraint FK1dk48omy9rb7prhno62ehp99j 
   foreign key (id) 
   references Actor;

alter table if exists CalendarioNegocio 
   add constraint FKk6ur7f2sy874i0kot9x8m639u 
   foreign key (negocio_id) 
   references Negocio;

alter table if exists Carpeta 
   add constraint FK2b8hqpg6959dw8i3jng07l6nx 
   foreign key (actor_id) 
   references Actor;

alter table if exists Cliente 
   add constraint FK66ueha0q6mtvwbdis6c196qgh 
   foreign key (id) 
   references Actor;

alter table if exists DenunciaValoracion 
   add constraint FKq2blfpohxt93pa7a79dm8c527 
   foreign key (cliente_id) 
   references Cliente;

alter table if exists DenunciaValoracion 
   add constraint FKrwgir575i5jietw1nble7spug 
   foreign key (valoracionNegocio_id) 
   references ValoracionNegocio;

alter table if exists Empresario 
   add constraint FK5ol0fiwhy8sads9uap75gp1sd 
   foreign key (id) 
   references Cliente;

alter table if exists Evento 
   add constraint FK48bbglnunx1fl14atjlytwgmm 
   foreign key (imagen_id) 
   references Imagen;

alter table if exists Evento 
   add constraint FKib5coaxvtpmrop4nf3fefa81w 
   foreign key (negocio_id) 
   references Negocio;

alter table if exists Evento_Usuario 
   add constraint FKh21qosne2hymy80x860mf3osj 
   foreign key (usuarios_id) 
   references Usuario;

alter table if exists Evento_Usuario 
   add constraint FKpc4ega5ns3th37rj3ixmheu5j 
   foreign key (eventos_id) 
   references Evento;

alter table if exists Mensaje 
   add constraint FKc0cgmc9b6k0bkffeq2rf1p7n 
   foreign key (beneficiario_id) 
   references Actor;

alter table if exists Mensaje 
   add constraint FKdv0grjjjjn3213tlbomtkupi1 
   foreign key (carpeta_id) 
   references Carpeta;

alter table if exists Mensaje 
   add constraint FKpn41ce1frvy814hsnn3urobsr 
   foreign key (remitente_id) 
   references Actor;

alter table if exists Negocio 
   add constraint FK4ume3op66elc6o12gl70wq1k8 
   foreign key (empresario_id) 
   references Empresario;

alter table if exists Negocio 
   add constraint FK78to3l5p9qwwh1ox2v0o213rt 
   foreign key (imagen_id) 
   references Imagen;

alter table if exists Negocio 
   add constraint FKhv6cjc401j4vph0ftmel3ei1m 
   foreign key (playa_id) 
   references Playa;

alter table if exists PeticionNegocio 
   add constraint FKpdamdi1v3962hbliiv82hg5pl 
   foreign key (administrador_id) 
   references Administrador;

alter table if exists PeticionNegocio 
   add constraint FKqde138p2s17uinqj3it0j2j69 
   foreign key (empresario_id) 
   references Empresario;

alter table if exists Playa 
   add constraint FK19un6i9g5am2ayn6k241buabo 
   foreign key (administrador_id) 
   references Administrador;

alter table if exists Playa 
   add constraint FKjgh9x6fbmb54j6v4d560hm1m3 
   foreign key (imagen_id) 
   references Imagen;

alter table if exists Reserva 
   add constraint FK647lgciwv2p74xnkn5t6831mu 
   foreign key (negocio_id) 
   references Negocio;

alter table if exists Reserva 
   add constraint FKkrxqwqvewd2pls5tdigwqprj2 
   foreign key (usuario_id) 
   references Usuario;

alter table if exists UserAccount_authorities 
   add constraint FKgqsunvr5shtkfn8xvyh9wdlod 
   foreign key (UserAccount_id) 
   references UserAccount;

alter table if exists Usuario 
   add constraint FKj8etqgt6ja8room111vqb89yj 
   foreign key (imagen_id) 
   references Imagen;

alter table if exists Usuario 
   add constraint FK7uleg6ywgkl3er30w4xp4t6n9 
   foreign key (id) 
   references Cliente;

alter table if exists ValoracionNegocio 
   add constraint FKtinlfw986r0wp1q5t8umqfr0t 
   foreign key (reserva_id) 
   references Reserva;

alter table if exists ValoracionPlaya 
   add constraint FKb8mnkga08mdumg88r7gv933o2 
   foreign key (playa_id) 
   references Playa;

alter table if exists ValoracionPlaya 
   add constraint FK2javnemm7eo6i7mynn56uq3qk 
   foreign key (usuario_id) 
   references Usuario;
