package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Pago;

@Component
@Transactional
public class PagoToStringConverter implements Converter<Pago, String>{
	
	@Override
	public String convert(Pago pago){
		String result;
		
		if(pago == null)
			result = null;
		else
			result = String.valueOf(pago.getId());
		
		return result;
	}

}

