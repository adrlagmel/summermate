package services;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Cliente;
import domain.ValoracionNegocio;
import domain.VotoUtil;
import repositories.ClienteRepository;
import repositories.ValoracionNegocioRepository;
import repositories.VotoUtilRepository;
import security.LoginService;
import utilities.Assert;

/*
 * Votos de "comentario útil". Vota cualquier cliente salvo el autor de la
 * valoración, una vez; puede retirar su voto.
 */
@Service
@Transactional
public class VotoUtilService {

	@Autowired
	private VotoUtilRepository			votoUtilRepository;

	@Autowired
	private ValoracionNegocioRepository	valoracionNegocioRepository;

	@Autowired
	private ClienteRepository			clienteRepository;


	public void votar(int valoracionNegocioId) {
		Cliente cliente = clientePrincipal();
		ValoracionNegocio vn = valoracionNegocioRepository.findById(valoracionNegocioId).orElse(null);
		Assert.notNull(vn);
		Assert.isTrue(vn.getReserva().getUsuario().getId() != cliente.getId());
		Assert.isTrue(votoUtilRepository.find(cliente.getId(), vn.getId()) == null);

		VotoUtil voto = new VotoUtil();
		voto.setCliente(cliente);
		voto.setValoracionNegocio(vn);
		votoUtilRepository.saveAndFlush(voto);

		actualizarContador(vn);
	}

	public void quitar(int valoracionNegocioId) {
		Cliente cliente = clientePrincipal();
		VotoUtil voto = votoUtilRepository.find(cliente.getId(), valoracionNegocioId);
		Assert.notNull(voto);

		ValoracionNegocio vn = voto.getValoracionNegocio();
		votoUtilRepository.delete(voto);
		votoUtilRepository.flush();

		actualizarContador(vn);
	}

	// Ids de las valoraciones de la lista que el usuario conectado ya votó
	@Transactional(readOnly = true)
	public Set<Integer> votadas(Collection<Integer> valoraciones) {
		if (valoraciones.isEmpty() || !LoginService.isAuthenticated())
			return Collections.emptySet();
		Cliente cliente = clienteRepository.findByUserAccountId(LoginService.getPrincipal().getId());
		if (cliente == null)
			return Collections.emptySet();
		return votoUtilRepository.votadas(cliente.getId(), valoraciones);
	}

	private void actualizarContador(ValoracionNegocio vn) {
		vn.setComentarioUtil(votoUtilRepository.contar(vn.getId()));
		valoracionNegocioRepository.save(vn);
	}

	private Cliente clientePrincipal() {
		Cliente cliente = clienteRepository.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(cliente);
		return cliente;
	}

}
