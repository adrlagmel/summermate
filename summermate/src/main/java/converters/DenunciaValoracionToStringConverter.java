package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.DenunciaValoracion;

@Component
@Transactional
public class DenunciaValoracionToStringConverter implements Converter<DenunciaValoracion, String>{
	
	@Override
	public String convert(DenunciaValoracion denunciaValoracion){
		String result;
		
		if(denunciaValoracion== null)
			result = null;
		else
			result = String.valueOf(denunciaValoracion.getId());
		
		return result;
	}

}
