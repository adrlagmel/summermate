package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Empresario;
import domain.PagoPremium;
import repositories.PagoPremiumRepository;
import security.LoginService;
import services.pagos.PasarelaPago;
import utilities.Assert;

/*
 * Cuenta premium del empresario (memoria del TFG). Sus negocios salen como
 * destacados. Un pago confirmado amplía premiumHasta desde hoy o desde el
 * final del periodo que ya tenga.
 */
@Service
@Transactional
public class PremiumService {

	@Autowired
	private PagoPremiumRepository	pagoRepository;

	@Autowired
	private EmpresarioService		empresarioService;

	@Autowired
	private PasarelaPago			pasarela;


	// Crea el pago pendiente y devuelve la URL de la pasarela
	public String contratar(PagoPremium.Plan plan) {
		Assert.notNull(plan);
		Empresario empresario = empresarioService.findByPrincipal();

		PagoPremium pago = new PagoPremium();
		pago.setEmpresario(empresario);
		pago.setPlan(plan);
		pago.setImporte(plan.importe);
		pago.setEstado(PagoPremium.Estado.PENDIENTE);
		pago.setFecha(LocalDateTime.now());
		pago.setReferencia(UUID.randomUUID().toString());
		pagoRepository.save(pago);

		return pasarela.iniciar(pago);
	}

	public PagoPremium pagoPropio(String referencia) {
		PagoPremium pago = pagoRepository.findByReferencia(referencia);
		Assert.notNull(pago);
		Assert.isTrue(pago.getEmpresario().getUserAccount().equals(LoginService.getPrincipal()));
		return pago;
	}

	public void confirmar(String referencia) {
		Assert.isTrue(pasarela.confirmaDesdeLaAplicacion());
		PagoPremium pago = pagoPropio(referencia);
		Assert.isTrue(pago.getEstado() == PagoPremium.Estado.PENDIENTE);

		pago.setEstado(PagoPremium.Estado.PAGADO);

		Empresario empresario = pago.getEmpresario();
		LocalDate desde = empresario.isPremium() ? empresario.getPremiumHasta().plusDays(1) : LocalDate.now();
		empresario.setPremiumHasta(desde.plusMonths(pago.getPlan().meses).minusDays(1));
	}

	public void cancelar(String referencia) {
		PagoPremium pago = pagoPropio(referencia);
		Assert.isTrue(pago.getEstado() == PagoPremium.Estado.PENDIENTE);

		pago.setEstado(PagoPremium.Estado.CANCELADO);
	}

	@Transactional(readOnly = true)
	public List<PagoPremium> misPagos() {
		return pagoRepository.findByEmpresario(empresarioService.findByPrincipal().getId());
	}

}
