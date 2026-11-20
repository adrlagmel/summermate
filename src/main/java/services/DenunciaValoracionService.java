package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.Assert;

import repositories.DenunciaValoracionRepository;
import security.UserAccount;
import security.LoginService;

import domain.DenunciaValoracion;
import domain.Cliente;
import domain.ValoracionNegocio;
import forms.DenunciaValoracionForm;

@Service
@Transactional
public class DenunciaValoracionService {

	
	@Autowired
	private DenunciaValoracionRepository denunciaValoracionRepository;
	
	@Autowired
	private EmpresarioService empresarioService;

	@Autowired
	private repositories.ClienteRepository clienteRepository;
	
	public DenunciaValoracionService() {

		super();

	}
	// Cualquier cliente (usuario o empresario) puede denunciar una valoración
	// de negocio, salvo la suya y solo una vez.
	public DenunciaValoracion create(ValoracionNegocio vn){
		Cliente cliente = clientePrincipal();
		Assert.notNull(vn);
		Assert.isTrue(puedeDenunciar(cliente, vn));

		DenunciaValoracion dv= new DenunciaValoracion();
		dv.setCliente(cliente);
		dv.setValoracionNegocio(vn);
			
		return dv;
	}
	
	// Copia en la entidad solo lo que escribe el empresario.
	public DenunciaValoracion reconstruct(DenunciaValoracionForm form){
		DenunciaValoracion result;

		if(form.isNew()){
			ValoracionNegocio vn = form.getValoracionNegocio();

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

	
	public Cliente clientePrincipal(){
		Cliente cliente = clienteRepository.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(cliente);
		return cliente;
	}

	public boolean puedeDenunciar(Cliente cliente, ValoracionNegocio vn){
		return vn.getReserva().getUsuario().getId() != cliente.getId()
			&& !denunciaValoracionRepository.yaDenunciada(cliente.getId(), vn.getId());
	}

	// Moderación del administrador: descartar una denuncia sin tocar la valoración.
	public void descartar(DenunciaValoracion dv){
		Assert.notNull(dv);
		denunciaValoracionRepository.delete(dv);
	}

	public java.util.List<DenunciaValoracion> findAllParaModerar(){
		return denunciaValoracionRepository.findAllParaModerar();
	}

	public Collection<DenunciaValoracion> misDenuncias(){
		int id = clientePrincipal().getId();
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
