package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.ClienteRepository;
import domain.Cliente;

@Component
@Transactional
public class StringToClienteConverter implements Converter<String, Cliente> {
	
	@Autowired ClienteRepository clienteRepository;
	
	@Override 
	public Cliente convert(String text){
		
		Cliente result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = clienteRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}

