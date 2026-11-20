package utilities.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotFalseValidator implements ConstraintValidator<NotFalse, Boolean> {   
    @Override
    public void initialize(NotFalse constraintAnnotation) {       
    }
	@Override
    public boolean isValid(Boolean b, ConstraintValidatorContext context){   
       return b!=false;
    }     
}