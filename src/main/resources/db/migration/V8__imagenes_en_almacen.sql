-- V8__imagenes_en_almacen.sql
--
-- Fase 8: las imágenes pueden vivir en un almacén de objetos (Cloudflare R2 u
-- otro compatible con S3). Entonces la fila guarda solo la clave del objeto.

alter table Imagen add column clave varchar(255) unique;
alter table Imagen alter column datos drop not null;
alter table Imagen add constraint imagen_contenido_check check (datos is not null or clave is not null);
