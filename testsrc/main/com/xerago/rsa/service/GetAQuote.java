/**
 * 
 */
package com.xerago.rsa.service;



import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import com.valuemomentum.plm.parser.Product;
import com.valuemomentum.plm.parser.TwoWheelerQuoteParser;
import com.xerago.rsa.domain.cassandra.PPreQuoteDetails;
import com.xerago.rsa.dao.TwoWheelerDAO;
import com.xerago.rsa.dto.response.Liability;
import com.xerago.rsa.dto.response.OdPremium;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.dto.response.PremiumDetailsData;
import com.xerago.rsa.dto.response.Status;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.model.PreQuoteDetails;
import com.xerago.rsa.model.TppPricingValuesModel;
import com.xerago.rsa.util.Constants;
import com.xerago.rsa.util.MotorUtils;

/**
 * @author pandiaraj
 *
 */
@Service
public class GetAQuote {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	@Autowired
	TwoWheelerDAO twoWheelerDAO;
	
	@Autowired
	CassandraOperations cassandraOperations; 
	
	@Autowired
	TwoWheelerQuoteParser twoWheelerQuoteParser;
	
	private static final String BUSINESS_STATUS_DRAFT = "draft";

	private static final String BUSINESS_STATUS_QUOTE = "quote";
	
	public PremiumDetails getQuote(MotorInsuranceModel motorInsuranceModel) throws Exception {
		
		PremiumDetails premiumDetails = new PremiumDetails();
		Status status =new Status();
		List<PremiumDetailsData> PremiumDetailsDateList = new LinkedList<>();
		
		List<PreQuoteDetails> listPreQuoteDetails = new ArrayList<PreQuoteDetails>(); 
		String planName = null;
		int liabilityYear = 1;
		int standaloneYear = 1;
		try {
			
			for(int i = 1 ; i <= 11; i++) {
				
				try {
					
					if("BrandnewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
						
						if(i == 1){
							planName="LiabilityOnly";
							motorInsuranceModel.setPolicyTerm(0);
							motorInsuranceModel.setLiabilityPolicyTerm(5);
						}else if(i==2){
							planName="Comprehensive";
							motorInsuranceModel.setPolicyTerm(1);
							motorInsuranceModel.setLiabilityPolicyTerm(5);
						}else if(i==3){
							planName="Comprehensive";
							motorInsuranceModel.setPolicyTerm(5);
							motorInsuranceModel.setLiabilityPolicyTerm(5);
						}else if(i >= 4) break;
			       }else {
			  				motorInsuranceModel.setPolicyTerm(i);
			  				motorInsuranceModel.setLiabilityPolicyTerm(i);
			  				motorInsuranceModel.setCpaPolicyTerm(i);
			  				planName = "Comprehensive";
			  			
			  			if(i >= 4 && i <= 6){
			  				LOGGER.info("liabilityYear - "+liabilityYear);
				  			motorInsuranceModel.setPolicyTerm(0);
					  		motorInsuranceModel.setLiabilityPolicyTerm(liabilityYear);
					  		motorInsuranceModel.setCpaPolicyTerm(liabilityYear);
					  		planName = "LiabilityOnly";
					  		liabilityYear++;
			  			} else if ( i >= 7 ){
			  				LOGGER.info("standaloneYear = "+standaloneYear);
			  				motorInsuranceModel.setPolicyTerm(standaloneYear);
					  		motorInsuranceModel.setLiabilityPolicyTerm(0);
					  		motorInsuranceModel.setCpaPolicyTerm(0);
					  		planName = "StandAlone";
					  		standaloneYear++;
			  			}
						
			       }
					
			motorInsuranceModel.setiFoundryAgentCodeAttrribute(twoWheelerDAO.getIFoundryAgentCodeAttribute(motorInsuranceModel.getAgentId()));
			Product products = twoWheelerDAO.loadMandatoryCoverages(planName, motorInsuranceModel);
			if("BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName()))
				Ifoundry.setSelectedLimitValues(products, motorInsuranceModel, "getAQuote");
			else
				Ifoundry.setSelectedLimitValues(products, motorInsuranceModel);
			
			Ifoundry.setlstCoverages(products, motorInsuranceModel);
			motorInsuranceModel.setProduct(products);

			String productName = products.getProductName();
					/**
					 * Technical Discount calculation
					 */
					double technicalDiscount = twoWheelerDAO.getTechnicalDiscount(motorInsuranceModel, productName);
					motorInsuranceModel.setTechnicalDiscount(technicalDiscount);
					
					/**
					 * For first year bulk discount and nil intermediation discount not applicable
					 */
					if(i!=1) twoWheelerDAO.getBulkRateNilIntermediatonRate(motorInsuranceModel);
					
					Product temp = twoWheelerQuoteParser.invokeRating(products, motorInsuranceModel);
					
						 
					PremiumDetailsDateList = setPremiumBreakupsDetails(motorInsuranceModel,temp, PremiumDetailsDateList);
						 
					String QuoteId = motorInsuranceModel.getQuoteId();
					String lnQuoteSequence = "";
					if (StringUtils.isBlank(QuoteId)) {
						motorInsuranceModel.setPlanName("TWOWHEELER");
						QuoteId = twoWheelerDAO.getQuoteSequence(motorInsuranceModel.getAgentId(), motorInsuranceModel.getPlanName());
						motorInsuranceModel.setPlanName(Constants.PLAN_MOTOR_SHIELD_TWOWHEELER);
						motorInsuranceModel.setQuoteId(QuoteId);
						lnQuoteSequence = QuoteId;
						
					} else {
						lnQuoteSequence = QuoteId;
					}
					LOGGER.info("QuoteId-getquote;" + lnQuoteSequence);
					motorInsuranceModel.setQuoteId(lnQuoteSequence);
					if (temp.getPremium() != 0) {
						Map<String, String> hmCoverages = MotorUtils.getCoveragesMap(temp);
						double servicetax = MotorUtils.getValue1(hmCoverages, "ServiceeTax");
						double cessKishiKalyan = MotorUtils.getValue1(hmCoverages, "EducationCess");
						cessKishiKalyan += MotorUtils.getValue1(hmCoverages, "KrishiKalyan");
						
						PreQuoteDetails tempPreQuoteDetails = new PreQuoteDetails();
						tempPreQuoteDetails.setGrossPremium(temp.getPremium());
						tempPreQuoteDetails.setNetPremium(temp.getPremium() - (servicetax + cessKishiKalyan));
						tempPreQuoteDetails.setPremiumWithoutCovers(temp.getPremium() - (servicetax + cessKishiKalyan));
						tempPreQuoteDetails.setServiceTax(servicetax + cessKishiKalyan);
						tempPreQuoteDetails.setQuoteID(motorInsuranceModel.getQuoteId());
						tempPreQuoteDetails.setUserID(motorInsuranceModel.getUserId());
						listPreQuoteDetails.add(tempPreQuoteDetails);
						LOGGER.info("Reponse saved for policy term of " + i + " Year");
					} else {
						throw new RuntimeException("Unable to get the premium for policy term :: " + i);
					}
					
				} catch (RemoteException ex) {
				    if(ex instanceof AxisFault){
				    	LOGGER.info("Axis Fault error: " + ((AxisFault)ex).getFaultAction());
				    }
				} catch (Exception e) {
					LOGGER.info(e.getMessage(), e);
					throw e;
				} finally {
					motorInsuranceModel.setPolicyTerm(3);
				}
			}
			
			if (StringUtils.isBlank(motorInsuranceModel.getQuoteId())) {
				motorInsuranceModel.setPlanName("TWOWHEELER");
				motorInsuranceModel.setQuoteId(twoWheelerDAO.getQuoteSequence(motorInsuranceModel.getAgentId(),
						motorInsuranceModel.getPlanName()));
				motorInsuranceModel.setPlanName(Constants.PLAN_MOTOR_SHIELD_TWOWHEELER);
			}
			
			if (!listPreQuoteDetails.isEmpty() && (listPreQuoteDetails.size() >= 3) ) {
				
				//Saving getquote details into cassandra
				PPreQuoteDetails pPreQuoteDetails = new PPreQuoteDetails();
				pPreQuoteDetails.setQuoteId(motorInsuranceModel.getQuoteId());
				pPreQuoteDetails.setPremium(listPreQuoteDetails.get(0).getGrossPremium() + "");
				pPreQuoteDetails.setEmail(motorInsuranceModel.getStrEmail());
				pPreQuoteDetails.setMobile(motorInsuranceModel.getMobile());
				pPreQuoteDetails.setPreQuoteTime(new SimpleDateFormat(Constants.FIFTH_DATE_FORMAT).format(new Date()));
				pPreQuoteDetails.setProductName(Constants.PLAN_MOTOR_SHIELD_TWOWHEELER);
				pPreQuoteDetails.setSearchEngine(motorInsuranceModel.getSearchEngine());
				pPreQuoteDetails.setCampaign(motorInsuranceModel.getCampaign());
				pPreQuoteDetails.setAdgroup(motorInsuranceModel.getAdGroup());
				pPreQuoteDetails.setSearchVsContent(motorInsuranceModel.getSearchVsContent());
				pPreQuoteDetails.setKeyword(motorInsuranceModel.getKeyword());
				pPreQuoteDetails.setReferralUrl(motorInsuranceModel.getReferralUrl());
				pPreQuoteDetails.setProductName(motorInsuranceModel.getProductName());
				pPreQuoteDetails.setCampaignName(motorInsuranceModel.getCampaignName());
				pPreQuoteDetails.setLeadType(motorInsuranceModel.getLeadType());
				pPreQuoteDetails.setGclid(motorInsuranceModel.getGclid());
				pPreQuoteDetails.setVehiclemostlydriven(motorInsuranceModel.getVehicleMostlyDrivenOn());
				pPreQuoteDetails.setYom(motorInsuranceModel.getYearOfManufacture() + "");
				pPreQuoteDetails.setCity(motorInsuranceModel.getVehicleRegisteredCity());
				pPreQuoteDetails.setManufactureName(motorInsuranceModel.getVehicleManufacturerName());
				pPreQuoteDetails.setIsclaimmadeinpreviouspolicy(motorInsuranceModel.getClaimsMadeInPreviousPolicy());
				pPreQuoteDetails.setNoclaimbonusincurrentpolicy(MotorUtils.getncbExpiringpolicy(motorInsuranceModel.getNoClaimBonusPercent())  );
				pPreQuoteDetails.setNoclaimbonusinexpiryingpolicy(motorInsuranceModel.getNoClaimBonusPercentinCurrent());
				pPreQuoteDetails.setUserId(motorInsuranceModel.getUserId());
//				pPreQuoteDetails.setDateofBirth(new SimpleDateFormat(Constants.DATE_DEFAULT).parse(motorInsuranceModel.getDateOfBirth()));
				pPreQuoteDetails.setPremium2Year(new BigDecimal(listPreQuoteDetails.get(1).getGrossPremium()));
				pPreQuoteDetails.setPremium3Year(new BigDecimal(listPreQuoteDetails.get(2).getGrossPremium()));
				if("RolloverTwowheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
					pPreQuoteDetails.setLiabilityPremium(new BigDecimal(listPreQuoteDetails.get(3).getGrossPremium()));
					pPreQuoteDetails.setLiabilityPremium2ndyear(BigDecimal.valueOf(listPreQuoteDetails.get(4).getGrossPremium()));
					pPreQuoteDetails.setLiabilityPremium3rdyear(BigDecimal.valueOf(listPreQuoteDetails.get(5).getGrossPremium()));
					pPreQuoteDetails.setStandalonePremium1Year(BigDecimal.valueOf(listPreQuoteDetails.get(6).getGrossPremium()));
					pPreQuoteDetails.setStandalonePremium2Year(BigDecimal.valueOf(listPreQuoteDetails.get(7).getGrossPremium()));
					pPreQuoteDetails.setStandalonePremium3Year(BigDecimal.valueOf(listPreQuoteDetails.get(8).getGrossPremium()));
					pPreQuoteDetails.setStandalonePremium4Year(BigDecimal.valueOf(listPreQuoteDetails.get(9).getGrossPremium()));
					pPreQuoteDetails.setStandalonePremium5Year(BigDecimal.valueOf(listPreQuoteDetails.get(10).getGrossPremium()));
				}
				
				PPreQuoteDetails newOne = cassandraOperations.insert(pPreQuoteDetails);
				LOGGER.info("newOne.getQuoteId() ::: " + newOne.getQuoteId());
				PremiumDetailsData premiumDetailsData = new PremiumDetailsData(); 
				premiumDetailsData.setGrossPremium(String.valueOf(listPreQuoteDetails.get(0).getGrossPremium()));
				premiumDetailsData.setNetPremium(String.valueOf(listPreQuoteDetails.get(0).getNetPremium()));
				premiumDetailsData.setPremiumWithoutCovers(String.valueOf(listPreQuoteDetails.get(0).getPremiumWithoutCovers()));
				premiumDetailsData.setQuoteId(listPreQuoteDetails.get(0).getQuoteID());
				premiumDetailsData.setUserId(listPreQuoteDetails.get(0).getUserID());
				if("BrandnewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
					premiumDetailsData.setLiabilityPremium(String.valueOf(listPreQuoteDetails.get(0).getGrossPremium()));
					premiumDetailsData.setBundlePremium(String.valueOf(listPreQuoteDetails.get(1).getGrossPremium()));
					premiumDetailsData.setComprehensivePremium(String.valueOf(listPreQuoteDetails.get(2).getGrossPremium()));
				}else{
					premiumDetailsData.setSecondYearPremium(String.valueOf(listPreQuoteDetails.get(1).getGrossPremium()));
					premiumDetailsData.setThirdYearPremium(String.valueOf(listPreQuoteDetails.get(2).getGrossPremium()));
					premiumDetailsData.setLiabilityPremium(String.valueOf(listPreQuoteDetails.get(3).getGrossPremium()));
					premiumDetailsData.setLiabilityPremiumFor2ndYear(String.valueOf(listPreQuoteDetails.get(4).getGrossPremium()));
					premiumDetailsData.setLiabilityPremiumFor3rdYear(String.valueOf(listPreQuoteDetails.get(5).getGrossPremium()));
					
					premiumDetails.setPremiumForOneYear(PremiumDetailsDateList.get(0));
					premiumDetails.setPremiumForTwoYears(PremiumDetailsDateList.get(1));
					premiumDetails.setPremiumForThreeYears(PremiumDetailsDateList.get(2));
					premiumDetails.setPremiumForLiabilityOnlyForOneYear(PremiumDetailsDateList.get(3));
					premiumDetails.setPremiumForLiabilityOnlyForTwoYears(PremiumDetailsDateList.get(4));
					premiumDetails.setPremiumForLiabilityOnlyForThreeYears(PremiumDetailsDateList.get(5));
					premiumDetails.setPremiumForStandAloneForOneYear(PremiumDetailsDateList.get(6));
					premiumDetails.setPremiumForStandAloneForTwoYears(PremiumDetailsDateList.get(7));
					premiumDetails.setPremiumForStandAloneForThreeYears(PremiumDetailsDateList.get(8));
					premiumDetails.setPremiumForStandAloneForFourYears(PremiumDetailsDateList.get(9));
					premiumDetails.setPremiumForStandAloneForFiveYears(PremiumDetailsDateList.get(10));
				}
				premiumDetails.setData(premiumDetailsData);
				status.setStatusCode("S-0002");
				status.setMessage("Premium Calculated Successfully");
				premiumDetails.setStatus(status);
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			status.setStatusCode("S-0002");
			status.setMessage("Premium Calculated Successfully");
		}
		return premiumDetails;
	}
	
	
	
	private List<PremiumDetailsData> setPremiumBreakupsDetails(MotorInsuranceModel motorInsuranceModel, Product product, List<PremiumDetailsData> PremiumDetailsDateList) {
		
		PremiumDetailsData premiumDetailsData = new PremiumDetailsData();
		
		Map<String, String> hmCoverages1 = MotorUtils.getCoveragesMap(product);
		double od_premium = 0;
		od_premium += MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover");
		od_premium += MotorUtils.getValue1(hmCoverages1, "VMC_ElecAccessoriesCover");
		od_premium += MotorUtils.getValue1(hmCoverages1, "VPC_FiberGlass");
		od_premium += MotorUtils.getValue1(hmCoverages1, "GeoExtension");
		od_premium += MotorUtils.getValue1(hmCoverages1, "ImportedVehicleLoading");
		od_premium += MotorUtils.getValue1(hmCoverages1, "DriverTutionExtension");
		od_premium += MotorUtils.getValue1(hmCoverages1, "AdditionalTowingChargesCover");

		od_premium -= MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_AntiTheftDiscount");
		od_premium -= MotorUtils.getValue1(hmCoverages1,
				"VMC_ODCommercialCover_pricingelement_AutoAssociationMembership");
		od_premium -= MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_VoluntaryDed");
		od_premium -= MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_NoCliamDiscount");
		od_premium -= MotorUtils.getValue1(hmCoverages1, "DiscountforHandicapped");

		double liability_premium = 0;
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_LiabilityCover");
		liability_premium -= MotorUtils.getValue1(hmCoverages1, "TPPDStatutoryDiscount");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_PAOwnerDriverCover");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_PAUnnamed");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_PAPaidDriver");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_LLPaidDriverCover");
		liability_premium += MotorUtils.getValue1(hmCoverages1, "VMC_LLEMPLOYEES");
		double totalpremium = liability_premium + od_premium;

		Map<String, String> premiumMap = new HashMap();

		premiumMap.put("Premium", String.valueOf(motorInsuranceModel.getPremium()));
		premiumMap.put("VMC_ODBasicCover", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover")));
		premiumMap.put("VMC_ElecAccessoriesCover",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ElecAccessoriesCover")));
		premiumMap.put("VMC_ODCommercialCover_pricingelement_AntiTheftDiscount", Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_AntiTheftDiscount")));
		premiumMap.put("VMC_ODCommercialCover_pricingelement_AutoAssociationMembership", Double.toString(
				MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_AutoAssociationMembership")));
		premiumMap.put("VMC_ODBasicCover_VMC_VolDeductible",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODBasicCover_deductible_VoluntaryDeductible")));
		premiumMap.put("VMC_ODCommercialCover_pricingelement_VoluntaryDed", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_VoluntaryDed")));
		premiumMap.put("VMC_ODCommercialCover_pricingelement_NoCliamDiscount", Double
				.toString(MotorUtils.getValue1(hmCoverages1, "VMC_ODCommercialCover_pricingelement_NoCliamDiscount")));
		premiumMap.put("od_premium", String.valueOf(od_premium));
		
		premiumMap.put("VMC_LiabilityCover", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_LiabilityCover")));
		premiumMap.put("VMC_PAOwnerDriverCover",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_PAOwnerDriverCover")));
		premiumMap.put("VMC_PAUnnamed", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_PAUnnamed")));
		premiumMap.put("VMC_PAPaidDriver", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_PAPaidDriver")));
		premiumMap.put("VMC_LLPaidDriverCover",
				Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_LLPaidDriverCover")));
		LOGGER.info(MotorUtils.getValue1(hmCoverages1, "VMC_LLEMPLOYEES") + "***************************");
		premiumMap.put("VMC_LLEMPLOYEES", Double.toString(MotorUtils.getValue1(hmCoverages1, "VMC_LLEMPLOYEES")));
		premiumMap.put("liability_premium", String.valueOf(liability_premium));
		premiumMap.put("liability_premium_od_premium", String.valueOf(totalpremium));
		premiumMap.put("ServiceeTax", Double.toString(MotorUtils.getValue1(hmCoverages1, "ServiceeTax")));
		premiumMap.put("EducationCess", Double.toString(MotorUtils.getValue1(hmCoverages1, "EducationCess")));
		premiumMap.put("KrishiKalyan", Double.toString(MotorUtils.getValue1(hmCoverages1, "KrishiKalyan")));
		
		motorInsuranceModel.setPremiumBreakUpMap(premiumMap);
		
		String strQuoteDate = motorInsuranceModel.getPolicyStartDate();
		String strExpiryDt = motorInsuranceModel.getPolicyExpiryDate();
		
		LOGGER.info("total == "+totalpremium);
		LOGGER.info("motorInsuranceModel.getPolicyTerm()  == "+motorInsuranceModel.getPolicyTerm() );
		LOGGER.info("start == "+strQuoteDate);
		LOGGER.info("End :: "+strExpiryDt);
		premiumDetailsData.setGrossPremium(String.valueOf(product.getPremium()));
		premiumDetailsData.setOdPremium(setODPremiumDetails(motorInsuranceModel,premiumDetailsData));
		premiumDetailsData.setLiability(setLiabilityPremiumDetails(motorInsuranceModel, premiumDetailsData));
		premiumDetailsData.setPolicyStartDate(strQuoteDate);
		premiumDetailsData.setPolicyExpiryDate(strExpiryDt);
		Map<String, String> hmCoverages = MotorUtils.getCoveragesMap(product);
		double servicetax = MotorUtils.getValue1(hmCoverages, "ServiceeTax");
		double cessKishiKalyan = MotorUtils.getValue1(hmCoverages, "EducationCess");
		cessKishiKalyan += MotorUtils.getValue1(hmCoverages, "KrishiKalyan");
		premiumDetailsData.setNetPremium(String.valueOf(product.getPremium() - (servicetax + cessKishiKalyan)));
		premiumDetailsData.setPremiumWithoutCovers(String.valueOf(product.getPremium() - (servicetax + cessKishiKalyan)));
		premiumDetailsData.setServiceTax(String.valueOf(servicetax + cessKishiKalyan));
		premiumDetailsData.setQuoteId(motorInsuranceModel.getQuoteId());
		premiumDetailsData.setTaxType(product.getTaxType());
		premiumDetailsData.setKrishiCess(String.valueOf(MotorUtils.getValue1(hmCoverages, "KrishiKalyan")));
		premiumDetailsData.setEcess(String.valueOf(MotorUtils.getValue(hmCoverages, "EducationCess")));
		premiumDetailsData.setIgst(String.valueOf(MotorUtils.getValue(hmCoverages, "ServiceeTax_pricingelement_IGST")));
		premiumDetailsData.setCgst(String.valueOf(MotorUtils.getValue(hmCoverages, "ServiceeTax_pricingelement_CGST")));
		premiumDetailsData.setUtgst(String.valueOf(MotorUtils.getValue(hmCoverages, "ServiceeTax_pricingelement_UTGST")));
		premiumDetailsData.setSgst(String.valueOf(MotorUtils.getValue(hmCoverages, "ServiceeTax_pricingelement_SGST")));
		premiumDetailsData
				.setPolicyTerm(motorInsuranceModel.getPolicyTerm() == 0 ? motorInsuranceModel.getLiabilityPolicyTerm()
						: motorInsuranceModel.getPolicyTerm());
		LOGGER.info("motorInsuranceModel.getModifiedIdvfor1Year() :: "+motorInsuranceModel.getModifiedIdvfor1Year());
		if(!"BrandNewTwoWheeler".equalsIgnoreCase(motorInsuranceModel.getProductName())){
			LOGGER.info("getIDV1 ::: "+motorInsuranceModel.getIdv());
			if(motorInsuranceModel.getPolicyTerm()==1){
				premiumDetailsData.setIdv(motorInsuranceModel.getIdv());
			}else if(motorInsuranceModel.getPolicyTerm()==2){
				premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor2Year());
			}else if(motorInsuranceModel.getPolicyTerm()==3){
				premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor3Year());
			}else if(motorInsuranceModel.getPolicyTerm()==4){
				premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor4Year());
			}else if(motorInsuranceModel.getPolicyTerm()==5){
				premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor5Year());
			}else {
				premiumDetailsData.setIdv(motorInsuranceModel.getTotalIdv()!=null ? Double.parseDouble(motorInsuranceModel.getTotalIdv()) :0);
			}
			
		} else {
			LOGGER.info("getIDV2 ::: "+motorInsuranceModel.getIdv());
			if(motorInsuranceModel.getPolicyTerm()==1){
				premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor1Year());
			}else if(motorInsuranceModel.getPolicyTerm()==2){
				premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor2Year());
			}else if(motorInsuranceModel.getPolicyTerm()==3){
				premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor3Year());
			}else if(motorInsuranceModel.getPolicyTerm()==4){
				premiumDetailsData.setIdv(motorInsuranceModel.getIdvFor4Year());
			}else if(motorInsuranceModel.getPolicyTerm()==5){
				premiumDetailsData.setIdv(motorInsuranceModel.getIdv());
			}else {
				premiumDetailsData.setIdv(motorInsuranceModel.getIdv());
			}
			LOGGER.info("getProductName::: "+premiumDetailsData.getIdv());
		}
		/*if(motorInsuranceModel.getPolicyTerm() == 1)
			premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor1Year());
		if(motorInsuranceModel.getPolicyTerm() == 2)
			premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor2Year());
		if(motorInsuranceModel.getPolicyTerm() == 3)
			premiumDetailsData.setIdv(motorInsuranceModel.getModifiedIdvfor3Year());
			*/
		PremiumDetailsDateList.add(premiumDetailsData);
		return PremiumDetailsDateList;
	}



	
	
	
	public Liability setLiabilityPremiumDetails(MotorInsuranceModel motorInsuranceModel,
			PremiumDetailsData premiumDetailsData) {
		Liability liability = new Liability();
		liability.setBasicPremiumIncludingPremiumForTppd(
				motorInsuranceModel.getPremiumBreakUpMap().get("VMC_LiabilityCover"));
		liability.setBiFuelKitCng("0.0");
		liability.setPersonalAccidentBenefits("-");
		liability.setUnderSectionIIIOwnerDriver(
				motorInsuranceModel.getPremiumBreakUpMap().get("VMC_PAOwnerDriverCover"));
		liability.setUnnamedPassengrs(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_PAUnnamed"));
		liability.setPaCoverToPaidDriver(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_PAPaidDriver"));
		liability.setToPaidDrivers(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_LLPaidDriverCover"));
		liability.setToEmployeses(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_LLEMPLOYEES"));
		liability.setTotalLiabilityPremium(motorInsuranceModel.getPremiumBreakUpMap().get("liability_premium"));
		premiumDetailsData.setLiability(liability);
		
		return liability;
	}

	
	public OdPremium setODPremiumDetails(MotorInsuranceModel motorInsuranceModel,
			PremiumDetailsData premiumDetailsData) {
		// Setting OD_Premium
				OdPremium odPremium = new OdPremium();
				odPremium.setBasicPremiumAndNonElectricalAccessories(
						motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODBasicCover"));
				odPremium.setElectricalAccessories(motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ElecAccessoriesCover"));
				odPremium.setBiFuelKit("0.0");
				odPremium.setFiberGlassTank("0.0");
				odPremium.setAutomobileAssociationDiscount(motorInsuranceModel.getPremiumBreakUpMap()
						.get("VMC_OwnDamageCover_pricingelement_AutoAssociationMembership"));
				odPremium.setVoluntaryDeductables(
						motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODBasicCover_deductible_VoluntaryDeductible"));
				odPremium.setVoluntaryDeductable(
						motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODCommercialCover_pricingelement_VoluntaryDed"));
				odPremium.setNoClaimBonus(
						motorInsuranceModel.getPremiumBreakUpMap().get("VMC_ODCommercialCover_pricingelement_NoCliamDiscount"));
				odPremium.setDepreciationWaiver("0.0");
				odPremium.setEngineProtector("0.0");
				odPremium.setNcbProtector("0.0");
				odPremium.setWindShieldGlass("0.0");
				odPremium.setLifeTimeRoadTax("0.0");
				odPremium.setSpareCar("0.0");
				odPremium.setInvoicePriceInsurance("0.0");
				odPremium.setLossOfBaggage("0.0");
				odPremium.setTotalOdPremium(motorInsuranceModel.getPremiumBreakUpMap().get("od_premium"));
				odPremium.setKeyReplacement("0.0");
				odPremium
						.setBulkDealDiscountCoverPremium(String.valueOf(motorInsuranceModel.getBulkDealDiscountCoverPremium()));
				odPremium.setNilIntermediationCoverPremium(
						String.valueOf(motorInsuranceModel.getNilIntermediationCoverPremium()));
				odPremium.setTowingChargesCoverPremium(
						String.valueOf(motorInsuranceModel.getAdditionalTowingChargesCoverPremium()));
				premiumDetailsData.setOdPremium(odPremium);
				
		
		return odPremium;
}

	
}
