package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.CarpetaRepository;
import domain.Carpeta;

@Component
@Transactional
public class StringToCarpetaConverter implements Converter<String, Carpeta> {
	
	@Autowired CarpetaRepository carpetaRepository;
	
	@Override 
	public Carpeta convert(String text){
		
		Carpeta result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = carpetaRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}

