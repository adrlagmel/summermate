package services;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ValoracionNegocioRepository;

import domain.DenunciaValoracion;
import domain.Reserva;
import domain.ValoracionNegocio;

@Service
@Transactional
public class ValoracionNegocioService {

	
	@Autowired
	private ValoracionNegocioRepository valoracionNegocioRepository;
	
	public ValoracionNegocioService() {

		super();

	}
	public ValoracionNegocio create(Reserva r){
		ValoracionNegocio vn= new ValoracionNegocio();
		Collection<DenunciaValoracion> denuncias = new ArrayList<DenunciaValoracion>();
		
		vn.setDenuncias(denuncias);
		vn.setFecha(new Date());
		vn.setReserva(r);
		return vn;
	}
	
	public void save(ValoracionNegocio vn) {   
		Assert.notNull(vn);
		
		valoracionNegocioRepository.save(vn);
	}
	
	
	public ValoracionNegocio findOne(int id) {

		ValoracionNegocio vn = valoracionNegocioRepository
				.findOne(id);

		return vn;

	}

}
