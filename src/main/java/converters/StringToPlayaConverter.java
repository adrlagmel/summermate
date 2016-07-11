package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.PlayaRepository;
import domain.Playa;

@Component
@Transactional
public class StringToPlayaConverter implements Converter<String, Playa> {
	
	@Autowired PlayaRepository playaRepository;
	
	@Override 
	public Playa convert(String text){
		
		Playa result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = playaRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}
