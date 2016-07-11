package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.NegocioRepository;
import domain.Negocio;

@Component
@Transactional
public class StringToNegocioConverter implements Converter<String, Negocio> {
	
	@Autowired NegocioRepository negocioRepository;
	
	@Override 
	public Negocio convert(String text){
		
		Negocio result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = negocioRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}
