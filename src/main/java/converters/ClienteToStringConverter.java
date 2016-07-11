package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Cliente;

@Component
@Transactional
public class ClienteToStringConverter implements Converter<Cliente, String>{
	
	@Override
	public String convert(Cliente cliente){
		String result;
		
		if(cliente== null)
			result = null;
		else
			result = String.valueOf(cliente.getId());
		
		return result;
	}

}
