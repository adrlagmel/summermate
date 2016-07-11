package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.EmpresarioRepository;
import domain.Empresario;

@Component
@Transactional
public class StringToEmpresarioConverter implements Converter<String, Empresario> {
	
	@Autowired EmpresarioRepository empresarioRepository;
	
	@Override 
	public Empresario convert(String text){
		
		Empresario result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = empresarioRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}

