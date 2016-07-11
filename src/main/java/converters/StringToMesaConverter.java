package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.MesaRepository;
import domain.Mesa;

@Component
@Transactional
public class StringToMesaConverter implements Converter<String, Mesa> {
	
	@Autowired MesaRepository mesaRepository;
	
	@Override 
	public Mesa convert(String text){
		
		Mesa result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = mesaRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}
