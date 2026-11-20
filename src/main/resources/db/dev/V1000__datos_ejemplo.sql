-- V1000__datos_ejemplo.sql
--
-- Datos de ejemplo para desarrollo y tests (perfil dev). Antes los cargaba
-- utilities.PopulateDatabase a partir de PopulateDatabase.xml; este fichero se
-- generó a partir de ese XML. Las contraseñas son hashes BCrypt: cada usuario
-- tiene como contraseña su nombre de usuario, salvo empresario4 (empresario).
--
-- Solo se aplica con el perfil dev (spring.flyway.locations incluye
-- classpath:db/dev). El número alto de versión deja sitio a las migraciones
-- del esquema, que van antes.

-- UserAccount
insert into UserAccount (id, version, username, password) values (1, 0, 'admin', '{bcrypt}$2a$10$14FSt52z7t0mObniZ5zLKulLZ0l5NZSDoJUI9/kMO6pnCJHUdTJdK');
insert into UserAccount (id, version, username, password) values (2, 0, 'usuario1', '{bcrypt}$2a$10$F14aMEeGfvIhnMkaSd/P2uQT4N8y1zDgnzLw7BfLlsX87oO0cchRu');
insert into UserAccount (id, version, username, password) values (3, 0, 'super', '{bcrypt}$2a$10$cLqXzfmIySgYng/mgDF41OLkCQWygkpBuGNoC0Eesz74s2UkgEmG2');
insert into UserAccount (id, version, username, password) values (4, 0, 'empresario1', '{bcrypt}$2a$10$6/otrMFg2ynUmHojnZ2VRejrvDfNdPV43p7rkoIr7Eu6x4aUM9rRC');
insert into UserAccount (id, version, username, password) values (5, 0, 'empresario2', '{bcrypt}$2a$10$ax0y1r0vh9XIXqhYYYcvM.MuwcXp..BGvW7VIJR6IhDkhgjnBbuYC');
insert into UserAccount (id, version, username, password) values (6, 0, 'usuario2', '{bcrypt}$2a$10$mqfHbAmMcIVog335yJLzt.wEaZP0lT/1or7bu/u47Hz87agp3JwrG');
insert into UserAccount (id, version, username, password) values (7, 0, 'empresario3', '{bcrypt}$2a$10$f/dkA2lbO/Z8CWRpL5yXsuOLKnVTY9H2eSC5YC2u.hv/knUcV/71e');
insert into UserAccount (id, version, username, password) values (8, 0, 'usuario3', '{bcrypt}$2a$10$4Kc8Is1PNN5gzIOKP2eNOupo9VBzS7YNgR7gvcBMk0gY5KmIIL.pm');
insert into UserAccount (id, version, username, password) values (9, 0, 'administrador2', '{bcrypt}$2a$10$avZtzB3Chum/ykxkHpK0rOUkL1fQsaUG7xmnA6e3.QYUzYvyNJtCO');
insert into UserAccount (id, version, username, password) values (10, 0, 'empresario4', '{bcrypt}$2a$10$QmSLBj6yczs.OH/1Fbc0eu0W26YI7y05qysYptkX.GYNcV5loiKsu');
insert into UserAccount (id, version, username, password) values (11, 0, 'empresario5', '{bcrypt}$2a$10$nA4Il0AdpIKcH6ID/zl0tO8xxWhPVNIN9.F4QV2TR14VZ25hIpRTS');
insert into UserAccount (id, version, username, password) values (12, 0, 'empresario6', '{bcrypt}$2a$10$YN/O9N1wdInEDDwASlDckeM6Pwmono574HTh6mlLjVBAUVdZrjaTi');
insert into UserAccount (id, version, username, password) values (13, 0, 'usuario4', '{bcrypt}$2a$10$svqC.yco2XOWiziqIewAseUdfW/CrZK26Ylm8n.0YmG4XW39Gbwtu');
insert into UserAccount (id, version, username, password) values (14, 0, 'usuario5', '{bcrypt}$2a$10$3S4Z3dVpgOPuCxdnz.cH1eNRn00uxG2Bx5Ik6XqwrvjYfSad.sv9S');
insert into UserAccount (id, version, username, password) values (15, 0, 'usuario6', '{bcrypt}$2a$10$xXjymsqT3eEnfOqz65OdzeR58Hg/OUwk4agm58g39FhhY8xv2kYZC');
insert into UserAccount (id, version, username, password) values (16, 0, 'usuario7', '{bcrypt}$2a$10$9/nQsq4se9yt3MmedlmOD.2PSlli5Sq701gFdYoapnyObpKu/Ls9W');
insert into UserAccount (id, version, username, password) values (17, 0, 'usuario8', '{bcrypt}$2a$10$OQmIV/o.eeQqBjM1xsORm.gfmvfWt17aMnBL.i23W6LT1nFLItPTC');
insert into UserAccount (id, version, username, password) values (18, 0, 'empresario7', '{bcrypt}$2a$10$t7LPVZFE7OnHt/4YRsQsFu7T89mS69IxfEC40c/HmKFdislJPG7n2');

-- UserAccount_authorities
insert into UserAccount_authorities (UserAccount_id, authority) values (1, 'ADMINISTRADOR');
insert into UserAccount_authorities (UserAccount_id, authority) values (2, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (3, 'ADMINISTRADOR');
insert into UserAccount_authorities (UserAccount_id, authority) values (3, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (3, 'EMPRESARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (4, 'EMPRESARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (5, 'EMPRESARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (6, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (7, 'EMPRESARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (8, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (9, 'ADMINISTRADOR');
insert into UserAccount_authorities (UserAccount_id, authority) values (10, 'EMPRESARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (11, 'EMPRESARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (12, 'EMPRESARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (13, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (14, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (15, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (16, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (17, 'USUARIO');
insert into UserAccount_authorities (UserAccount_id, authority) values (18, 'EMPRESARIO');

-- Actor
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (1, 0, 'Pepe', 'Cerro Daza', 'joscerdaz@alum.us.es', 1);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (2, 0, 'Adrián', 'Lagomazzini Mellado', 'adrlagmel@alum.us.es', 9);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (3, 0, 'Juan', 'Calero Romero', 'calero@gmail.com', 4);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (4, 0, 'Andrés', 'Sevilla González', 'segon@gmail.com', 5);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (5, 0, 'Marta', 'Danta López', 'dantalopez@gmail.com', 7);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (6, 0, 'Jose Miguel', 'Álvarez Pimentel', 'alvarezpimentel@gmail.com', 10);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (7, 0, 'Victor', 'García Argüelles', 'garciaarguelles@gmail.com', 11);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (8, 0, 'Andrea', 'De Miguel Hernández', 'andreademiguel@gmail.com', 12);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (9, 0, 'Marcos', 'Lorenzo Hernández', 'andreslorenzo@gmail.com', 18);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (10, 0, 'Andrés', 'Fernández Ruiz', 'afernandez@gmail.com', 2);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (11, 0, 'Laura', 'Castillo Ortíz', 'afernandez@gmail.com', 6);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (12, 0, 'Carlos', 'Martínez Portillo', 'martport@gmail.com', 8);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (13, 0, 'Miguel', 'Ortíz Cañete', 'ortmart@gmail.com', 13);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (14, 0, 'Manuela', 'Jiménez Estevez', 'jimest@gmail.com', 14);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (15, 0, 'Melisa', 'Fernández Castellanos', 'ferncaste@gmail.com', 15);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (16, 0, 'Carmen', 'Carmín Carmona', 'carmcarmcarm@gmail.com', 16);
insert into Actor (id, version, nombre, apellidos, email, userAccount_id) values (17, 0, 'Fran', 'Martín Donaire', 'martdonaire@gmail.com', 17);

-- Administrador
insert into Administrador (id) values (1);
insert into Administrador (id) values (2);

-- Cliente
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (3, '+32685545457', '1980-01-01', 'Española', 'C/ Sargento López nº 1', 'HOMBRE');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (4, '+34612345678', '1985-01-01', 'Española', 'C/ Mendoza nº7', 'HOMBRE');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (5, '+34666999777', '1989-12-11', 'Española', 'C/ Mendoza nº7', 'MUJER');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (6, '+34666545777', '1975-06-07', 'Española', 'C/ Orta nº24', 'HOMBRE');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (7, '+34664549877', '1968-11-10', 'Española', 'C/ San Sebastián nº2', 'HOMBRE');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (8, '+34667745777', '1984-07-09', 'Española', 'C/ Segovia nº31', 'MUJER');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (9, '+34667635777', '1990-07-09', 'Española', 'C/ Segovia nº32', 'HOMBRE');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (10, '+32685548974', '1981-01-01', 'Española', 'C/ Miliki nº 1', 'HOMBRE');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (11, '+32685548998', '1991-01-01', 'Española', 'C/ San Francisco nº 1', 'MUJER');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (12, '+32678696555', '1991-04-05', 'Española', 'C/ Cervera nº 5', 'HOMBRE');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (13, '+32655696555', '1991-08-06', 'Española', 'C/ Asunción nº 5', 'HOMBRE');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (14, '+32655222555', '1991-05-03', 'Española', 'C/ Candelaria nº 5', 'MUJER');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (15, '+32659623555', '1990-08-06', 'Española', 'C/ Marina nº 15', 'MUJER');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (16, '+32655669555', '1969-09-09', 'Española', 'C/ Burgos nº 145', 'MUJER');
insert into Cliente (id, telefono, fechaNacimiento, nacionalidad, direccion, sexo) values (17, '+32659396555', '1991-04-04', 'Española', 'C/ Centurión nº 24', 'HOMBRE');

-- Usuario
insert into Usuario (id, estadoActual) values (10, 'Disfrutando del Sol');
insert into Usuario (id, estadoActual) values (11, 'Estando contigo, contigo, contigo me siento feliz');
insert into Usuario (id, estadoActual) values (12, 'Keep calm!');
insert into Usuario (id, estadoActual) values (13, 'Vacaciones!');
insert into Usuario (id, estadoActual) values (14, 'Hard work!');
insert into Usuario (id, estadoActual) values (15, 'Lucha y sufrir!');
insert into Usuario (id, estadoActual) values (16, 'Leer y escribir!');
insert into Usuario (id, estadoActual) values (17, 'Practicando deporte!');

-- Empresario
insert into Empresario (id, cif) values (3, 'P15686568');
insert into Empresario (id, cif) values (4, 'T15686589');
insert into Empresario (id, cif) values (5, 'A28686589');
insert into Empresario (id, cif) values (6, 'B27778545');
insert into Empresario (id, cif) values (7, 'B27778775');
insert into Empresario (id, cif) values (8, 'B27998543');
insert into Empresario (id, cif) values (9, 'B27997743');

-- Carpeta
insert into Carpeta (id, version, nombre, actor_id) values (1, 0, 'Entrada', 1);
insert into Carpeta (id, version, nombre, actor_id) values (2, 0, 'Enviados', 1);
insert into Carpeta (id, version, nombre, actor_id) values (3, 0, 'Entrada', 3);
insert into Carpeta (id, version, nombre, actor_id) values (4, 0, 'Enviados', 3);
insert into Carpeta (id, version, nombre, actor_id) values (5, 0, 'Entrada', 10);
insert into Carpeta (id, version, nombre, actor_id) values (6, 0, 'Enviados', 10);
insert into Carpeta (id, version, nombre, actor_id) values (7, 0, 'Entrada', 4);
insert into Carpeta (id, version, nombre, actor_id) values (8, 0, 'Enviados', 4);
insert into Carpeta (id, version, nombre, actor_id) values (9, 0, 'Entrada', 11);
insert into Carpeta (id, version, nombre, actor_id) values (10, 0, 'Enviados', 11);
insert into Carpeta (id, version, nombre, actor_id) values (11, 0, 'Entrada', 5);
insert into Carpeta (id, version, nombre, actor_id) values (12, 0, 'Enviados', 5);
insert into Carpeta (id, version, nombre, actor_id) values (13, 0, 'Entrada', 12);
insert into Carpeta (id, version, nombre, actor_id) values (14, 0, 'Enviados', 12);
insert into Carpeta (id, version, nombre, actor_id) values (15, 0, 'Entrada', 2);
insert into Carpeta (id, version, nombre, actor_id) values (16, 0, 'Enviados', 2);
insert into Carpeta (id, version, nombre, actor_id) values (17, 0, 'Entrada', 6);
insert into Carpeta (id, version, nombre, actor_id) values (18, 0, 'Enviados', 6);
insert into Carpeta (id, version, nombre, actor_id) values (19, 0, 'Entrada', 7);
insert into Carpeta (id, version, nombre, actor_id) values (20, 0, 'Enviados', 7);
insert into Carpeta (id, version, nombre, actor_id) values (21, 0, 'Entrada', 8);
insert into Carpeta (id, version, nombre, actor_id) values (22, 0, 'Enviados', 8);
insert into Carpeta (id, version, nombre, actor_id) values (23, 0, 'Entrada', 13);
insert into Carpeta (id, version, nombre, actor_id) values (24, 0, 'Enviados', 13);
insert into Carpeta (id, version, nombre, actor_id) values (25, 0, 'Entrada', 14);
insert into Carpeta (id, version, nombre, actor_id) values (26, 0, 'Enviados', 14);
insert into Carpeta (id, version, nombre, actor_id) values (27, 0, 'Entrada', 15);
insert into Carpeta (id, version, nombre, actor_id) values (28, 0, 'Enviados', 15);
insert into Carpeta (id, version, nombre, actor_id) values (29, 0, 'Entrada', 16);
insert into Carpeta (id, version, nombre, actor_id) values (30, 0, 'Enviados', 16);
insert into Carpeta (id, version, nombre, actor_id) values (31, 0, 'Entrada', 17);
insert into Carpeta (id, version, nombre, actor_id) values (32, 0, 'Enviados', 17);
insert into Carpeta (id, version, nombre, actor_id) values (33, 0, 'Entrada', 9);
insert into Carpeta (id, version, nombre, actor_id) values (34, 0, 'Enviados', 9);

-- Playa
insert into Playa (id, version, nombre, descripcion, composicion, valoracionMedia, servicios, extension, via, nombreVia, codigoPostal, ciudad, provincia, latitud, longitud, administrador_id) values (1, 0, 'Matalascañas', 'La playa de Matalascañas es continua a la de Mazagón hacia el sureste de la Costa de la Luz en Huelva, también es muy larga ( más de 10 Km.), llega hasta los límites del parque nacional de Doñana con la desembocadura del Guadalquivir.', 'Arenosa', 7.0, 'Agua para aclararse, chiringuitos, etc. Autobuses urbanos y taxis.', 2000, 'Avenida', 'Sector N', 21760, 'Matalascañas', 'Huelva', 36.992590, -6.54247, 1);
insert into Playa (id, version, nombre, descripcion, composicion, valoracionMedia, servicios, extension, administrador_id, via, nombreVia, codigoPostal, ciudad, provincia, latitud, longitud) values (2, 0, 'Playa de Regla', 'Playa cosmopolita ubicada en pleno centro urbano de Chipiona             junto al Monasterio de Nuestra Señora de Regla,             con una gran oferta para el ocio Playa galardonada             con bandera azul 2015.', 'Arena dorada', 8.0, 'La playa la regla dispone de servicios habituales como autobús,             escaso aparcamiento, aseos, duchas, teléfono, alquiler de sombrillas,             alquiler de hamacas y tumbonas, deportes náuticos, club náutico.', 1250, 1, 'Paseo', 'Costa de la luz', 11550, 'Chipiona', 'Cádiz', 36.734838, -6.441144);
insert into Playa (id, version, nombre, valoracionMedia, descripcion, composicion, servicios, extension, administrador_id, via, nombreVia, codigoPostal, ciudad, provincia, latitud, longitud) values (3, 0, 'Calas de Poniente', 0.0, 'Como Calas de Poniente se conocen a siete calas de reducido             tamaño en estado virgen: Cala Encendida, Cala Aspero, Cala Pato,             Cala Medina y Cala Frailecillo, todas ellas de gran belleza y             características similares.', 'Arena dorada', 'La playa ''Calas de Poniente'' dispone de servicios habituales como autobús             interurbano, aparcamiento (no vigilado/ más de 100 plazas).', 600, 1, 'Carretera', 'CA-4202', 11140, 'Conil', 'Cádiz', 36.298889, -6.144164);
insert into Playa (id, version, nombre, valoracionMedia, descripcion, composicion, servicios, extension, administrador_id, via, nombreVia, codigoPostal, ciudad, provincia, latitud, longitud) values (4, 0, 'La Caleta', 0.0, 'Se trata de la única playa de la ciudad localizada en el casco histórico,              muy popular entre los gaditanos. Presenta características de playa urbana              y destaca por su por su belleza singular.', 'Arena dorada', 'Cuenta con un equipamiento y servicio muy completo para el usuario.', 495, 2, 'Avenida', 'Duque de Nájera', 11070, 'Cadiz', 'Cádiz', 36.530933, -6.304980);
insert into Playa (id, version, nombre, valoracionMedia, descripcion, composicion, servicios, extension, administrador_id, via, nombreVia, codigoPostal, ciudad, provincia, latitud, longitud) values (5, 0, 'La Malagueta', 0.0, 'Playa urbana muy espaciosa junto al dique de Levante            del puerto de Málaga y que, tradicionalmente, ha constituido            el punto de encuentro donde los pescadores lugareños sacan el copo.', 'Arena oscura', 'Recomendado para familias.             Ubicado en zona urbana. Zona de costa', 1200, 2, 'Paseo marítimo', 'Ciudad de Melilla', 29016, 'Málaga', 'Málaga', 36.717268, -4.410057);
insert into Playa (id, version, nombre, valoracionMedia, descripcion, composicion, servicios, extension, administrador_id, via, nombreVia, codigoPostal, ciudad, provincia, latitud, longitud) values (6, 0, 'De Nueva Almeria', 0.0, 'Playa situada en la la capital almeriense, enfrente de la             barriada de Nueva Almería, a levante de la ciudad.', 'Arena oscura / Grava ', 'Aseos, Duchas, Papeleras, Servicios de limpieza, Alquiler de hamacas y sombrillas y pasarelas de acceso', 1500, 2, 'Paseo marítimo', 'Carmen de Burgos', 04070, 'Almeria', 'Almeria', 36.825985, -2.450185);

-- Negocio
insert into Negocio (id, version, nombre, tipo, valoracionMedia, descripcion, telefono, aforo, paginaWeb, negocioActivo, empresario_id, playa_id) values (1, 0, 'Comida Fast', 'Comida rápida', 7.0, 'Restaurante de comida rápida de muy buena calidad', '+34622323132', 50, 'http://www.comidafast.com', true, 3, 1);
insert into Negocio (id, version, nombre, valoracionMedia, tipo, descripcion, telefono, aforo, paginaWeb, empresario_id, playa_id, negocioActivo) values (2, 0, 'La vie est bella', 3.0, 'Pizzeria', 'Pizzeria italiana', '+34622323138', 50, 'http://www.italianbella.com', 4, 2, true);
insert into Negocio (id, version, nombre, valoracionMedia, tipo, descripcion, telefono, aforo, paginaWeb, empresario_id, playa_id, negocioActivo) values (3, 0, 'El Faro', 7.0, 'Restaurante', 'Restaurante - Taperia en la zona costera de Chipiona', '+34652363138', 50, 'http://www.restauranteelfaro.com', 4, 2, true);
insert into Negocio (id, version, nombre, valoracionMedia, tipo, descripcion, telefono, aforo, negocioActivo, empresario_id, playa_id) values (4, 0, 'Heladeria Fran', 8.0, 'Restaurante', 'Heladeria Fran es una empresa del sector con más de 50 años en el sector de             las hosteleria, pasando por más de 3 generaciones', '+34652363138', 50, true, 7, 4);

-- PeticionNegocio
insert into PeticionNegocio (id, version, codigo, fecha, titulo, estado, comentarios, empresario_id, administrador_id) values (1, 0, '0002-CodigoPeticion', '2015-01-01 12:00:00', 'Titulo1peticionNegocio', 'ACEPTADO', 'Comentarios1peticionNegocio', 3, 1);
insert into PeticionNegocio (id, version, codigo, fecha, titulo, estado, comentarios, empresario_id, administrador_id) values (2, 0, '0003-CodigoPeticion', '2015-01-01 12:00:00', 'Peticion Negocio Hostelería', 'ACEPTADO', 'Necesitamos publicidad y creemos que Summermate puede ayudarnos', 4, 1);
insert into PeticionNegocio (id, version, codigo, fecha, titulo, estado, comentarios, empresario_id) values (3, 0, '0006-CodigoPeticion', '2015-06-02 12:00:00', 'Peticion Negocio Hostelería', 'PENDIENTE', 'Estamos interesados en los servicios que nos puede ofrecer Summermate para darle una mejor salida a nuestro negocio', 5);
insert into PeticionNegocio (id, version, codigo, fecha, titulo, estado, comentarios, empresario_id, administrador_id) values (4, 0, '0010-CodigoPeticion', '2016-02-02 12:00:00', 'Peticion Negocio Hostelería', 'RECHAZADO', 'Necesitamos publicidad y creemos que Summermate puede ayudarnos', 6, 2);
insert into PeticionNegocio (id, version, codigo, fecha, titulo, estado, comentarios, empresario_id, administrador_id) values (5, 0, '0532-CodigoPeticion', '2016-02-02 12:00:00', 'Petición de negocio en la Malagueta', 'ACEPTADO', 'Nos ponemos en contacto con vosotros para dar relance               a nuestro negocio de ultramarinos en Malaga, concretamente               en la playa de la Malagueta. Un saludo.', 7, 2);
insert into PeticionNegocio (id, version, codigo, fecha, titulo, estado, comentarios, empresario_id) values (6, 0, '0123-CodigoPeticion', '2016-03-03 11:00:00', 'Petición de Apertura de heladeria', 'PENDIENTE', 'Nos ponemos en contacto con vosotros para dar relance               a nuestro negocio de Heladeria en Cádiz, concretamente               en la playa de la Caleta Un saludo.', 8);

-- Evento
insert into Evento (id, version, codigo, nombre, descripcion, precio, fechaRegistro, fechaCelebracion, negocio_id) values (1, 0, '0001-evento', 'Pesca Deportiva', 'Curso de pesca deportiva para principiantes', 150.0, '2016-06-06 12:00:00', '2016-07-07 10:00:00', 1);
insert into Evento (id, version, codigo, nombre, descripcion, fechaRegistro, fechaCelebracion, precio, negocio_id) values (2, 0, '0002-evento', 'Travesía a nado - Chipiona', 'Travesía a nada por las playas de Chipiona. Inscripciones abiertas.', '2016-06-06 12:00:00', '2016-07-07 10:00:00', 10.0, 2);
insert into Evento (id, version, codigo, nombre, descripcion, fechaRegistro, fechaCelebracion, precio, negocio_id) values (3, 0, '0003-evento', 'XI Torneo Ajedrez', 'Undécima edición del Torneo de Ajedrez. ¿Te lo vas a perder?', '2016-06-06 12:00:00', '2016-07-07 10:00:00', 5.0, 2);
insert into Evento (id, version, codigo, nombre, descripcion, fechaRegistro, fechaCelebracion, precio, negocio_id) values (4, 0, '0004-evento', 'Travesía a nado - Chipiona', 'Travesía a nada por las playas de Chipiona. Inscripciones abiertas.', '2016-06-06 12:00:00', '2016-07-07 10:00:00', 10.0, 4);
insert into Evento (id, version, codigo, nombre, descripcion, fechaRegistro, fechaCelebracion, precio, negocio_id) values (5, 0, '0005-evento', 'Degustación pescaito frito', 'Degustación pescaito frito en nuestro restaurante. No te lo puedes perder!', '2016-06-06 12:00:00', '2016-07-07 10:00:00', 0.0, 3);
insert into Evento (id, version, codigo, nombre, descripcion, fechaRegistro, fechaCelebracion, precio, negocio_id) values (6, 0, '0006-evento', 'I Torneo Voleibol Costa de Cádiz', 'Torneo de Voleibol sobre la playa de la Caleta. Inscripciones abiertas hasta fecha de celebración.', '2016-06-06 12:00:00', '2016-07-07 10:00:00', 15.0, 4);

-- Evento_Usuario
insert into Evento_Usuario (eventos_id, usuarios_id) values (1, 10);
insert into Evento_Usuario (eventos_id, usuarios_id) values (2, 10);
insert into Evento_Usuario (eventos_id, usuarios_id) values (3, 11);
insert into Evento_Usuario (eventos_id, usuarios_id) values (4, 12);
insert into Evento_Usuario (eventos_id, usuarios_id) values (5, 13);
insert into Evento_Usuario (eventos_id, usuarios_id) values (6, 14);

-- Reserva
insert into Reserva (id, version, codigo, fechaCreacion, fecha, comentarios, comensales, fumadores, usuario_id, negocio_id) values (1, 0, '0001-reserva', '2015-10-10 12:00:00', '2016-08-10 12:00:00', 'reserva de comida para 20 personas', 2, false, 10, 1);
insert into Reserva (id, version, codigo, fechaCreacion, fecha, comentarios, comensales, fumadores, usuario_id, negocio_id) values (2, 0, '0002-reserva', '2015-09-10 12:00:00', '2016-10-10 12:00:00', 'reserva de comida para 10 personas', 2, false, 11, 1);
insert into Reserva (id, version, codigo, fechaCreacion, fecha, comentarios, comensales, fumadores, usuario_id, negocio_id) values (3, 0, '0003-reserva', '2015-08-08 12:00:00', '2016-10-02 11:00:00', 'reserva de comida para 8 personas', 8, false, 12, 2);
insert into Reserva (id, version, codigo, fechaCreacion, fecha, comentarios, comensales, fumadores, usuario_id, negocio_id) values (4, 0, '0004-reserva', '2015-07-07 10:00:00', '2015-11-11 12:00:00', 'Para 6 comensales', 2, false, 13, 3);
insert into Reserva (id, version, codigo, fechaCreacion, fecha, comentarios, comensales, fumadores, usuario_id, negocio_id) values (5, 0, '0005-reserva', '2015-12-25 12:30:00', '2016-03-03 12:00:00', 'Reserva Cena', 2, false, 14, 4);
insert into Reserva (id, version, codigo, fechaCreacion, fecha, comentarios, comensales, fumadores, usuario_id, negocio_id) values (6, 0, '0006-reserva', '2015-03-26 12:00:00', '2016-07-07 12:00:00', 'Reserva Comensales', 2, false, 15, 4);

-- CalendarioNegocio
insert into CalendarioNegocio (id, version, codigoNegocio, fechaInicio, fechaFin, anotacionesReserva, negocio_id) values (1, 0, '0001-reserva', '2016-08-10 12:00:00', '2016-08-10 12:59:00', '', 1);
insert into CalendarioNegocio (id, version, codigoNegocio, fechaInicio, fechaFin, anotacionesReserva, negocio_id) values (2, 0, '0002-reserva', '2016-10-10 12:00:00', '2016-10-10 12:59:00', '', 1);
insert into CalendarioNegocio (id, version, codigoNegocio, fechaInicio, fechaFin, anotacionesReserva, negocio_id) values (3, 0, '0003-reserva', '2016-02-10 12:00:00', '2016-02-10 12:59:00', '', 2);
insert into CalendarioNegocio (id, version, codigoNegocio, fechaInicio, fechaFin, anotacionesReserva, negocio_id) values (4, 0, '0004-reserva', '2015-11-11 12:00:00', '2015-11-11 12:59:00', '', 3);
insert into CalendarioNegocio (id, version, codigoNegocio, fechaInicio, fechaFin, anotacionesReserva, negocio_id) values (5, 0, '0005-reserva', '2016-03-03 12:00:00', '2016-03-03 12:59:00', '', 4);
insert into CalendarioNegocio (id, version, codigoNegocio, fechaInicio, fechaFin, anotacionesReserva, negocio_id) values (6, 0, '0006-reserva', '2016-07-07 12:00:00', '2016-07-07 12:59:00', '', 4);

-- ValoracionPlaya
insert into ValoracionPlaya (id, version, titulo, fecha, puntuacion, comentario, usuario_id, playa_id) values (1, 0, 'Playa en buen estado', '2016-01-25 14:30:00', 7, 'No está mal', 10, 1);
insert into ValoracionPlaya (id, version, titulo, fecha, puntuacion, comentario, usuario_id, playa_id) values (2, 0, 'Agradable!', '2016-01-25 14:30:00', 8, 'Muy buen ambiente', 11, 2);

-- ValoracionNegocio
insert into ValoracionNegocio (id, version, titulo, fecha, puntuacion, comentario, comentarioUtil, reserva_id) values (1, 0, 'Valoracion Comida Fast', '2016-01-25 14:30:00', 7, 'No está mal', 2, 1);
insert into ValoracionNegocio (id, version, titulo, fecha, puntuacion, comentario, comentarioUtil, reserva_id) values (2, 0, 'Falta de higiene!', '2016-02-19 14:30:00', 3, 'Buen servicio, mala comida', 0, 2);
insert into ValoracionNegocio (id, version, titulo, fecha, puntuacion, comentario, comentarioUtil, reserva_id) values (3, 0, 'Repetiría sin dudarlo!', '2016-02-19 14:30:00', 7, 'Buen servicio, buena comida. Repetiría.', 0, 3);
insert into ValoracionNegocio (id, version, titulo, fecha, puntuacion, comentario, comentarioUtil, reserva_id) values (4, 0, 'Calidad/precio inmejorable!', '2016-02-19 14:30:00', 8, 'Buena relación calidad/precio. Un servicio espectacular. Muy recomendable.', 0, 4);

-- DenunciaValoracion
insert into DenunciaValoracion (id, version, titulo, comentario, tipo, cliente_id, valoracionNegocio_id) values (1, 0, 'Falsedad en el comentario', 'No es conveniente que tache de tal manera la falta de higiene de mi local. Espero que retire el comentario', 'RECHAZO', 3, 2);

-- Las secuencias siguen por encima de los ids usados aquí
select setval('Actor_SEQ', 17);
select setval('CalendarioNegocio_SEQ', 6);
select setval('Carpeta_SEQ', 34);
select setval('DenunciaValoracion_SEQ', 1);
select setval('Evento_SEQ', 6);
select setval('Negocio_SEQ', 4);
select setval('PeticionNegocio_SEQ', 6);
select setval('Playa_SEQ', 6);
select setval('Reserva_SEQ', 6);
select setval('UserAccount_SEQ', 18);
select setval('ValoracionNegocio_SEQ', 4);
select setval('ValoracionPlaya_SEQ', 2);
