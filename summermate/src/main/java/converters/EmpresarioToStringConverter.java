package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Empresario;

@Component
@Transactional
public class EmpresarioToStringConverter implements Converter<Empresario, String>{
	
	@Override
	public String convert(Empresario empresario){
		String result;
		
		if(empresario== null)
			result = null;
		else
			result = String.valueOf(empresario.getId());
		
		return result;
	}

}
