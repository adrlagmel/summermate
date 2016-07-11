package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.MensajeRepository;
import domain.Mensaje;

@Component
@Transactional
public class StringToMensajeConverter implements Converter<String, Mensaje> {
	
	@Autowired MensajeRepository mensajeRepository;
	
	@Override 
	public Mensaje convert(String text){
		
		Mensaje result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = mensajeRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}
