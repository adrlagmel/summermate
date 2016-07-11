package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.ReservaRepository;
import domain.Reserva;

@Component
@Transactional
public class StringToReservaConverter implements Converter<String, Reserva> {
	
	@Autowired ReservaRepository reservaRepository;
	
	@Override 
	public Reserva convert(String text){
		
		Reserva result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = reservaRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}

