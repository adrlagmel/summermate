-- V1001__datos_ejemplo_fase6.sql
--
-- Ajustes de los datos de ejemplo para las restricciones de la Fase 6:
-- los CIF originales no tenían un carácter de control válido.

update Empresario set cif = 'P1568656A' where id = 3;
update Empresario set cif = 'B15686587' where id = 4;
update Empresario set cif = 'A28686582' where id = 5;
update Empresario set cif = 'B27778547' where id = 6;
update Empresario set cif = 'B27778778' where id = 7;
update Empresario set cif = 'B27998541' where id = 8;
update Empresario set cif = 'B27997741' where id = 9;

-- Los eventos de ejemplo eran de 2016 y no se podían editar ni apuntarse a
-- ellos. Se pasan al próximo verano, salvo el 1, que queda como evento pasado.
update Evento set fechaCelebracion = date_trunc('year', now()) + interval '1 year 6 months 6 days 10 hours' where id <> 1;
