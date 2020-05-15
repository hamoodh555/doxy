package com.xerago.rsa.bean.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xerago.rsa.util.PortalDateConversion;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ProposerAge.NotContainsValidator.class)
public @interface ProposerAge {
		
	String message() default "{com.xerago.rsa.util.bean.validation.constraints.ProposerAge.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class NotContainsValidator implements ConstraintValidator<ProposerAge, Date> {

		private static final Logger LOGGER = LogManager.getRootLogger();

		@Override
		public boolean isValid(final Date value, final ConstraintValidatorContext context) {
			try {
				Date today = new Date();
				List<Integer> list = PortalDateConversion.calcAgeWithInceptionDate(value, today);
			
				return (CollectionUtils.isNotEmpty(list) && list.get(0) >= 18) ? true: false; 
				
			} catch (Exception e) {
				LOGGER.info(e.getMessage(), e);
				return false;
			}
		}

		@Override
		public void initialize(ProposerAge arg0) {
			// Do nothing
		}

	}

}
