package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.CalendarioNegocioRepository;
import domain.CalendarioNegocio;

@Component
@Transactional
public class StringToCalendarioNegocioConverter implements Converter<String, CalendarioNegocio> {
	
	@Autowired CalendarioNegocioRepository calendarioNegocioRepository;
	
	@Override 
	public CalendarioNegocio convert(String text){
		
		CalendarioNegocio result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = calendarioNegocioRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}
