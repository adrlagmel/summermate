package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.EventoRepository;
import domain.Evento;

@Component
@Transactional
public class StringToEventoConverter implements Converter<String, Evento> {
	
	@Autowired EventoRepository eventoRepository;
	
	@Override 
	public Evento convert(String text){
		
		Evento result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = eventoRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}

