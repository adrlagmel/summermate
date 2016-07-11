package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ValoracionNegocio;

@Component
@Transactional
public class ValoracionNegocioToStringConverter implements Converter<ValoracionNegocio, String>{
	
	@Override
	public String convert(ValoracionNegocio valoracionN){
		String result;
		
		if(valoracionN == null)
			result = null;
		else
			result = String.valueOf(valoracionN.getId());
		
		return result;
	}

}

