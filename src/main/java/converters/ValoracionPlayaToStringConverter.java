package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ValoracionPlaya;

@Component
@Transactional
public class ValoracionPlayaToStringConverter implements Converter<ValoracionPlaya, String>{
	
	@Override
	public String convert(ValoracionPlaya valoracionP){
		String result;
		
		if(valoracionP == null)
			result = null;
		else
			result = String.valueOf(valoracionP.getId());
		
		return result;
	}

}