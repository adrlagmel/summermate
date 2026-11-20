-- V4__denuncias_y_moderacion.sql
--
-- Fase 6: denuncia cualquier cliente, una vez por valoración, y hay dos
-- motivos más (SPAM y OTRO).

alter table DenunciaValoracion drop constraint if exists denunciavaloracion_tipo_check;
alter table DenunciaValoracion add constraint denunciavaloracion_tipo_check
	check (tipo in ('FALSEDAD', 'RECHAZO', 'BURLA', 'SPAM', 'OTRO'));

alter table DenunciaValoracion add constraint uk_denuncia_cliente_valoracion
	unique (cliente_id, valoracionNegocio_id);
