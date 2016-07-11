package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Negocio;

@Component
@Transactional
public class NegocioToStringConverter implements Converter<Negocio, String>{
	
	@Override
	public String convert(Negocio negocio){
		String result;
		
		if(negocio == null)
			result = null;
		else
			result = String.valueOf(negocio.getId());
		
		return result;
	}

}

