package utilities.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotFalseValidator implements ConstraintValidator<NotFalse, Boolean> {   
    @Override
    public void initialize(NotFalse constraintAnnotation) {       
    }
	@Override
    public boolean isValid(Boolean b, ConstraintValidatorContext context){   
       return b!=false;
    }     
}