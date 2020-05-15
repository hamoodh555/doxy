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
import java.util.Arrays;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.commons.lang3.StringUtils;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NotContains.NotContainsValidator.class)
public @interface NotContains {
	String[] contains();

	String message() default "NotContains";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class NotContainsValidator implements ConstraintValidator<NotContains, String> {

		List<String> contains;

		@Override
		public void initialize(NotContains annotation) {
			this.contains = Arrays.asList(annotation.contains());
		}

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {

			if (StringUtils.isBlank(value) || !containsIgnoreCase(contains, value)) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(
						"There is no such value. One of the Options " + contains.toString())
							.addConstraintViolation();
				return false;
			}
			
			return true;
		}

		private boolean containsIgnoreCase(List<String> searchList, String searchTerm) {
			for (String item : searchList) {
				if (item.equalsIgnoreCase(searchTerm.trim())){
					return true;
				}
			}
			return false;
		}

	}

}
