package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Playa;

@Component
@Transactional
public class PlayaToStringConverter implements Converter<Playa, String>{
	
	@Override
	public String convert(Playa playa){
		String result;
		
		if(playa == null)
			result = null;
		else
			result = String.valueOf(playa.getId());
		
		return result;
	}

}
