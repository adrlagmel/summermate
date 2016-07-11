package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.DenunciaValoracionRepository;
import domain.DenunciaValoracion;

@Component
@Transactional
public class StringToDenunciaValoracionConverter implements Converter<String, DenunciaValoracion> {
	
	@Autowired DenunciaValoracionRepository denunciaRepository;
	
	@Override 
	public DenunciaValoracion convert(String text){
		
		DenunciaValoracion result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = denunciaRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}

