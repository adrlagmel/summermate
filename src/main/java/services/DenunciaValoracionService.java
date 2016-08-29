package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.DenunciaValoracionRepository;

import domain.DenunciaValoracion;
import domain.Empresario;
import domain.ValoracionNegocio;

@Service
@Transactional
public class DenunciaValoracionService {

	
	@Autowired
	private DenunciaValoracionRepository denunciaValoracionRepository;
	
	@Autowired
	private EmpresarioService empresarioService;
	
	public DenunciaValoracionService() {

		super();

	}
	public DenunciaValoracion create(ValoracionNegocio vn){
		Empresario empresario = empresarioService.findByPrincipal();
		
		DenunciaValoracion dv= new DenunciaValoracion();
		dv.setCliente(empresario);
		dv.setValoracionNegocio(vn);
			
		return dv;
	}
	
	public void save(DenunciaValoracion dv) {   
		Assert.notNull(dv);
		
		denunciaValoracionRepository.save(dv);
	}
	
	public void delete(DenunciaValoracion dv){

		denunciaValoracionRepository.delete(dv);

	}
	
	public DenunciaValoracion findOne(int id) {

		DenunciaValoracion dv = denunciaValoracionRepository
				.findOne(id);

		return dv;

	}
	
	public DenunciaValoracion findOneToEdit(int denunciaValoracionId) {
		DenunciaValoracion result = denunciaValoracionRepository.findOne(denunciaValoracionId);
		return result;
	}

	
	public Collection<DenunciaValoracion> denunciasDelEmpresario(){
		Empresario e = empresarioService.findByPrincipal();
		int id = e.getId();
		Collection<DenunciaValoracion> result = denunciaValoracionRepository.findDenunciasByEmpresario(id);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<DenunciaValoracion> denunciasDeUnaValoracion(int valoracionNegocioId){
		
		Collection<DenunciaValoracion> result = denunciaValoracionRepository.findDenunciasByValoracion(valoracionNegocioId);
		Assert.notNull(result);
		
		return result;
		
	}
	
	
}
