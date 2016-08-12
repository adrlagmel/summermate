package utilities;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dhtmlx.planner.DHXEv;
import com.dhtmlx.planner.DHXEvent;
import com.dhtmlx.planner.DHXEventsManager;

import domain.CalendarioNegocio;

public class EventsManager extends DHXEventsManager {

	public EventsManager(HttpServletRequest request) {
		super(request);
	}
	
	@SuppressWarnings("unchecked")
	public Iterable<DHXEv> getEvents() {
		
		List<DHXEv> evs = new ArrayList<DHXEv>();
		try {
			
			Collection<CalendarioNegocio> calendars = (Collection<CalendarioNegocio>) this.getRequest().getAttribute("calendar");
					
			for(CalendarioNegocio scc : calendars){
				DHXEvent e = new DHXEvent();
				
				e.setId(scc.getId());
				e.setStart_date(scc.getFechaInicio());
				e.setEnd_date(scc.getFechaFin());
				e.setText(scc.getAnotacionesReserva());
				
				evs.add(e);
			}

			
		} catch (RuntimeException e) {
			e.printStackTrace();
		} 
 
    	return evs;
	}

	
}
