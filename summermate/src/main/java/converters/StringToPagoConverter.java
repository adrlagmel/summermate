package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.PagoRepository;
import domain.Pago;

@Component
@Transactional
public class StringToPagoConverter implements Converter<String, Pago> {
	
	@Autowired PagoRepository pagoRepository;
	
	@Override 
	public Pago convert(String text){
		
		Pago result;
		int id;
		
		try{
			if(StringUtils.isEmpty(text))
				result = null;
			else{
				id = Integer.valueOf(text);
				result = pagoRepository.findOne(id);
			}
			
		} catch(Throwable oops){
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}
	

}
