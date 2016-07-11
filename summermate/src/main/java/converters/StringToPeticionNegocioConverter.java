package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.PeticionNegocioRepository;
import domain.PeticionNegocio;

@Component
@Transactional
public class StringToPeticionNegocioConverter implements Converter<String, PeticionNegocio> {
	
	@Autowired PeticionNegocioRepository peticionNegocioRepository;
	
	@Override 
	public PeticionNegocio convert(String text){
		
		PeticionNegocio result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = peticionNegocioRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}
