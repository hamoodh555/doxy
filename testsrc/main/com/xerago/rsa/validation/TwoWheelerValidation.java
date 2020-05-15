package com.xerago.rsa.validation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xerago.rsa.util.Constants;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.model.MotorInsuranceModel;

@Component
public class TwoWheelerValidation implements Constants {

	private static final Log LOGGER = LogFactory.getLog(TwoWheelerValidation.class);
	
	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	/*
	 * Swagger Validaton Start
	 */
	public List<String> validateGetQuoteService(MotorInsuranceModel motorInsuranceModel) throws Exception {
		List<String> errorCode = new ArrayList<>();
		LOGGER.info("Validate Quote");
		Date dtCurrent = new Date();
		String strCurrentDt = objSimpleDateFormat.format(dtCurrent);
		Date dtToday = objSimpleDateFormat.parse(objSimpleDateFormat.format(dtCurrent));
		Calendar calendar = new GregorianCalendar();

		LOGGER.info("ProductName =" + motorInsuranceModel.getProductName());

		if ("RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {
			
			if(motorInsuranceModel.getYearOfManufacture() >= 0){
				int maxYear = Calendar.getInstance().get(Calendar.YEAR);
				LOGGER.info("maxYear ="+maxYear);
				int minYear= Calendar.getInstance().get(Calendar.YEAR) -9;
				LOGGER.info("minYear ="+minYear);
				if(!(minYear <= motorInsuranceModel.getYearOfManufacture() && maxYear >= motorInsuranceModel.getYearOfManufacture())){
					errorCode.add("E-0405");
				}
				}
			
			if (TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {

				if (!"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover()) && 
						(StringUtils.isBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())
						|| motorInsuranceModel.getPreviousPolicyExpiryDate().equalsIgnoreCase(DATE_DEFAULT))) {
					errorCode.add("E-0029");
				}
				try {
					Date vehicleRegistrationDate = objSimpleDateFormat
							.parse(motorInsuranceModel.getVehicleRegistrationDate());
					calendar.setTime(vehicleRegistrationDate);
					int yearOfManufacture = motorInsuranceModel.getYearOfManufacture();
					Date previousPolicyExpiryDate = new Date();
					if (motorInsuranceModel.getPreviousPolicyExpiryDate() != null
							&& !motorInsuranceModel.getPreviousPolicyExpiryDate().equalsIgnoreCase(DATE_DEFAULT)) {
						LOGGER.info("motorInsuranceModel.getPreviousPolicyExpiryDate() ::: "
								+ motorInsuranceModel.getPreviousPolicyExpiryDate());
						previousPolicyExpiryDate = objSimpleDateFormat
								.parse(motorInsuranceModel.getPreviousPolicyExpiryDate());
						if (previousPolicyExpiryDate.before(vehicleRegistrationDate)) {
							errorCode.add("E-0038");

						}
					}
					calendar = new GregorianCalendar();
					calendar.setTime(previousPolicyExpiryDate);
					int preExpYear = calendar.get(Calendar.YEAR);
					if (preExpYear > 0 && yearOfManufacture > 0 && preExpYear < yearOfManufacture) {
						errorCode.add("E-0039");
					}
					if (StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegistrationDate())
							&& StringUtils.isNotBlank(motorInsuranceModel.getPolicyStartDate())) {
						Date policyStartDate = objSimpleDateFormat.parse(motorInsuranceModel.getPolicyStartDate());
						if (vehicleRegistrationDate.after(policyStartDate)) {
							errorCode.add("E-0040");

						}
					}
					
					/*if (TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {
						try {
							long differenceInDays = TwoWheelerValidation
									.diffDates(motorInsuranceModel.getPreviousPolicyExpiryDate(), strCurrentDt);
							LOGGER.info("differenceInDays -PolicyStartDate_RequestxmlFormOnly:" + differenceInDays);
							LOGGER.info("differenceInDays:" + differenceInDays + ":strCurrentDt:" + strCurrentDt
									+ ":IF::" + (differenceInDays >= 0));
							if (differenceInDays > 0) {
								motorInsuranceModel.setPolicyStartDate(strCurrentDt);
							}

						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
						}
					}*/
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			/*
			 * if
			 * (StringUtils.isBlank(motorInsuranceModel.getPreviousPolicyType())
			 * ) { errorCodes.add("E-0030");
			 * 
			 * }
			 */
			if(!"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())){
				LOGGER.info("NoClaimBonusPercentinCurrent =" + motorInsuranceModel.getNoClaimBonusPercentinCurrent());
				LOGGER.info("vehicle ownership = "+motorInsuranceModel.getCarOwnerShip());
				if (StringUtils.isBlank(motorInsuranceModel.getNoClaimBonusPercentinCurrent()) ) {
					errorCode.add("E-0031");
				}
				if (StringUtils.isBlank(motorInsuranceModel.getClaimsMadeInPreviousPolicy())) {
					errorCode.add("E-0032");
				} else if (!YES.equalsIgnoreCase(motorInsuranceModel.getClaimsMadeInPreviousPolicy())
						&& !NO.equalsIgnoreCase(motorInsuranceModel.getClaimsMadeInPreviousPolicy())) {
					errorCode.add("E-0396");
				} else if (YES.equalsIgnoreCase(motorInsuranceModel.getClaimsMadeInPreviousPolicy())) {
					/*Bug #360616 Validation needs to be removed 
					 * if (StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent()) && !"DBS".equalsIgnoreCase(motorInsuranceModel.getAgentId())) {
						if (!"1".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
								&& !"2".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
								&& !"3".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
								&& !"4".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
								&& !"5".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
								&& !"6".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())) {
							errorCode.add("E-0386");

						}
					}*/
					if (StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
						if (!"0".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
							errorCode.add("E-0387");
						}
					}
				} else if (NO.equalsIgnoreCase(motorInsuranceModel.getClaimsMadeInPreviousPolicy()) && NO.equalsIgnoreCase(motorInsuranceModel.getCarOwnerShip() )) {
					if (StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent())
							&& "1".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
							&& !"20".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
						errorCode.add("E-0388");
					} else if (StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent())
							&& "2".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
							&& !"25".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
						errorCode.add("E-0389");
					} else if (StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent())
							&& "3".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
							&& !"35".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
						errorCode.add("E-0391");
					} else if (StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent())
							&& "4".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
							&& !"45".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
						errorCode.add("E-0392");
					} else if (StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent())
							&& "5".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
							&& !"50".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
						errorCode.add("E-0393");
					} else if (StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent())
							&& "6".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercent())
							&& !"50".equalsIgnoreCase(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
						errorCode.add("E-0394");
					}
				}
			}
			
		}
		if(motorInsuranceModel.getYearOfManufacture() >= 0 && "BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int previousyear = Calendar.getInstance().get(Calendar.YEAR) - 1;
			if(currentYear != motorInsuranceModel.getYearOfManufacture() && previousyear != motorInsuranceModel.getYearOfManufacture()){
				errorCode.add("E-0404");
			}
			}
		
		if(StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegisteredCity())){
			boolean status = twoWheelerDAO.getStateValueForCity(motorInsuranceModel.getVehicleRegisteredCity());
			if (status) {
				errorCode.add("E-0062");
			}
		}

		LOGGER.info("name =" + motorInsuranceModel.getStrFirstName());
		if (StringUtils.isBlank(motorInsuranceModel.getStrFirstName())) {
			errorCode.add("E-0204");
		} else if (!(motorInsuranceModel.getStrFirstName().length() >= 0
				&& motorInsuranceModel.getStrFirstName().length() <= 30)) {
			errorCode.add("E-0214");
		} else if (!stringValueWithSpace.matcher(motorInsuranceModel.getStrFirstName()).find()) {
			LOGGER.info("strFirstName  Validation failed");
			errorCode.add("E-0203");
		}
		LOGGER.info("Email::"+motorInsuranceModel.getStrEmail());
		if (StringUtils.isBlank(motorInsuranceModel.getStrEmail())) {
			errorCode.add("E-0207");
		} else if (!(motorInsuranceModel.getStrEmail().length() >= 0
				&& motorInsuranceModel.getStrEmail().length() <= 50)) {
			errorCode.add("E-0215");
		} else if (!emailValidation.matcher(motorInsuranceModel.getStrEmail()).find()) {
			LOGGER.info("EmailId Validation failed");
			errorCode.add("E-0206");
		}

		if (StringUtils.isBlank(motorInsuranceModel.getStrMobileNo())) {
			errorCode.add("E-0216");
		} else if (!(motorInsuranceModel.getStrMobileNo().length() >= 0
				&& motorInsuranceModel.getStrMobileNo().length() <= 10)) {
			errorCode.add("E-0217");
		} else if (!mobileNoValidation.matcher(motorInsuranceModel.getStrMobileNo()).find()) {
			LOGGER.info("mobile  Validation failed");
			errorCode.add("E-0210");
		}
		
		
		return errorCode;
	}

	public static long diffDates(String start, String end) {

		LOGGER.info("start:: " + start);
		LOGGER.info("end::: " + end);

		long lnDiffInDays = 0;
		String dtEnd = "";
		try {
			if (end != null) {
				dtEnd = StringUtils.trim(end);
			}
			if (!Constants.DATE_DEFAULT.equalsIgnoreCase(start) && !Constants.DATE_DEFAULT.equalsIgnoreCase(dtEnd)) {
				DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
				Date d1 = dateFormat.parse(start);
				Date d2 = dateFormat.parse(dtEnd);
				lnDiffInDays = diffDates(d1, d2);
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		LOGGER.info("lnDiffInDays::: " + lnDiffInDays);
		return lnDiffInDays;
	}

	public static long diffDates(Date start, Date end) {

		LOGGER.info("start:: " + start);
		LOGGER.info("end::: " + end);
		long lnDiffInDays = 0;
		try {
			Calendar calStart = Calendar.getInstance();
			calStart.setTime(start);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(end);
			Calendar d = Calendar.getInstance();
			d.setTimeInMillis(calEnd.getTimeInMillis() - calStart.getTimeInMillis());
			lnDiffInDays = d.getTime().getTime() / 86400000;
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		LOGGER.info("lnDiffInDays::: " + lnDiffInDays);
		return lnDiffInDays;
	}

	public List<String> validateCalculatePremiumService(MotorInsuranceModel motorInsuranceModel) throws Exception {
		List<String> errorCode = new ArrayList<>();
		errorCode = validateGetQuoteService(motorInsuranceModel);
		
		/*LOGGER.info("name =" + motorInsuranceModel.getStrLastName());
		if (StringUtils.isBlank(motorInsuranceModel.getStrLastName())) {
			errorCode.add("E-0224");
		}else if (!(motorInsuranceModel.getStrLastName().length() >= 0
					&& motorInsuranceModel.getStrLastName().length() <= 30)) {
				errorCode.add("E-0223");
			} else if(!stringValueWithSpace.matcher(motorInsuranceModel.getStrLastName()).find()) {
				LOGGER.info("strLastName  Validation failed");
				errorCode.add("E-0205");
			}*/
		if ("RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())) {

			if (StringUtils.isBlank(motorInsuranceModel.getCarOwnerShip())) {
				errorCode.add("E-0305");
			} else {
				if (!(YES.equalsIgnoreCase(motorInsuranceModel.getCarOwnerShip())
						|| NO.equalsIgnoreCase(motorInsuranceModel.getCarOwnerShip()))) {
					errorCode.add("E-0306");
				}
			}
			LOGGER.info("PNL::::"+motorInsuranceModel.getProductName());
			if (StringUtils.isBlank(motorInsuranceModel.getPreviousPolicyType())) {
				errorCode.add("E-0030");
			} else if ( "RolloverTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName()) && !"Comprehensive".equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyType())
					&& !"Thirdparty".equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyType())) {
				errorCode.add("E-0047");

			}else if ("Thirdparty".equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyType()) && !"LiabilityOnly".equalsIgnoreCase(motorInsuranceModel.getTypeOfCover())) {
				errorCode.add("E-0058");
			}

		}
		LOGGER.info("RegisteredInTheNameOf =" + motorInsuranceModel.getVehicleRegisteredInTheNameOf());
		if (StringUtils.isBlank(motorInsuranceModel.getVehicleRegisteredInTheNameOf())) {
			errorCode.add("E-0023");
		} else if (!"Individual".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())
				&& !"Company".equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())) {
			errorCode.add("E-0047");
		}

		if (StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegisteredInTheNameOf())
				&& COMPANY.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())) {
			String strCompName = motorInsuranceModel.getCompanyNameForCar();
			if (strCompName == null || strCompName.length() <= 0) {
				errorCode.add("E-0024");
			} else if (!(strCompName.length() >= 0 && strCompName.length() <= 50)) {
				errorCode.add("E-0381");
			} else if (!alphaNumreicWithSpace.matcher(strCompName).find()) {
				errorCode.add("E-0382");
			}

		}

		LOGGER.info("Title =" + motorInsuranceModel.getStrTitle());
		if (StringUtils.isBlank(motorInsuranceModel.getStrTitle())) {
			errorCode.add("E-0202");
		} else if (!"Mr".equalsIgnoreCase(motorInsuranceModel.getStrTitle())
				&& !"Ms".equalsIgnoreCase(motorInsuranceModel.getStrTitle())
				&& !"Mrs".equalsIgnoreCase(motorInsuranceModel.getStrTitle())) {
			errorCode.add("E-0201");
		}
		if (StringUtils.isNotBlank(motorInsuranceModel.getStdCode())) {
			if (!numericValidation.matcher(motorInsuranceModel.getStdCode()).find()) {
				errorCode.add("E-0208");
			} else if (!(motorInsuranceModel.getStdCode().length() >= 1
					&& motorInsuranceModel.getStdCode().length() <= 6)) {
				errorCode.add("E-0218");
			}
		}

		/*
		 * if (StringUtils.isBlank(motorInsuranceModel.getDateOfBirth()) ||
		 * DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getDateOfBirth())
		 * ||
		 * DATE_FORMAT.equalsIgnoreCase(motorInsuranceModel.getDateOfBirth())) {
		 * motorInsuranceModel.setStatusFlag("E-0212");
		 * 
		 * }
		 */
		LOGGER.info("Occupation =" + motorInsuranceModel.getOccupation());
		if(!"DBS".equalsIgnoreCase(motorInsuranceModel.getAgentId())){
		if (StringUtils.isBlank(motorInsuranceModel.getOccupation())) {
			errorCode.add("E-0219");
		} else if (!"Armed Forces".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Business / Sales Profession".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Central / State Government Employee".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Corporate Executive".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Engineering Profession".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Financial Services Profession".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Heads of Government".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Heads of State".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Home Maker / Housewife".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"IT Profession".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Medical Profession".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Musician / Artist".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Sports Person".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Student".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Teaching Profession".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Political Party Official".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Politician".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Senior Government Official".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Senior Judicial Official".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Senior Military Official".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"State-owned Corporation Official".equalsIgnoreCase(motorInsuranceModel.getOccupation())
				&& !"Others".equalsIgnoreCase(motorInsuranceModel.getOccupation())) {
			errorCode.add("E-0220");
		}
		}
		if (StringUtils.isNotBlank(motorInsuranceModel.getAadharNumber())) {
			if (motorInsuranceModel.getAadharNumber().length() != 12) {
				errorCode.add("E-0222");
			} else if (!numericValidation.matcher(motorInsuranceModel.getAadharNumber()).find()) {
				errorCode.add("E-0221");
			}
		}
		
		  LOGGER.info("ContactCity ="+motorInsuranceModel.getContactCity());
		  if(StringUtils.isBlank(motorInsuranceModel.getContactCity())){
		  motorInsuranceModel.setStatusFlag("E-0376");
		  }/*else if(StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegisteredCity())){
				boolean status = twoWheelerDAO.getStateValueForCity(motorInsuranceModel.getVehicleRegisteredCity());
				if (!status) {
					errorCode.add("E-0062");
				}
			}*/
		 

		LOGGER.info("voluntarydeductible =" + motorInsuranceModel.getVoluntarydeductible());
		LOGGER.info("Premium =" + motorInsuranceModel.getPremium());

		return errorCode;

	}

	public List<String> validateUpdateVehicleDetailsService(MotorInsuranceModel motorInsuranceModel) throws Exception {
		List<String> errorCode = new ArrayList<>();
		errorCode = validateCalculatePremiumService(motorInsuranceModel);
		
		if (StringUtils.isBlank(motorInsuranceModel.getEngineNumber())) {
			errorCode.add("E-0102");
		} else if (!StringUtils.isAlphanumeric(motorInsuranceModel.getEngineNumber())) {
			errorCode.add("E-0111");
		} else if (!(motorInsuranceModel.getEngineNumber().length() >= 6
				&& motorInsuranceModel.getEngineNumber().length() <= 30)) {
			errorCode.add("E-0112");
		}
		
		/**
		 * Task #285932 Validation to block duplicate policy- 160301
		 */
		LOGGER.info("EngineNo::::" + motorInsuranceModel.getEngineNumber() + "ChassisNumber::::"
				+ motorInsuranceModel.getChassisNumber());
		if(!CoverFoxAgent.contains(motorInsuranceModel.getAgentId())) {
			if (twoWheelerDAO.getDPolicyVehicleDetails(motorInsuranceModel.getEngineNumber(),
					motorInsuranceModel.getChassisNumber(), motorInsuranceModel.getRegistrationNumber())) {
				errorCode.add("E-0349");
			}
		}
		
		if (StringUtils.isBlank(motorInsuranceModel.getChassisNumber())) {
			errorCode.add("E-0103");
		} else if (!StringUtils.isAlphanumeric(motorInsuranceModel.getChassisNumber())) {
			errorCode.add("E-0113");
		} else if (!(motorInsuranceModel.getChassisNumber().length() >= 6
				&& motorInsuranceModel.getChassisNumber().length() <= 30)) {
			errorCode.add("E-0114");
		}

		if (StringUtils.isBlank(motorInsuranceModel.getIsCarFinanced())) {
			errorCode.add("E-0104");
		} else if (YES.equalsIgnoreCase(motorInsuranceModel.getIsCarFinanced())) {

			if (StringUtils.isBlank(motorInsuranceModel.getIsCarFinancedValue())) {
				errorCode.add("E-0105");
			} else if (!"Hypothecation".equalsIgnoreCase(motorInsuranceModel.getIsCarFinancedValue())
					&& !"Hire purchase".equalsIgnoreCase(motorInsuranceModel.getIsCarFinancedValue())
					&& !"Lease".equalsIgnoreCase(motorInsuranceModel.getIsCarFinancedValue())) {
				errorCode.add("E-0105");
			}

			if (StringUtils.isBlank(motorInsuranceModel.getFinancierName())) {
				errorCode.add("E-0106");
			}// in parser class motorInsuranceModel.setFinancierName(formPost.get("financierName") +", "+formPost.get("financierlocation"));
			/* else if (!StringUtils.isAlphanumericSpace(motorInsuranceModel.getFinancierName())) {
				errorCode.add("E-0116");
			}*/ else if (!(motorInsuranceModel.getFinancierName().length() >= 1
					&& motorInsuranceModel.getFinancierName().length() <= 60)) {
				errorCode.add("E-0117");
			}
		}
		
		
		if (StringUtils.isBlank(motorInsuranceModel.getContactAddress1())) {
			errorCode.add("E-0124");
		} else if (!addressValidation.matcher(motorInsuranceModel.getContactAddress1()).find()) {
			errorCode.add("E-0126");
		} else if (!(motorInsuranceModel.getContactAddress1().length() >= 1
				&& motorInsuranceModel.getContactAddress1().length() <= 30)) {
			errorCode.add("E-0125");
		}

		if (StringUtils.isBlank(motorInsuranceModel.getContactAddress2())) {
			errorCode.add("E-0127");
		} else if (!addressValidation.matcher(motorInsuranceModel.getContactAddress1()).find()) {
			errorCode.add("E-0129");
		} else if (!(motorInsuranceModel.getContactAddress2().length() >= 1
				&& motorInsuranceModel.getContactAddress2().length() <= 30)) {
			errorCode.add("E-0128");
		}

		if (StringUtils.isNotBlank(motorInsuranceModel.getContactAddress3())) {
			if (!addressValidation.matcher(motorInsuranceModel.getContactAddress1()).find()) {
				errorCode.add("E-0131");
			} else if (!(motorInsuranceModel.getContactAddress3().length() >= 1
					&& motorInsuranceModel.getContactAddress3().length() <= 30)) {
				errorCode.add("E-0130");
			}
		}

		if (StringUtils.isNotBlank(motorInsuranceModel.getContactAddress4())) {
			if (!addressValidation.matcher(motorInsuranceModel.getContactAddress1()).find()) {
				errorCode.add("E-0133");
			} else if (!(motorInsuranceModel.getContactAddress4().length() >= 1
					&& motorInsuranceModel.getContactAddress4().length() <= 30)) {
				errorCode.add("E-0132");
			}
		}

		if (StringUtils.isBlank(motorInsuranceModel.getContactCity())) {
			errorCode.add("E-0376");
		} 
		/*if(StringUtils.isNotBlank(motorInsuranceModel.getVehicleRegisteredCity())){
			boolean status = twoWheelerDAO.getStateValueForCity(motorInsuranceModel.getVehicleRegisteredCity());
			if (!status) {
				errorCode.add("E-0062");
			}
		}*/
		LOGGER.info("Pincode =" + motorInsuranceModel.getContactPincode());
		if (StringUtils.isBlank(motorInsuranceModel.getContactPincode())) {
			errorCode.add("E-0134");
		} else if (!StringUtils.isNumeric(motorInsuranceModel.getContactPincode())) {
			errorCode.add("E-0135");
		} else if (motorInsuranceModel.getContactPincode().length() != 6) {
			errorCode.add("E-0136");
		}

		LOGGER.info("SameASRegistrationAddress =" + motorInsuranceModel.getIsSameASRegistrationAddress());
		if (StringUtils.isBlank(motorInsuranceModel.getIsSameASRegistrationAddress())) {
			errorCode.add("E-0137");
		} else if (!"Yes".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())
				&& !"No".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())) {
			errorCode.add("E-0138");
		}
		LOGGER.info("addressOne =" + motorInsuranceModel.getAddressOne());

		if (StringUtils.isBlank(motorInsuranceModel.getAddressOne())) {
			errorCode.add("E-0139");
		} else if (!addressValidation.matcher(motorInsuranceModel.getAddressOne()).find()) {
			errorCode.add("E-0141");
		} else if (!(motorInsuranceModel.getAddressOne().length() >= 1
				&& motorInsuranceModel.getAddressOne().length() <= 30)) {
			errorCode.add("E-0140");
		} else if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())
				&& !motorInsuranceModel.getAddressOne().equalsIgnoreCase(motorInsuranceModel.getContactAddress1())) {
			errorCode.add("E-0152");
		}

		LOGGER.info("addressTwo =" + motorInsuranceModel.getAddressTwo());
		if (StringUtils.isBlank(motorInsuranceModel.getAddressTwo())) {
			errorCode.add("E-0142");

		} else if (!addressValidation.matcher(motorInsuranceModel.getAddressTwo()).find()) {
			errorCode.add("E-0144");
		} else if (!(motorInsuranceModel.getAddressTwo().length() >= 1
				&& motorInsuranceModel.getAddressTwo().length() <= 30)) {
			errorCode.add("E-0143");

		} else if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())
				&& !motorInsuranceModel.getAddressTwo().equalsIgnoreCase(motorInsuranceModel.getContactAddress2())) {
			errorCode.add("E-0153");

		}

		if (StringUtils.isNotBlank(motorInsuranceModel.getAddressThree())) {
			if (!addressValidation.matcher(motorInsuranceModel.getAddressThree()).find()) {
				errorCode.add("E-0146");

			} else if (!(motorInsuranceModel.getAddressThree().length() >= 1
					&& motorInsuranceModel.getAddressThree().length() <= 30)) {
				errorCode.add("E-0145");

			} else if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())
					&& !motorInsuranceModel.getAddressThree()
							.equalsIgnoreCase(motorInsuranceModel.getContactAddress3())) {
				errorCode.add("E-0154");

			}
		}

		if (StringUtils.isNotBlank(motorInsuranceModel.getAddressFour())) {
			if (!addressValidation.matcher(motorInsuranceModel.getAddressFour()).find()) {
				errorCode.add("E-0148");

			} else if (!(motorInsuranceModel.getAddressFour().length() >= 1
					&& motorInsuranceModel.getAddressFour().length() <= 30)) {
				errorCode.add("E-0147");

			} else if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())
					&& !motorInsuranceModel.getAddressFour()
							.equalsIgnoreCase(motorInsuranceModel.getContactAddress4())) {
				errorCode.add("E-0155");

			}
		}

		if (StringUtils.isBlank(motorInsuranceModel.getRegCity())) {
			errorCode.add("E-0007");

		} else if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())
				&& !motorInsuranceModel.getRegCity().equalsIgnoreCase(motorInsuranceModel.getContactCity())) {
			errorCode.add("E-0156");

		} else {
			/*
			 * GetStateForCity getStateForCity =
			 * commonLookupDAO.getStateValueForCity(motorInsuranceModel.
			 * getContactCity()); if
			 * (StringUtils.isBlank(getStateForCity.getStateName())) {
			 * errorCode.add("E-0377");
			 * 
			 * }
			 */
		}
		LOGGER.info("Pincode =" + motorInsuranceModel.getRegPinCode());
		if (StringUtils.isBlank(motorInsuranceModel.getRegPinCode())) {
			errorCode.add("E-0149");

		} else if (!StringUtils.isNumeric(motorInsuranceModel.getRegPinCode())) {
			errorCode.add("E-0150");

		} else if (motorInsuranceModel.getRegPinCode().length() != 6) {
			errorCode.add("E-0151");

		} else if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsSameASRegistrationAddress())
				&& !motorInsuranceModel.getRegPinCode().equalsIgnoreCase(motorInsuranceModel.getContactPincode())) {
			errorCode.add("E-0157");

		}
		
		validateNomineeDetails(errorCode, motorInsuranceModel);
		
		//vahan flow block
		LOGGER.info("EngineNo::::"+motorInsuranceModel.getVahanResponse());
		if (motorInsuranceModel.getVahanResponse()!=null && motorInsuranceModel.getVahanResponse().equalsIgnoreCase("BlockFlow") ) {
			errorCode.add("E-5555");
		}
		

		return errorCode;

	}
	
	/**
	 * @author	roshini
	 * @since	2018-02-25
	 * @param	errorCode
	 * @param	motorInsuranceModel
	 * <p>
	 * Nominee Details are mandatory Only the <code>vehicleRegisteredInTheNameOf</code> of 'Individual' and <code>cpaCoverisRequired</code> 'YES'
	 * 
	 */
	private void validateNomineeDetails(List<String> errorCode, MotorInsuranceModel motorInsuranceModel) {
		if(INDIVIDUAL.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf()) && YES.equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())){
			if (StringUtils.isBlank(motorInsuranceModel.getNominee_Name())) {
				errorCode.add("E-0118");
			} else if (!(motorInsuranceModel.getNominee_Name().length() >= 1
					&& motorInsuranceModel.getNominee_Name().length() <= 30)) {
				errorCode.add("E-0119");
			} else if (!stringValueWithSpace.matcher(motorInsuranceModel.getNominee_Name()).find()) {
				errorCode.add("E-0120");
			}
			if (StringUtils.isBlank(motorInsuranceModel.getNominee_Age())) {
				errorCode.add("E-0121");
			} else if (StringUtils.isNotBlank(motorInsuranceModel.getNominee_Age())) {
				int nominee_age = Integer.parseInt(motorInsuranceModel.getNominee_Age());
				if (nominee_age >= 18) {
					if (StringUtils.isNotBlank(motorInsuranceModel.getGuardian_Name())
							|| StringUtils.isNotBlank(motorInsuranceModel.getGuardian_Age())
							|| StringUtils.isNotBlank(motorInsuranceModel.getRelationship_with_Guardian())) {
						LOGGER.info("guardian details not required::: " + nominee_age);
						errorCode.add("E-0015");
	
					}
	
				} else {
					if (StringUtils.isBlank(motorInsuranceModel.getGuardian_Name())
							|| StringUtils.isBlank(motorInsuranceModel.getGuardian_Age())
							|| StringUtils.isBlank(motorInsuranceModel.getRelationship_with_Guardian())) {
						LOGGER.info("guardian details required::: " + nominee_age);
						errorCode.add("E-0016");
					}else if(StringUtils.isNotBlank(motorInsuranceModel.getGuardian_Age()) && Integer.parseInt(motorInsuranceModel.getGuardian_Age()) < 18){
						errorCode.add("E-0426");
					} else if (StringUtils.isNotBlank(motorInsuranceModel.getRelationship_with_Guardian()) 
							&& !containsCaseInsensitive(motorInsuranceModel.getRelationship_with_Guardian(),Constants.nomineeRelationshipPossibleValues)) {
							errorCode.add("E-0463");
					}

				}
	
			}
			
			if (!containsCaseInsensitive(motorInsuranceModel.getRelationship_with_nominee(),Constants.nomineeRelationshipPossibleValues)) {
				errorCode.add("E-0463");
			}
			
			
//			if (StringUtils.isBlank(motorInsuranceModel.getRelationship_with_nominee())) {
//				errorCode.add("E-0122");
//			} else if (!"Spouse".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Father".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Mother".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Son".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Daughter".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Brother".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Sister".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Grand Son".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Grand Daughter".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Grand Father".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Grand Mother".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Father-in-Law".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Mother-in-Law".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Son-in-Law".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Daughter-in-Law".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Brother-in-Law".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Sister-in-Law".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Uncle".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Aunt".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Cousin".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Nephew".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Niece".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())
//					&& !"Legal Guardian".equalsIgnoreCase(motorInsuranceModel.getRelationship_with_nominee())) {
//				errorCode.add("E-0123");
//			}
		}

		
		
	}
	public boolean containsCaseInsensitive(String s, List<String> l){
        return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
    }
	/* End */
}
