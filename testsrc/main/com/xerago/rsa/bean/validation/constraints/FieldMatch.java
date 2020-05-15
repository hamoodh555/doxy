package com.xerago.rsa.bean.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatch.FieldMatchValidator.class)
@Documented
public @interface FieldMatch {
	
	String message() default "{com.xerago.rsa.util.bean.validation.constraints.Today.message}";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return The planName field
	 */
	String planName();

	/**
	 * @return The planType field
	 */
	String planType();
	
	/**
	 * @return The sumInsured field
	 */
	String sumInsured();
	
	/**
	 * @return The maximumDurationPerTrip field
	 */
	String maximumDurationPerTrip();
	
	/**
	 * @return The travelStartDate field
	 */
	String travelStartDate();
	
	/**
	 * @return The travelEndDate field
	 */
	String travelEndDate();

	/**
	 * Defines several <code>@FieldMatch</code> annotations on the same element
	 *
	 * @see FieldMatch
	 */
	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		FieldMatch[] value();
	}

	public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
		
		private static final Logger LOGGER = LogManager.getRootLogger();
		
		private String planNameValue;
		private String planTypeValue;
		private String sumInsuredValue;
		private String maximumDurationPerTripValue;
		private String travelStartDateValue;
		private String travelEndDateValue;

		@Override
		public void initialize(final FieldMatch constraintAnnotation) {
			planNameValue = constraintAnnotation.planName();
			planTypeValue = constraintAnnotation.planType();
			sumInsuredValue = constraintAnnotation.sumInsured();
			maximumDurationPerTripValue = constraintAnnotation.maximumDurationPerTrip();
			travelStartDateValue = constraintAnnotation.travelStartDate();
			travelEndDateValue = constraintAnnotation.travelEndDate();
			
		}

		@Override
		public boolean isValid(final Object value, final ConstraintValidatorContext context) {/*
			boolean wrong = false;
			
			try {
				Product product = (Product) value;
				final String planNameFromRequest = product.getPlanName();
				final String planTypeValueFromRequest = product.getPlanType();
				final Integer sumInsuredValueFromRequest= product.getSumInsured();
				final Integer maximumDurationPerTripRequest = product.getMaximumDurationPerTrip();
				final Date travelStartDateRequest = product.getTravelStartDate();
				final Date travelEndDateRequest = product.getTravelEndDate();
				
				StringBuilder message = new StringBuilder();
				Date currentDate = new Date(); 
				
				*//**
				 * Travel start and End date comparison
				 *//*
				LocalDate travelStartDate = LocalDateTime.ofInstant(travelStartDateRequest.toInstant(), ZoneId.systemDefault()).toLocalDate();
				LocalDate travelEndDate = LocalDateTime.ofInstant(travelEndDateRequest.toInstant(), ZoneId.systemDefault()).toLocalDate();
				LocalDate localDate = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
				
				if(travelStartDate.isBefore(localDate)) {
					message.setLength(0);
					LOGGER.info("Travel start date should be start from today or future date in the format of 'DD/MM/YYYY'.");
					message.append("Travel start date should be start from today or future date");
					context.disableDefaultConstraintViolation();
					ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
					NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(travelStartDateValue);
					nbcc.addConstraintViolation();
					return false;
				}
				
				if(travelEndDate.isBefore(travelStartDate)) {
					message.setLength(0);
					message.append("The Travelenddate should be equal or greater than TravelstartDate.");
					LOGGER.info(message.toString());
					context.disableDefaultConstraintViolation();
					ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
					NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(travelEndDateValue);
					nbcc.addConstraintViolation();
					return false;
				}
				
				if ( planNameFromRequest.equalsIgnoreCase(Constants.MULTI_TRIP) ){}else {
					
					if( PortalDateConversion.daysBetween(currentDate, travelStartDateRequest) >= 181 ){
						message.setLength(0);
						message.append("The Travelstartdate should be within 180 days from CurrentDate.");
						LOGGER.info(message.toString());
						context.disableDefaultConstraintViolation();
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(travelStartDateValue);
						nbcc.addConstraintViolation();
						return false;
					}else if( PortalDateConversion.daysBetween(travelStartDateRequest, travelEndDateRequest) >= 181 ){
						message.setLength(0);
						message.append("The Travelenddate should be within 180 days from Travelstartdate.");
						LOGGER.info(message.toString());
						context.disableDefaultConstraintViolation();
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(travelEndDateValue);
						nbcc.addConstraintViolation();
						return false;
					}
					
				}
				
				if( Constants.LEISURE_TRIP.equalsIgnoreCase(planNameFromRequest) ){
					
					LOGGER.info("1");
					wrong = validValues(Constants.LEISURE_SUBPLANTYPES, planTypeValueFromRequest, message);
					if(wrong) {
						wrong = validValues(Constants.LEISURE_SI_MAP.get(planTypeValueFromRequest), sumInsuredValueFromRequest, message);
						if(!wrong) {
							context.disableDefaultConstraintViolation();
							LOGGER.info(message.toString());
							ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
							NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(sumInsuredValue);
							nbcc.addConstraintViolation();
						}
					} else {
						LOGGER.info("wrong:: " + wrong);
						LOGGER.info(message.toString());
						context.disableDefaultConstraintViolation();
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(planTypeValue);
						nbcc.addConstraintViolation();
					}
					
				}else if( Constants.MULTI_TRIP.equalsIgnoreCase(planNameFromRequest) ){
					
					LOGGER.info("2");
					wrong = validValues(Constants.MULTI_SUBPLANTYPES, planTypeValueFromRequest, message);
					if(wrong) {
						wrong = validValues(Constants.MULTI_SI_MAP.get(planTypeValueFromRequest), sumInsuredValueFromRequest, message);
						if(wrong) {
							wrong = validValues(Constants.MULTI_MAXDURATIONPERTRIP, maximumDurationPerTripRequest, message);
							if(!wrong) {
								LOGGER.info(message.toString());
								LOGGER.info("Constants.MULTI_MAXDURATIONPERTRIP::: " + Constants.MULTI_MAXDURATIONPERTRIP);
								LOGGER.info("Constants.maximumDurationPerTripRequest::: " + maximumDurationPerTripRequest);
								context.disableDefaultConstraintViolation();
								ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
								NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(maximumDurationPerTripValue);
								nbcc.addConstraintViolation();
							}
						} else {
							context.disableDefaultConstraintViolation();
							LOGGER.info(message.toString());
							ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
							NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(sumInsuredValue);
							nbcc.addConstraintViolation();
						}
					} else {
						context.disableDefaultConstraintViolation();
						LOGGER.info(message.toString());
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(planTypeValue);
						nbcc.addConstraintViolation();
					}
					
				}else if( Constants.SENIOR_CITIZEN.equalsIgnoreCase(planNameFromRequest) ){
					
					LOGGER.info("3");
					wrong = validValues(Constants.SENIOR_CITIZEN_SI, sumInsuredValueFromRequest, message);
					if(!wrong) {
						context.disableDefaultConstraintViolation();
						LOGGER.info(message.toString());
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(sumInsuredValue);
						nbcc.addConstraintViolation();
					}
					
				}else if( Constants.ASIA.equalsIgnoreCase(planNameFromRequest) ){
					
					LOGGER.info("4");
					wrong = validValues(Constants.ASIA_SI, sumInsuredValueFromRequest, message);
					if(!wrong) {
						context.disableDefaultConstraintViolation();
						LOGGER.info(message.toString());
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(sumInsuredValue);
						nbcc.addConstraintViolation();
					}
					
				}else if( Constants.STUDENT.equalsIgnoreCase(planNameFromRequest) ){
					
					LOGGER.info("5");
					wrong = validValues(Constants.STUDENT_SUBPLANTYPES, planTypeValueFromRequest, message);
					if(wrong) {
						wrong = validValues(Constants.STUDENT_SI_MAP.get(planTypeValueFromRequest), sumInsuredValueFromRequest, message);
						if(!wrong) {
							context.disableDefaultConstraintViolation();
							LOGGER.info(message.toString());
							ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
							NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(sumInsuredValue);
							nbcc.addConstraintViolation();
						}
					} else {
						context.disableDefaultConstraintViolation();
						LOGGER.info(message.toString());
						ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(message.toString());
						NodeBuilderCustomizableContext nbcc = constraintViolationBuilder.addPropertyNode(planTypeValue);
						nbcc.addConstraintViolation();
					}
					
				}
				
			} catch (final Exception ignore) {
				LOGGER.info(ignore.getMessage(), ignore);
				throw ignore;
			}
			return wrong;			
		*/	return false;
			}
		
		private static boolean validValues(Object[] object, Object value, StringBuilder message ) {
			message.setLength(0);
			java.util.List<Object> list = Arrays.asList(object);
			LOGGER.info("object:: " + Arrays.toString(object) + " ::: value:: " + value);
	
			boolean okay = false;
			if(value instanceof String ){
				okay = list.stream().filter(s -> ((String) s).equalsIgnoreCase((String) value)).findFirst().isPresent();
			} else {
				okay = list.stream().filter(s -> (s).equals(value)).findFirst().isPresent();
			}
			
			if(!okay) {
				message.append("There is no such value. Possible values are " + list.toString());
			}
			return okay;
		}
	}
	
}
