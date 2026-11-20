package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.DenunciaValoracionRepository;
import security.UserAccount;
import security.LoginService;

import domain.DenunciaValoracion;
import domain.Empresario;
import domain.ValoracionNegocio;
import forms.DenunciaValoracionForm;

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
	
	// Copia en la entidad solo lo que escribe el empresario.
	public DenunciaValoracion reconstruct(DenunciaValoracionForm form){
		DenunciaValoracion result;

		if(form.isNew()){
			ValoracionNegocio vn = form.getValoracionNegocio();

			// Solo se pueden denunciar valoraciones de un negocio propio.
			Assert.notNull(vn);
			Assert.isTrue(vn.getReserva().getNegocio().getEmpresario().getUserAccount().equals(LoginService.getPrincipal()));
			result = create(vn);
		}else{
			result = findOneToEdit(form.getId());
			form.checkVersion(result);
		}

		result.setTitulo(form.getTitulo());
		result.setComentario(form.getComentario());
		result.setTipo(form.getTipo());

		return result;
	}

	public void save(DenunciaValoracion dv) {   
		Assert.notNull(dv);
		checkPrincipal(dv);
		
		denunciaValoracionRepository.save(dv);
	}
	
	public void delete(DenunciaValoracion dv){
		checkPrincipal(dv);

		denunciaValoracionRepository.delete(dv);

	}
	
	public DenunciaValoracion findOne(int id) {

		DenunciaValoracion dv = denunciaValoracionRepository
				.findById(id).orElse(null);

		return dv;

	}
	
	public DenunciaValoracion findOneToEdit(int denunciaValoracionId) {
		DenunciaValoracion result = denunciaValoracionRepository.findById(denunciaValoracionId).orElse(null);

		checkPrincipal(result);

		return result;
	}

	// Antes no se comprobaba: cualquier empresario podía editar o borrar la
	// denuncia de otro cambiando el id.
	public void checkPrincipal(DenunciaValoracion denuncia) {
		Assert.notNull(denuncia);

		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(denuncia.getCliente().getUserAccount().equals(userAccount));
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
