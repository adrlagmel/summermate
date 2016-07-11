package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.ValoracionPlayaRepository;
import domain.ValoracionPlaya;

@Component
@Transactional
public class StringToValoracionPlayaConverter implements Converter<String, ValoracionPlaya> {
	
	@Autowired ValoracionPlayaRepository valoracionPlayaRepository;
	
	@Override 
	public ValoracionPlaya convert(String text){
		
		ValoracionPlaya result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = valoracionPlayaRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}

