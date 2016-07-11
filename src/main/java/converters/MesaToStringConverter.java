package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Mesa;

@Component
@Transactional
public class MesaToStringConverter implements Converter<Mesa, String>{
	
	@Override
	public String convert(Mesa mesa){
		String result;
		
		if(mesa == null)
			result = null;
		else
			result = String.valueOf(mesa.getId());
		
		return result;
	}

}
