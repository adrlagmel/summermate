package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.PeticionNegocio;

@Component
@Transactional
public class PeticionNegocioToStringConverter implements Converter<PeticionNegocio, String>{
	
	@Override
	public String convert(PeticionNegocio pNegocio){
		String result;
		
		if(pNegocio == null)
			result = null;
		else
			result = String.valueOf(pNegocio.getId());
		
		return result;
	}

}

