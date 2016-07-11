package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CalendarioNegocio;

@Component
@Transactional
public class CalendarioNegocioToStringConverter implements Converter<CalendarioNegocio, String>{
	
	@Override
	public String convert(CalendarioNegocio calendarioNegocio){
		String result;
		
		if(calendarioNegocio== null)
			result = null;
		else
			result = String.valueOf(calendarioNegocio.getId());
		
		return result;
	}

}