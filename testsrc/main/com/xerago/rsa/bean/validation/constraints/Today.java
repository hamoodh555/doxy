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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = Today.NotContainsValidator.class)
public @interface Today {
	
	String message() default "{com.xerago.rsa.util.bean.validation.constraints.Today.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class NotContainsValidator implements ConstraintValidator<Today, Date> {
		
		private static final Logger LOGGER = LogManager.getRootLogger();

		@Override
		public boolean isValid(final Date value, final ConstraintValidatorContext context) {
			try {
				Date today = new Date();
				LocalDate localDateToday = LocalDateTime.ofInstant(today.toInstant(), ZoneId.systemDefault()).toLocalDate();
				LocalDate localDateValue = LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()).toLocalDate();
				
				return ( localDateValue.isEqual(localDateToday) || localDateValue.isAfter(localDateToday) ) ? true: false;
				
			} catch (Exception e) {
				LOGGER.info(e.getMessage(), e);
				return false;
			}
		}

		@Override
		public void initialize(Today arg0) {
			// Do nothing
		}

	}

}
