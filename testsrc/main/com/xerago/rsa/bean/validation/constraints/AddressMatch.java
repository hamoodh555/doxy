package com.xerago.rsa.bean.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import javax.validation.Payload;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xerago.rsa.dto.request.Proposer;
import com.xerago.rsa.Regex;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = AddressMatch.FieldMatchValidator.class)
@Documented
public @interface AddressMatch {
	
	String message() default "{com.xerago.rsa.util.bean.validation.constraints.AddressMatch.message}";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return The addressLine3 field
	 */
	String addressLine1();

	/**
	 * @return The addressLine4 field
	 */
	String addressLine2();
	
	/**
	 * @return The addressLine3 field
	 */
	String addressLine3();

	/**
	 * @return The addressLine4 field
	 */
	String addressLine4();
	
	/**
	 * Defines several <code>@FieldMatch</code> annotations on the same element
	 *
	 * @see AddressMatch
	 */
	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		AddressMatch[] value();
	}

	public class FieldMatchValidator implements ConstraintValidator<AddressMatch, Object> {
		
		private static final Logger LOGGER = LogManager.getRootLogger();
		
		private String addressLine1;
		private String addressLine2;
		private String addressLine3;
		private String addressLine4;
		
		@Override
		public void initialize(final AddressMatch constraintAnnotation) {
			addressLine1 = constraintAnnotation.addressLine1();
			addressLine2 = constraintAnnotation.addressLine2();
			addressLine3 = constraintAnnotation.addressLine3();
			addressLine4 = constraintAnnotation.addressLine4();
		}

		@Override
		public boolean isValid(final Object value, final ConstraintValidatorContext context) {
			
			try {
				LOGGER.info("value.getClass()::: " + value.getClass());
				Proposer proposer = (Proposer)value;
				
				StringBuilder message = new StringBuilder();
				boolean proceed = Pattern.matches(Regex.ADDRESS, proposer.getAddressLine1());
				LOGGER.info("proceed::: " + proceed);
				if(!proceed) {
					message.setLength(0);
					message.append("Please use Alphabet, Numbers and only these puncuations (.,/#)");
					LOGGER.info(message.toString());
					context.disableDefaultConstraintViolation();
					ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
					NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(addressLine1);
					nbcc.addConstraintViolation();
					return false;
				}

				proceed = Pattern.matches(Regex.ADDRESS, proposer.getAddressLine2());
				if (!proceed) {
					message.setLength(0);
					message.append("Please use Alphabet, Numbers and only these puncuations (.,/#)");
					LOGGER.info(message.toString());
					context.disableDefaultConstraintViolation();
					ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
					NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(addressLine2);
					nbcc.addConstraintViolation();
					return false;
				}
				
				
				if (StringUtils.isNotBlank(proposer.getAddressLine3())) {
					if(!(proposer.getAddressLine3().length() <= 255)){
						message.setLength(0);
						message.append("size must be between {0} and {255} characters wide");
						LOGGER.info(message.toString());
						context.disableDefaultConstraintViolation();
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(addressLine3);
						nbcc.addConstraintViolation();
						return false;
					}
					proceed = Pattern.matches(Regex.ADDRESS, proposer.getAddressLine3());
					if (!proceed) {
						message.setLength(0);
						message.append("Please use Alphabet, Numbers and only these puncuations (.,/#)");
						LOGGER.info(message.toString());
						context.disableDefaultConstraintViolation();
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(addressLine3);
						nbcc.addConstraintViolation();
						return false;
					}
				}
				
				if (StringUtils.isNotBlank(proposer.getAddressLine4())) {
					if(!(proposer.getAddressLine4().length() <= 255)){
						message.setLength(0);
						message.append("size must be between {0} and {255} characters wide");
						LOGGER.info(message.toString());
						context.disableDefaultConstraintViolation();
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(addressLine4);
						nbcc.addConstraintViolation();
						return false;
					}
					proceed = Pattern.matches(Regex.ADDRESS, proposer.getAddressLine4());
					if (!proceed) {
						message.setLength(0);
						message.append("Please use Alphabet, Numbers and only these puncuations (.,/#)");
						LOGGER.info(message.toString());
						context.disableDefaultConstraintViolation();
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(addressLine4);
						nbcc.addConstraintViolation();
						return false;
					}
				}
				
			} catch (final Exception ignore) {
				LOGGER.info(ignore.getMessage(), ignore);
				throw ignore;
			}
			return true;
		}
	}
	
}
