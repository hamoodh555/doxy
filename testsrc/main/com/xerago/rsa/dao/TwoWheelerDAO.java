/**
 * 
 */
package com.xerago.rsa.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valuemomentum.plm.parser.Attributes;
import com.valuemomentum.plm.parser.Coverage;
import com.valuemomentum.plm.parser.Deductibles;
import com.valuemomentum.plm.parser.Limits;
import com.valuemomentum.plm.parser.Product;
import com.xerago.rsa.domain.AgentMaster;
import com.xerago.rsa.domain.AgentPlanMapping;
import com.xerago.rsa.domain.BrokerCodeMaster;
import com.xerago.rsa.domain.BulkDealDiscount;
import com.xerago.rsa.domain.DElectricalAccessoriesLimit;
import com.xerago.rsa.domain.DModifyidvRangeMaster;
import com.xerago.rsa.domain.DNomineeClientcodeSequence;
import com.xerago.rsa.domain.DNonElecAccessoriesLimit;
import com.xerago.rsa.domain.DOtpDetails;
import com.xerago.rsa.domain.DPolicyAttributes;
import com.xerago.rsa.domain.DPolicyCoverages;
import com.xerago.rsa.domain.DPolicyDetails;
import com.xerago.rsa.domain.DPolicyPricingElements;
import com.xerago.rsa.domain.DPolicyPurchasedDetails;
import com.xerago.rsa.domain.DPolicyVehicleDetails;
import com.xerago.rsa.domain.DPoscodeDetails;
import com.xerago.rsa.domain.DPoscodeMaster;
import com.xerago.rsa.domain.DQuoteDetails;
import com.xerago.rsa.domain.DTppPricingElements;
import com.xerago.rsa.domain.DUpdatedClientDetails;
import com.xerago.rsa.domain.GoodFeatureDiscount;
import com.xerago.rsa.domain.GstinStateMaster;
import com.xerago.rsa.domain.LongtermRateMaster;
import com.xerago.rsa.domain.ModelTppRatesMaster;
import com.xerago.rsa.domain.MotorTechnicalDiscount;
import com.xerago.rsa.domain.NillIntermediationDiscount;
import com.xerago.rsa.domain.PolicyPrefixMaster;
import com.xerago.rsa.model.CityStateLookupModel;
import com.xerago.rsa.model.CoverageModel;
import com.xerago.rsa.model.DTpSurveyorUseDetails;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.model.Productattributes;
import com.xerago.rsa.model.Productcoveragemaster;
import com.xerago.rsa.model.VehicleClassification;
import com.xerago.rsa.util.CommonUtils;
import com.xerago.rsa.util.Constants;
import com.xerago.rsa.util.MotorUtils;
import com.xerago.rsa.util.PortalDateConversion;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
@Repository
public class TwoWheelerDAO {

	private static final Logger LOGGER = LogManager.getRootLogger();
	private static final List<String> TP_DIS_FUEL_TYPES = new ArrayList<>(Arrays.asList("ELECTRIC","BATTERY","POWER"));

	@Autowired
	SessionFactory sessionFactory;

	@Transactional(readOnly = true)
	public Product loadMandatoryCoverages(String typeOfCover, MotorInsuranceModel motorInsuranceModel)
			throws Exception {
		Map<String, Coverage> mapCoverages = new HashMap<String, Coverage>();
		Product product = new Product();
		Session session = sessionFactory.getCurrentSession();

		String strProductName = "LiabilityOnly".equalsIgnoreCase(typeOfCover) ? "MotorCycleLiabilityOnly"
				: "MotorCyclePackage";

		if (strProductName != null) {

			product.setProductName(strProductName);

			LOGGER.info("select * from CoverageModel where productname=:productName");
			Query query = null;
			if (motorInsuranceModel.getPolicyTerm() == 1) {
				query = session
						.createSQLQuery(
								"select * from productcoveragemaster where productname=:productName and isselected = :isSelected and coveragename not in ('NilIntermediationRateCover', 'BulkDealDiscountCover') order by iscomposite desc")
						.addScalar("id", new LongType()).addScalar("coveragename").addScalar("productname")
						.addScalar("limitname").addScalar("limittype").addScalar("defaultlimitvalue")
						.addScalar("deductiblename").addScalar("deductibletype").addScalar("defaultdeductiblevalue")
						.addScalar("coveragedescription").addScalar("limitdescription")
						.addScalar("deductibledescription").addScalar("ismandatory", new LongType())
						.addScalar("iscomposite", new LongType()).addScalar("parentcoverageid", new LongType())
						.addScalar("productid", new LongType()).addScalar("coveragedisplaytype", new LongType())
						.addScalar("isselected", new LongType())
						.setResultTransformer(Transformers.aliasToBean(Productcoveragemaster.class))
						.setParameter("productName", strProductName).setParameter("isSelected", "1");
			} else {
				query = session
						.createSQLQuery(
								"select * from productcoveragemaster where productname=:productName and isselected = :isSelected order by iscomposite desc")
						.addScalar("id", new LongType()).addScalar("coveragename").addScalar("productname")
						.addScalar("limitname").addScalar("limittype").addScalar("defaultlimitvalue")
						.addScalar("deductiblename").addScalar("deductibletype").addScalar("defaultdeductiblevalue")
						.addScalar("coveragedescription").addScalar("limitdescription")
						.addScalar("deductibledescription").addScalar("ismandatory", new LongType())
						.addScalar("iscomposite", new LongType()).addScalar("parentcoverageid", new LongType())
						.addScalar("productid", new LongType()).addScalar("coveragedisplaytype", new LongType())
						.addScalar("isselected", new LongType())
						.setResultTransformer(Transformers.aliasToBean(Productcoveragemaster.class))
						.setParameter("productName", strProductName).setParameter("isSelected", "1");
			}

			List<Productcoveragemaster> coveageModelList = query.list();
			if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase("StandAlone", typeOfCover)) {
				coveageModelList = getODCoversOnly(coveageModelList);
			}
			for (Productcoveragemaster coverageMaster : coveageModelList) {
				CoverageModel coverage = convertCoverageMasterToCoverageModel(coverageMaster);

				if (!canIncludeCover(coverage, motorInsuranceModel)) {
					continue;
				}

				Coverage objCoverage = new Coverage();
				LOGGER.info("coverage.getCoverageName() : " + coverage.getCoverageName());
				objCoverage.setCoverageName(coverage.getCoverageName());
				objCoverage.setComposite("1".equalsIgnoreCase(coverage.getIsComposite()) ? true : false);
				LOGGER.info("objCoverage.isComposite() : " + objCoverage.isComposite());
				LOGGER.info("coverage.getIsMandatory() : " + coverage.getIsMandatory());
				boolean isMandatory = ("1".equalsIgnoreCase(coverage.getIsMandatory())) ? true : false;
				objCoverage.setMandatory(isMandatory);
				objCoverage.setSelected((isMandatory) ? true : false);
				objCoverage.setParentcoverageid(Integer.parseInt(coverage.getParentCoverageId()));
				objCoverage.setCoverageDescription(coverage.getCoverageDescription());
				String strLimitName = coverage.getLimitName();

				LOGGER.info("coverage.getLimitName() : " + coverage.getLimitName());

				if (strLimitName != null) {
					Limits objLimits = new Limits();
					objLimits.setLimitName(strLimitName);
					objLimits.setLimitType(coverage.getLimitType());
					double value = Double.parseDouble(coverage.getDefaultLimitvalue());
					objLimits.setDefaultValue(value);
					objLimits.setSelectedLimitValue(value);
					objCoverage.getLimits().add(objLimits);

					if ("VMC_ODBasicCover".equalsIgnoreCase(coverage.getCoverageName())) {
						objLimits = new Limits();
						objLimits.setLimitName("ODLimitFor2ndYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 2 ? "Derived" : "Independent");
						objLimits.setSelectedLimitValue(
								motorInsuranceModel.getPolicyTerm() >= 2 ? motorInsuranceModel.getIdvFor2Year() : 0);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("ODLimitFor3rdYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 3 ? "Derived" : "Independent");
						objLimits.setSelectedLimitValue(
								motorInsuranceModel.getPolicyTerm() >= 3 ? motorInsuranceModel.getIdvFor3Year() : 0);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("ODLimitFor4thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 4 ? "Derived" : "Independent");
						objLimits.setSelectedLimitValue(
								motorInsuranceModel.getPolicyTerm() >= 4 ? motorInsuranceModel.getIdvFor4Year() : 0);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("ODLimitFor5thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 5 ? "Derived" : "Independent");
						objLimits.setSelectedLimitValue(
								motorInsuranceModel.getPolicyTerm() >= 5 ? motorInsuranceModel.getIdvFor5Year() : 0);
						objCoverage.getLimits().add(objLimits);
					}

					if ("VMC_NonElecAccessoriesCover".equalsIgnoreCase(coverage.getCoverageName())) {
						objLimits = new Limits();
						objLimits.setLimitName("NonElectricalLimitFor2ndYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 2 ? "User Input" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("NonElectricalLimitFor3rdYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 3 ? "User Input" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("NonElectricalLimitFor4thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 4 ? "User Input" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("NonElectricalLimitFor5thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 5 ? "User Input" : "Independent");
						objCoverage.getLimits().add(objLimits);
					}

					if ("VMC_ElecAccessoriesCover".equalsIgnoreCase(coverage.getCoverageName())) {
						objLimits = new Limits();
						objLimits.setLimitName("ElectricalAccessoriesLimitFor2ndYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 2 ? "User Input" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("ElectricalAccessoriesLimitFor3rdYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 3 ? "User Input" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("ElectricalAccessoriesLimitFor4thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 4 ? "User Input" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("ElectricalAccessoriesLimitFor5thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 5 ? "User Input" : "Independent");
						objCoverage.getLimits().add(objLimits);
					}

					if ("VMC_LiabilityCover".equalsIgnoreCase(coverage.getCoverageName())) {
						double value1 = 0.0d;
						double value2 = 0.0d;
						double value3 = 0.0d;
						double value4 = 0.0d;

						if (motorInsuranceModel.getLiabilityPolicyTerm() == 2) {
							value1 = value;
						}
						if (motorInsuranceModel.getLiabilityPolicyTerm() == 3) {
							value1 = value;
							value2 = value;
						}
						if (motorInsuranceModel.getLiabilityPolicyTerm() == 4) {
							value1 = value;
							value2 = value;
							value3 = value;
							value4 = value;
						}
						if (motorInsuranceModel.getLiabilityPolicyTerm() == 5) {
							value1 = value;
							value2 = value;
							value3 = value;
							value4 = value;
						}
						objLimits = new Limits();
						objLimits.setLimitName("LiabilityLimitFor2ndYear");
						objLimits.setSelectedLimitValue(value1);
						objLimits.setLimitType("Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("LiabilityLimitFor3rdYear");
						objLimits.setSelectedLimitValue(value2);
						objLimits.setLimitType("Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("LiabilityLimitFor4thYear");
						objLimits.setSelectedLimitValue(value3);
						objLimits.setLimitType("Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("LiabilityLimitFor5thYear");
						objLimits.setSelectedLimitValue(value4);
						objLimits.setLimitType("Independent");
						objCoverage.getLimits().add(objLimits);
					}

					if ("VMC_PAPaidDriver".equalsIgnoreCase(coverage.getCoverageName())) {
						double value2 = 0.0d;
						double value3 = 0.0d;
						double value4 = 0.0d;
						double value5 = 0.0d;

						if (motorInsuranceModel.getLiabilityPolicyTerm() == 2) {
							value2 = value;
						} else if (motorInsuranceModel.getLiabilityPolicyTerm() == 3) {
							value2 = value;
							value3 = value;
						} else if (motorInsuranceModel.getLiabilityPolicyTerm() == 4) {
							value2 = value;
							value3 = value;
							value4 = value;
						} else if (motorInsuranceModel.getLiabilityPolicyTerm() == 5) {
							value2 = value;
							value3 = value;
							value4 = value;
							value5 = value;
						}
						objLimits = new Limits();
						objLimits.setLimitName("PAToPaidDriverLimitFor2ndYear");
						objLimits.setLimitType("Independent");
						objLimits.setSelectedLimitValue(value2);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("PAToPaidDriverLimitFor3rdYear");
						objLimits.setLimitType("Independent");
						objLimits.setSelectedLimitValue(value3);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("PAToPaidDriverLimitFor4thYear");
						objLimits.setLimitType("Independent");
						objLimits.setSelectedLimitValue(value4);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("PAToPaidDriverLimitFor5thYear");
						objLimits.setLimitType("Independent");
						objLimits.setSelectedLimitValue(value5);
						objCoverage.getLimits().add(objLimits);
					}

					if ("VMC_PAUnnamed".equalsIgnoreCase(coverage.getCoverageName())) {

						double value2 = 0.0d;
						double value3 = 0.0d;
						double value4 = 0.0d;
						double value5 = 0.0d;

						if (motorInsuranceModel.getLiabilityPolicyTerm() == 2) {
							value2 = value;
						} else if (motorInsuranceModel.getLiabilityPolicyTerm() == 3) {
							value2 = value;
							value3 = value;
						} else if (motorInsuranceModel.getLiabilityPolicyTerm() == 4) {
							value2 = value;
							value3 = value;
							value4 = value;
						} else if (motorInsuranceModel.getLiabilityPolicyTerm() == 5) {
							value2 = value;
							value3 = value;
							value4 = value;
							value5 = value;

						}

						objLimits = new Limits();
						objLimits.setLimitName("PAtoUnnamedOccupantsLtFor2ndYear");
						objLimits.setLimitType(
								motorInsuranceModel.getLiabilityPolicyTerm() >= 2 ? "User Input" : "Independent");
						objLimits.setSelectedLimitValue(value2);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("PAtoUnnamedOccupantsLtFor3rdYear");
						objLimits.setLimitType(
								motorInsuranceModel.getLiabilityPolicyTerm() >= 3 ? "User Input" : "Independent");
						objLimits.setSelectedLimitValue(value3);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("PAtoUnnamedOccupantsLtFor4thYear");
						objLimits.setLimitType(
								motorInsuranceModel.getLiabilityPolicyTerm() >= 4 ? "User Input" : "Independent");
						objLimits.setSelectedLimitValue(value4);
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("PAtoUnnamedOccupantsLtFor5thYear");
						objLimits.setLimitType(
								motorInsuranceModel.getLiabilityPolicyTerm() >= 5 ? "User Input" : "Independent");
						objLimits.setSelectedLimitValue(value5);
						objCoverage.getLimits().add(objLimits);
					}

					if ("BulkDealDiscountCover".equalsIgnoreCase(coverage.getCoverageName())) {
						objLimits = new Limits();
						objLimits.setLimitName("BulkDealDiscountLimitFor2ndYear");
						objLimits.setLimitType("Derived");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("BulkDealDiscountLimitFor3rdYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 3 ? "Derived" : "Independent");
						objCoverage.getLimits().add(objLimits);
						objLimits = new Limits();
						objLimits.setLimitName("BulkDealDiscountLimitFor4thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 4 ? "Derived" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("BulkDealDiscountLimitFor5thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 5 ? "Derived" : "Independent");
						objCoverage.getLimits().add(objLimits);
					}

					if ("NilIntermediationRateCover".equalsIgnoreCase(coverage.getCoverageName())) {
						objLimits = new Limits();
						objLimits.setLimitName("NilIntermediationRateLimitFor2ndYear");
						objLimits.setLimitType("Derived");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("NilIntermediationRateLimitFor3rdYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 3 ? "Derived" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("NilIntermediationRateLimitFor4thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 4 ? "Derived" : "Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("NilIntermediationRateLimitFor5thYear");
						objLimits.setLimitType(motorInsuranceModel.getPolicyTerm() >= 5 ? "Derived" : "Independent");
						objCoverage.getLimits().add(objLimits);
					}

					/**
					 * Task #141300 Add-on cover additional Towing charges
					 */
					if ("AdditionalTowingChargesCover".equalsIgnoreCase(coverage.getCoverageName())) {
						objLimits = new Limits();
						objLimits.setLimitName("AdditionalTowingChargesLimitFor2ndYear");
						objLimits.setLimitType("Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("AdditionalTowingChargesLimitFor3rdYear");
						objLimits.setLimitType("Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("AdditionalTowingChargesLimitFor4thYear");
						objLimits.setLimitType("Independent");
						objCoverage.getLimits().add(objLimits);

						objLimits = new Limits();
						objLimits.setLimitName("AdditionalTowingChargesLimitFor5thYear");
						objLimits.setLimitType("Independent");
						objCoverage.getLimits().add(objLimits);
					}

					if ("VMC_PAOwnerDriverCover".equalsIgnoreCase(coverage.getCoverageName())) {
						/*
						 * <limit> <name>PAOwnerDriverCoverLimit</name>
						 * <sourceType>Independent</sourceType>
						 * <value>1500000.0</value> </limit>
						 */
						LOGGER.info(
								"motorInsuranceModel.getCpaSumInsured() = " + motorInsuranceModel.getCpaSumInsured());
						objLimits = new Limits();
						{
							objLimits.setLimitName("PAOwnerDriverCoverLimit");
							objLimits.setLimitType("Independent");
							// default to 15 lacs, in case validation slips up
							objLimits.setSelectedLimitValue(motorInsuranceModel.getCpaSumInsured() != null
									&& motorInsuranceModel.getCpaSumInsured() != 0
											? motorInsuranceModel.getCpaSumInsured() : 15_00_000);
						}
						objCoverage.setLimits(Arrays.asList(objLimits));
					}

				}

				String strDeductibleName = coverage.getDeductibleName();

				LOGGER.info("coverage.getDeductibleName() : " + coverage.getDeductibleName());
				LOGGER.info("coverage.getId() : " + coverage.getId());

				if (strDeductibleName != null) {
					Deductibles ded = new Deductibles();
					ded.setDeductibleName(strDeductibleName);
					ded.setDeductibleType(coverage.getDeductibleType());
					double value = Double.parseDouble(coverage.getDefaultDeductiblevalue());
					ded.setDefaultValue(value);
					ded.setSelectedDeductibleValue(value);
					objCoverage.getDeductibles().add(ded);
				}

				int coverageId = coverage.getId();
				LOGGER.info("Coverage ID Start :  " + coverageId);
				objCoverage.setCoverageid(coverageId);

				LOGGER.info("Parent Coverage ID :  " + objCoverage.getParentcoverageid());

				if (objCoverage.getParentcoverageid() > 0) {
					Coverage parentCoverage = null;
					try {
						parentCoverage = mapCoverages.get(String.valueOf(objCoverage.getParentcoverageid()));
						parentCoverage.getSubCoverages().add(objCoverage);
					} catch (Exception ex) {
						LOGGER.error(ex.getMessage(), ex);
						LOGGER.info("Error Here !");
					}
				} else {
					mapCoverages.put(String.valueOf(coverageId), objCoverage);
				}

			}

			List<Coverage> lstCoverages = new ArrayList<Coverage>();

			LOGGER.info("Loop Starting coverage ID 1 : " + mapCoverages.values());

			for (Coverage objcoverage : mapCoverages.values()) {
				setPricingElements(strProductName, objcoverage, "coverage");
				// To Retrieve Attributes
				objcoverage.setAttributes(setAttributes(strProductName, objcoverage, "coverage"));
				// Task #289448 IF REQUEST FOR TW CPA
				if ("VMC_PAOwnerDriverCover".equalsIgnoreCase(objcoverage.getCoverageName())) {
					for (Attributes attr : objcoverage.getAttributes()) {
						if ("PAOwnerDriverTerm".equalsIgnoreCase(attr.getAttributeName())) {
							attr.setSelecetdAttributeValue(String.valueOf(motorInsuranceModel.getCpaPolicyTerm()));
						}
					}
				}

				// To Retrieve Limit Values
				List<Limits> lstLimits = (List<Limits>) objcoverage.getLimits();
				LOGGER.info("lstLimits size : " + lstLimits.size());

				if (lstLimits != null && !lstLimits.isEmpty()) {
					Limits objLimits = lstLimits.get(0);
					if (objLimits != null) {
						List<Double> lstLimitValues = setLimits(strProductName, objcoverage.getCoverageid(),
								objLimits.getLimitName());
						if (lstLimitValues != null && !lstLimitValues.isEmpty()) {
							objLimits.setLimitValues(lstLimitValues);
						} else {
							lstLimitValues = new ArrayList<Double>();

							lstLimitValues.add(new Double(objLimits.getDefaultValue()));
							objLimits.setLimitValues(lstLimitValues);
						}
					}
				}

				List<Coverage> lstSubCoverages = (List<Coverage>) objcoverage.getSubCoverages();
				if (lstSubCoverages != null && !lstSubCoverages.isEmpty()) {
					for (Coverage objSubCov : lstSubCoverages) {
						// To Retrieve Limit Values
						List<Limits> lstSubLimits = objSubCov.getLimits();
						LOGGER.info("objSubCov.getCoverageName() : " + objSubCov.getCoverageName());
						LOGGER.info("lstSubLimits size : " + lstSubLimits.size());
						if (lstSubLimits != null && !lstSubLimits.isEmpty()) {
							Limits objLimits = lstSubLimits.get(0);
							if (objLimits != null) {
								List<Double> lstLimitValues = setLimits(strProductName, objSubCov.getCoverageid(),
										objLimits.getLimitName());
								if (lstLimitValues != null && !lstLimitValues.isEmpty()) {
									objLimits.setLimitValues(lstLimitValues);
								} else {
									lstLimitValues = new ArrayList<Double>();

									lstLimitValues.add(new Double(objLimits.getDefaultValue()));
									objLimits.setLimitValues(lstLimitValues);
								}
							}
						}
						// To Retrieve Pricing Elements
						setPricingElements(strProductName, objSubCov, "coverage");
						// To Retrieve Attributes
						objSubCov.setAttributes(setAttributes(strProductName, objSubCov, "coverage"));
					}
				}
				lstCoverages.add(objcoverage);
			}

			product.setCoverages(lstCoverages); // myself added this
			LOGGER.info("product.getCoverages() :  " + product.getCoverages().size());
		}

		// To Retrieve Package Level Pricing Elements
		Coverage cov = new Coverage();
		setPricingElements(strProductName, cov, Constants.PACKAGE_ATTRIBUTE);
		product.setPricingElements(cov.getPricingElements());

		// To Retrieve Package Level Attributes
		Coverage cov1 = new Coverage();
		product.setAttributes(setAttributes(strProductName, cov1, Constants.PACKAGE_ATTRIBUTE));
		return product;
	}

	@Transactional(readOnly = true)
	public String getAddonCoverDepreciation(double value, String businessStatus, String vehicleSubline)
			throws Exception {
		Session session = sessionFactory.getCurrentSession();
		String idvfrom = null;
		LOGGER.info("AddonCoverDepreciation value ::: " + value);
		LOGGER.info("businessStatus ::: " + businessStatus);
		LOGGER.info("vehicleSubline ::: " + vehicleSubline);

		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT :value - (:value*DEPRECIATION/100) FROM VEHICLE_DEPRECIATION WHERE upper(BUSINESS_STATUS) = upper(:businessStatus) ");
		sb.append("AND upper(VEHICLESUBLINE) = upper(:vehicleSubline)");

		SQLQuery query = session.createSQLQuery(sb.toString());
		query.setParameter("value", value);
		query.setParameter("businessStatus", businessStatus);
		query.setParameter("vehicleSubline", vehicleSubline);
		List list = query.list();
		if (list != null && !list.isEmpty()) {
			LOGGER.info("list.size() ::: " + list.size());
			LOGGER.info("idvfrom ::: " + (BigDecimal) list.get(0));
			BigDecimal b = ((BigDecimal) list.get(0)).setScale(0, BigDecimal.ROUND_HALF_UP);
			idvfrom = b.toPlainString();
			LOGGER.info("idvfrom ::: " + idvfrom);
		} else {
			throw new IllegalArgumentException();
		}
		return idvfrom;
	}

	@Transactional(readOnly = true)
	public synchronized String getQuoteSequence(String AgentId, String PlanName) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder strQuery = new StringBuilder();
		List DBQuoteSequence;
		String QSequence = "";
		String Qprefix = "";
		int prefix = 0;
		String numnerS = "";
		StringBuilder dbQuoteSequence = null;

		if ("CITIBANK".equalsIgnoreCase(AgentId)) {
			AgentId = "CITI";
		}

		LOGGER.info("session " + session);
		Query query = session
				.createSQLQuery(
						"select * FROM POLICY_PREFIX_MASTER where AGENT_ID =:agentId and PRODUCT_NAME =:productName")
				.addEntity(PolicyPrefixMaster.class).setParameter("agentId", AgentId)
				.setParameter("productName", PlanName);

		List<PolicyPrefixMaster> qList = query.list();
		Iterator itr = qList.iterator();
		while (itr.hasNext()) {
			PolicyPrefixMaster m = (PolicyPrefixMaster) itr.next();
			QSequence = m.getQuoteSequence();
			Qprefix = m.getQuotePrefix();
		}
		strQuery.append(" select " + QSequence + " from dual ");
		Query query1 = session.createSQLQuery(strQuery.toString());
		DBQuoteSequence = query1.list();
		numnerS = DBQuoteSequence.get(0).toString();
		prefix = 7 - numnerS.length();
		String pString = "";
		while (prefix > 0) {
			pString = "0" + pString;
			--prefix;
		}
		dbQuoteSequence = new StringBuilder();
		dbQuoteSequence.append(Qprefix);
		dbQuoteSequence.append(pString + numnerS);
		return dbQuoteSequence.toString();

	}

	@Transactional(readOnly = true)
	public synchronized String getQuoteSequence(String agentId, String productName, String PlanName) throws Exception {

		LOGGER.info("AgentId: " + agentId + " :productName: " + productName + " :PlanName: " + PlanName);
		Session session = sessionFactory.getCurrentSession();
		StringBuilder strQuery = new StringBuilder();
		List DBQuoteSequence;
		String QSequence = "";
		String Qprefix = "";
		int prefix = 0;
		String numnerS = "";
		StringBuilder dbQuoteSequence = null;
		if ("CITIBANK".equalsIgnoreCase(agentId)) {
			agentId = "CITI";
		}
		LOGGER.info("session " + session);
		Query query = session.createQuery(
				"FROM PolicyPrefixMaster where agentId =:agentId and productName =:productName and planName =:planName");
		query.setParameter("agentId", agentId);
		query.setParameter("productName", productName);
		query.setParameter("planName", PlanName);

		List qList = query.list();
		Iterator itr = qList.iterator();

		while (itr.hasNext()) {

			PolicyPrefixMaster m = (PolicyPrefixMaster) itr.next();

			QSequence = m.getQuoteSequence();
			LOGGER.info("m.getQuoteSequence():: " + m.getQuoteSequence());
			Qprefix = m.getQuotePrefix();
			LOGGER.info("m.getQuotePrefix():: " + m.getQuotePrefix());
		}
		strQuery.append(" select " + QSequence + " from dual ");

		Query query1 = session.createSQLQuery(strQuery.toString());
		DBQuoteSequence = query1.list();
		numnerS = DBQuoteSequence.get(0).toString();
		prefix = 6 - numnerS.length();
		String pString = "";
		while (prefix > 0) {
			pString = "0" + pString;
			--prefix;
		}

		dbQuoteSequence = new StringBuilder();
		LOGGER.info("dbQuoteSequence1:: " + dbQuoteSequence);
		dbQuoteSequence.append(agentId);
		LOGGER.info("dbQuoteSequence2:: " + dbQuoteSequence);
		dbQuoteSequence.append(Qprefix);
		LOGGER.info("dbQuoteSequence3:: " + dbQuoteSequence);
		dbQuoteSequence.append(pString + numnerS);
		LOGGER.info("dbQuoteSequence4:: " + dbQuoteSequence);
		return dbQuoteSequence.toString();

	}

	@Transactional(readOnly = true)
	public String getState(String city) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("select STATE_NAME from CITY_STATE_MASTER where upper(CITY_NAME)=upper(:city)")
				.setParameter("city", city);
		query.setMaxResults(1);
		return (String) query.uniqueResult();
	}

	@Transactional(readOnly = true)
	public void getBulkRateNilIntermediatonRate(MotorInsuranceModel motorInsuranceModel) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		int vehicleAge = 0;
		if (motorInsuranceModel.getPolicyTerm() == 2) {
			vehicleAge = motorInsuranceModel.getVehicleAgeforyear2() != null
					? motorInsuranceModel.getVehicleAgeforyear2().intValue() : 0;
		}

		if (motorInsuranceModel.getPolicyTerm() == 3) {
			vehicleAge = motorInsuranceModel.getVehicleAgeforyear3() != null
					? motorInsuranceModel.getVehicleAgeforyear3().intValue() : 0;
		}

		if (motorInsuranceModel.getPolicyTerm() == 4) {
			vehicleAge = motorInsuranceModel.getVehicleAgeforyear4() != null
					? motorInsuranceModel.getVehicleAgeforyear4().intValue() : 0;
		}
		if (motorInsuranceModel.getPolicyTerm() == 5) {
			vehicleAge = motorInsuranceModel.getVehicleAgeforyear5() != null
					? motorInsuranceModel.getVehicleAgeforyear5().intValue() : 0;
		}

		AgentMaster agentMaster = getAgentMaster(motorInsuranceModel.getAgentId());
		LOGGER.info("agentMaster getAgCode() ::: " + agentMaster.getAgCode());
		LOGGER.info("agentMaster getOaCode() ::: " + agentMaster.getOaCode());
		LOGGER.info("vehicleAge ::: " + vehicleAge);
		LOGGER.info("motorInsuranceModel.getPolicyStartDate() ::: " + motorInsuranceModel.getPolicyStartDate());

		Query query = session
				.createSQLQuery(
						"SELECT * FROM BULK_DEAL_DISCOUNT WHERE ( (UPPER(PARTYCODE) = UPPER(:partyCode)) OR UPPER(PARTYCODE) = 'ALL')"
								+ "AND TRANSACTIONTYPE = :transactiontype "
								+ "AND :vehicleAge BETWEEN STARTAGE AND ENDAGE "
								+ " AND(TO_DATE(:policyStartDate, 'dd/mm/yyyy') >= EFFECTIVE_START_DATE) "
								+ "AND(EFFECTIVE_END_DATE IS NULL OR(TO_DATE(:policyStartDate, 'dd/mm/yyyy') <= EFFECTIVE_END_DATE)) "
								+ " AND PRODUCTNAME = :productName")
				.addEntity(BulkDealDiscount.class).setParameter("partyCode", agentMaster.getAgCode())
				.setParameter("transactiontype", CommonUtils.NB_TRANSACTION_TYPE).setParameter("vehicleAge", vehicleAge)
				.setParameter("policyStartDate", motorInsuranceModel.getPolicyStartDate())
				.setParameter("productName", Constants.MotorCyclePackage_LongTerm);
		List<BulkDealDiscount> list = query.list();
		// if (list != null && !list.isEmpty()) {
		if (false) {
			motorInsuranceModel.setBulkRate(list.get(0).getDiscountValue());
			LOGGER.info("motorInsuranceModel.getBulkRate() ::1: " + motorInsuranceModel.getBulkRate());
		} else {
			query.setParameter("partyCode", agentMaster.getOaCode());
			// list = (List<BulkDealDiscount>)
			// getNamedQuery("GetBulkDealDiscount", agentMaster.getOaCode(),
			// vehicleAge, motorInsuranceModel.getPolicyStartDate(),
			// Constants.MotorCyclePackage_LongTerm);
			list = query.list();
			if (list != null && !list.isEmpty()) {
				motorInsuranceModel.setBulkRate(list.get(0).getDiscountValue());
				LOGGER.info("motorInsuranceModel.getBulkRate() ::2: " + motorInsuranceModel.getBulkRate() + "_"
						+ agentMaster.getOaCode());
			} else {
				query.setParameter("partyCode", "ALL");
				// list = (List<BulkDealDiscount>)
				// getNamedQuery("GetBulkDealDiscount", "All",
				// vehicleAge, motorInsuranceModel.getPolicyStartDate(),
				// Constants.MotorCyclePackage_LongTerm);
				list = query.list();
				if (list != null && !list.isEmpty()) {
					motorInsuranceModel.setBulkRate(list.get(0).getDiscountValue());
					LOGGER.info("motorInsuranceModel.getBulkRate() ::3: " + motorInsuranceModel.getBulkRate() + "_All");
				}
			}
		}
		session.clear();
		query = session
				.createSQLQuery(
						"SELECT * FROM NILL_INTERMEDIATION_DISCOUNT WHERE ((UPPER(PARTYCODE) = UPPER(:partyCode)) OR UPPER(PARTYCODE) = 'ALL') AND TRANSACTIONTYPE = :transactiontype AND :vehicleAge BETWEEN STARTAGE AND ENDAGE "
								+ " AND(TO_DATE(:policyStartDate, 'dd/mm/yyyy') >= EFFECTIVE_START_DATE) AND(EFFECTIVE_END_DATE IS NULL OR(TO_DATE(:policyStartDate, 'dd/mm/yyyy') <= EFFECTIVE_END_DATE))"
								+ " AND PRODUCTNAME = :productName")
				.addEntity(NillIntermediationDiscount.class).setParameter("partyCode", agentMaster.getAgCode())
				.setParameter("transactiontype", CommonUtils.NB_TRANSACTION_TYPE).setParameter("vehicleAge", vehicleAge)
				.setParameter("policyStartDate", motorInsuranceModel.getPolicyStartDate())
				.setParameter("productName", Constants.MotorCyclePackage_LongTerm);
		List<NillIntermediationDiscount> list1 = query.list();
		if (list1 != null && !list1.isEmpty()) {
			motorInsuranceModel.setNilIntermediationDiscountRate(list1.get(0).getDiscountValue());
			LOGGER.info("motorInsuranceModel.getNilIntermediationDiscountRate() ::1: "
					+ motorInsuranceModel.getNilIntermediationDiscountRate());
		} else {
			// session.clear();
			// list1 = (List<NillIntermediationDiscount>)
			// getNamedQuery("GetNillIntermediationDiscount",
			// agentMaster.getOaCode(),
			// vehicleAge, motorInsuranceModel.getPolicyStartDate(),
			// Constants.MotorCyclePackage_LongTerm);
			query.setParameter("partyCode", agentMaster.getOaCode());
			list1 = query.list();
			if (list1 != null && !list1.isEmpty()) {
				motorInsuranceModel.setNilIntermediationDiscountRate(list1.get(0).getDiscountValue());
				LOGGER.info("motorInsuranceModel.getNilIntermediationDiscountRate() ::2: "
						+ motorInsuranceModel.getNilIntermediationDiscountRate() + "_" + agentMaster.getOaCode());
			} else {
				// session.clear();
				// list1 = (List<NillIntermediationDiscount>)
				// getNamedQuery("GetNillIntermediationDiscount", "All",
				// vehicleAge, motorInsuranceModel.getPolicyStartDate(),
				// Constants.MotorCyclePackage_LongTerm);
				query.setParameter("partyCode", "ALL");
				list1 = query.list();
				if (list1 != null && !list1.isEmpty()) {
					motorInsuranceModel.setNilIntermediationDiscountRate(list1.get(0).getDiscountValue());
					LOGGER.info("motorInsuranceModel.getNilIntermediationDiscountRate() ::3: "
							+ motorInsuranceModel.getNilIntermediationDiscountRate() + "_All");
				}
			}
		}
	}

	@Transactional(readOnly = true)
	private void setPricingElements(String productName, Coverage cov, String level) throws Exception {

		Query query;
		Session session = sessionFactory.getCurrentSession();

		int coverageId = cov.getCoverageid();
		if (Constants.PACKAGE_ATTRIBUTE.equalsIgnoreCase(level)) {

			query = session
					.createSQLQuery(
							"select pricingelementname from productpricingelements where pricingelementlevel=:Level and productname=:ProductName")
					.setParameter("Level", level).setParameter("ProductName", productName);

		} else {

			query = session
					.createSQLQuery(
							"select pricingelementname from productpricingelements where pricingelementlevel=:Level and productname=:ProductName and coverageid=:coverageId")
					.setParameter("Level", level).setParameter("ProductName", productName)
					.setParameter("coverageId", coverageId);
		}

		List<String> lstProductPricingElementModel = query.list();
		LOGGER.info("ProductPricingElementModel :  " + lstProductPricingElementModel.size() + " Is Empty == "
				+ lstProductPricingElementModel.isEmpty());

		for (String value : lstProductPricingElementModel) {

			cov.setPricing(value, 0);
			LOGGER.info("ProductPricingElementModel :  " + value);
		}

	}

	@Transactional(readOnly = true)
	private List<Attributes> setAttributes(String strProductName, Coverage objCoverage, String strLevel)
			throws Exception {
		List<Attributes> attributes = new ArrayList<Attributes>();
		Query query;
		Session session = sessionFactory.getCurrentSession();
		int coverageID = objCoverage.getCoverageid();

		LOGGER.info("attribute level : " + strLevel + ", Product name : " + strProductName + ", coverage Id : "
				+ coverageID);
		if (Constants.PACKAGE_ATTRIBUTE.equalsIgnoreCase(strLevel)) {
			query = session
					.createSQLQuery(
							"select * from PRODUCTATTRIBUTES a where a.ATTRIBUTELEVEL=:Level and a.PRODUCTNAME=:ProductName")
					.addScalar("id", new LongType()).addScalar("productname").addScalar("coveragename")
					.addScalar("attributename").addScalar("attributetype").addScalar("attributedatatype")
					.addScalar("attributevalue").addScalar("attributelevel").addScalar("productid", new LongType())
					.addScalar("coverageid", new LongType())
					.setResultTransformer(Transformers.aliasToBean(Productattributes.class))
					.setParameter("Level", strLevel).setParameter("ProductName", strProductName);
		} else {
			query = session
					.createSQLQuery(
							"select * from PRODUCTATTRIBUTES a where a.ATTRIBUTELEVEL=:Level and a.PRODUCTNAME=:ProductName and a.COVERAGEID=:CoverageId")
					.addScalar("id", new LongType()).addScalar("productname").addScalar("coveragename")
					.addScalar("attributename").addScalar("attributetype").addScalar("attributedatatype")
					.addScalar("attributevalue").addScalar("attributelevel").addScalar("productid", new LongType())
					.addScalar("coverageid", new LongType())
					.setResultTransformer(Transformers.aliasToBean(Productattributes.class))
					.setParameter("Level", strLevel).setParameter("ProductName", strProductName)
					.setParameter("CoverageId", objCoverage.getCoverageid());
		}

		List<Productattributes> attributesList = query.list();
		LOGGER.info("Attribute Size" + attributesList.size());
		for (Productattributes attribute : attributesList) {
			Attributes attr = new Attributes();
			attr.setAttributeDataType(attribute.getAttributedatatype());
			attr.setAttributeName(attribute.getAttributename());
			attr.setAttributeType(attribute.getAttributetype());
			attr.setCoverageId(Integer.parseInt(attribute.getCoverageid() + ""));
			attr.setCoverageName(attribute.getCoveragename());
			attr.setId(Integer.parseInt(attribute.getId() + ""));
			attr.setLevel(attribute.getAttributelevel());
			attr.setProductId(Integer.parseInt(attribute.getProductid() + ""));
			attr.setProductName(attribute.getProductname());
			attr.setSelecetdAttributeValue(attribute.getAttributevalue());
			attributes.add(attr);
		}

		return attributes;
	}

	/**
	 * <h1>Technical Discount</h1>
	 * This implements the technical discount logic which will be taken from DB.
	 * 		<p>
	 * 		The technical discont is applicable according to the agentId, manufacture name, model code, region, vehicle age, policy start date,
	 * 		engine capacity, vehicle registered in the name of state and city, business type and NCB.
	 * 		<p><b>Note: </b>
	 * 			<b>Type of Grid:</b>
	 * 				AGENT-REGION, MODEL-AGENT, MODEL-STATE, MODEL-REGION
	 * 		</p>
	 * 	<h4>Other Logics :(Optional)</h4>
	 * 1. If IsPosOpted then, Technical discount getting from request and finally comparing the value with the DB value.
	 *  then, finding the less discount(%) and discount value will be applied. 
	 *  <p> 2.Campaign discount also will be added into technical discount. </p>
	 * @param motorInsuranceModel
	 * @param productName
	 * @return
	 * @throws Exception
	 * @author roshini
	 * Updated on 2019-11-20
	 */
	@Transactional(readOnly = true)
	public double getTechnicalDiscount(MotorInsuranceModel motorInsuranceModel, String productName) throws Exception {
		LOGGER.info("@getTechnicalDiscount() - " + productName);

		double Discount = 0;
		double totalDiscount = 0.0d;
		boolean isAvailable = false;
		double addlDiscount = 0;
		String vehicleManufacturerName = motorInsuranceModel.getVehicleManufacturerName();
		String policyStartDate = motorInsuranceModel.getPolicyStartDate();

		double techDiscount = 0;
		if (motorInsuranceModel == null) {
			return techDiscount;
		} else {
			if (motorInsuranceModel.getPolicyTerm() > 1) {
				productName = Constants.MotorCyclePackage_LongTerm;
			}

			String region = motorInsuranceModel.getRegion();
			String modelCode = motorInsuranceModel.getVehicleModelCode();
			String stateCode = motorInsuranceModel.getStateCode();
			String agent_id = motorInsuranceModel.getAgentId();
			int vehicleAge = motorInsuranceModel.getVehicleAge();
			double engineCapacity = motorInsuranceModel.getEngineCapacity();
			LOGGER.info("VehicleAge : " + vehicleAge + "stateCode : " + stateCode);
			String inceptionDate = motorInsuranceModel.getPolicyStartDate();
			String transactionType = "";
			boolean isPreviousPolicyHolder = "true".equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder());
			if (Constants.TRUE.equalsIgnoreCase(motorInsuranceModel.getIsPreviousPolicyHolder())) {
				transactionType = CommonUtils.NB_TRANSACTION_TYPE; // CommonUtils.RN_TRANSACTION_TYPE;
			} else {
				transactionType = CommonUtils.NB_TRANSACTION_TYPE;
				if (vehicleAge == 0) {
					vehicleAge = -1;
				}
			}
			LOGGER.info("getTechnicalDiscount-motorInsuranceModel-Region : " + region);

			// added for sometimes DB return W/O space in region start
			try {
				if (region.contains(" ")) {
					region = (region != null) ? region.substring(0, region.indexOf(' ')) : "";
				}
			} catch (Exception e) {
				LOGGER.info("Exception-motorInsuranceModel-Region : " + region);
				LOGGER.info("Exception-motorInsuranceModel-Region : " + e);
			}

			// added for sometimes DB return W/O space in region end

			String gridName = "";
			String yAxis = "";

			/* This code updated for Gridupload master changes */

			gridName = "Model-Agent";

			LOGGER.info("yAxis : " + region);

			LOGGER.info("cap loading for agent_id:" + agent_id);
			/*
			 * if ("RSAI".equalsIgnoreCase(agent_id)) {
			 * 
			 * yAxis = "IMTEAM_" + region; } else { yAxis = "PBAZAR_" + region;
			 * }
			 */
			String agentGroup = getAgentGroup(agent_id, Constants.PRODUCT_TWOWHEELER);
			yAxis = agentGroup + "_" + region;

			LOGGER.info("yAxis : " + yAxis);

			String Capregion = "IMTEAM_" + region;

			LOGGER.info("Entering into grid upload master table...");
			// Discount from EMotor
			techDiscount = getTechnicalDiscountMotor(productName, vehicleManufacturerName,agent_id, policyStartDate,
					transactionType);
			if (techDiscount == 999.0) {
				// Discount by AGENT-REGION
				gridName = Constants.AGENT_REGION;
				String agCode = getAgentMaster(agent_id).getAgCode();
				if (StringUtils.isNotBlank(agCode)) {
					yAxis = agCode + "_" + region;
					techDiscount = getGridValueFromDB(modelCode, yAxis, gridName, policyStartDate, transactionType,
							productName, vehicleAge, motorInsuranceModel.getCityCode(), true);

					if (techDiscount == 999.0) {
						techDiscount = getGridValueFromDB(modelCode, yAxis, gridName, policyStartDate, transactionType,
								productName, vehicleAge, motorInsuranceModel.getStateCode(), false);
					}
					if (techDiscount == 999.0) {
						techDiscount = getGridValueFromDB(modelCode, yAxis, gridName, policyStartDate, transactionType,
								productName, vehicleAge);
					}

				}
				LOGGER.info("techDiscount from AGENT_REGION ** " + techDiscount);
				// Discount by MODEL-AGENT
				if (StringUtils.isNotBlank(agentGroup)) {
					gridName = Constants.MODEL_AGENT;
					yAxis = agentGroup + "_" + region;
					// NCB Discount
					if (techDiscount == 999.0
							&& (getPbGridUploadMaster(vehicleManufacturerName) && isPreviousPolicyHolder
									&& !Constants.DATE_DEFAULT.equalsIgnoreCase(motorInsuranceModel.getPreviousPolicyExpiryDate())
									&& StringUtils.isNotBlank(motorInsuranceModel.getPreviousPolicyExpiryDate())
									&& StringUtils.isNotBlank(motorInsuranceModel.getNoClaimBonusPercent()))) {
						LOGGER.info("motorInsuranceModel (( 2)) " + motorInsuranceModel.getCityCode());
						techDiscount = getGridValueByNCB(techDiscount, modelCode, yAxis, gridName, policyStartDate,
								transactionType, productName, vehicleAge, stateCode, motorInsuranceModel);
						LOGGER.info("Discount by NCB ::: " + techDiscount);
					}
					if (techDiscount == 999.0) {
						techDiscount = getGridValueFromDB(modelCode, yAxis, gridName, policyStartDate, transactionType,
								productName, vehicleAge, motorInsuranceModel.getCityCode(), true);
					}
					if (techDiscount == 999.0) {
						techDiscount = getGridValueFromDB(modelCode, yAxis, gridName, policyStartDate, transactionType,
								productName, vehicleAge, motorInsuranceModel.getStateCode(), false);
					}
					if (techDiscount == 999.0) {
						LOGGER.info("techDiscount 3 ::: " + techDiscount);
						techDiscount = getGridValueFromDB(modelCode, yAxis, gridName, policyStartDate, transactionType,
								productName, vehicleAge);
					}

				}
				LOGGER.info("techDiscount from MODEL_AGENT ** " + techDiscount);
				// Discount by MODEL-STATE
				if (techDiscount == 999.0) {
					gridName = Constants.MODEL_STATE;
					yAxis = motorInsuranceModel.getState();
					techDiscount = getGridValueFromDBForModelState(modelCode, yAxis, gridName, policyStartDate,
							transactionType, productName, vehicleAge, motorInsuranceModel.getStateCode(), true);
					if (techDiscount == 999.0) {
						techDiscount = getGridValueFromDBForModelState(modelCode, yAxis, gridName, policyStartDate,
								transactionType, productName, vehicleAge, motorInsuranceModel.getStateCode(), false);
					}
				}
				LOGGER.info("techDiscount from MODEL_STATE ** " + techDiscount);
				// Discount by Model-Region
				if (techDiscount == 999.0) {
					gridName = Constants.MODEL_REGION;
					techDiscount = getGridValueFromDB(modelCode, region, gridName, policyStartDate,
							transactionType, productName, vehicleAge, motorInsuranceModel.getCityCode(), true);
					if (techDiscount == 999.0) {
						techDiscount = getGridValueFromDB(modelCode, region, gridName, policyStartDate,
								transactionType, productName, vehicleAge, motorInsuranceModel.getStateCode(), false);
					}
					if (techDiscount == 999.0) {
						techDiscount = getGridValueFromDB(modelCode, region, gridName, policyStartDate,
								transactionType, productName, vehicleAge);
					}
				}
				LOGGER.info("techDiscount from MODEL_REGION ** " + techDiscount);
			}
			/*
			 * LOGGER.info("motorInsuranceModel.getAgentId() ::: " +
			 * motorInsuranceModel.getAgentId());
			 * if(StringUtils.containsIgnoreCase(agentGroup, "PBAZAR")) {
			 *//**
				 * Task #206130 GridUpload Master query change
				 */
			/*
			 * LOGGER.info("motorInsuranceModel.getStateCode()::: " +
			 * motorInsuranceModel.getStateCode()); techDiscount =
			 * getGridValueFromDB(modelCode, yAxis, gridName, inceptionDate,
			 * transactionType, productName, vehicleAge,
			 * motorInsuranceModel.getStateCode()); if (techDiscount == 999.0) {
			 * LOGGER.info("techDiscount 1 ::: " + techDiscount); techDiscount =
			 * getGridValueFromDB(modelCode, yAxis, gridName, inceptionDate,
			 * transactionType, productName, vehicleAge);
			 * LOGGER.info("WithOut stateCode techDiscount ::: " +
			 * techDiscount); }
			 * 
			 * if (techDiscount == 999.0) { LOGGER.info("techDiscount 2 ::: " +
			 * techDiscount); techDiscount = getGridValueFromDB(modelCode,
			 * region, gridName, inceptionDate, transactionType, productName,
			 * vehicleAge); LOGGER.
			 * info("WithOut stateCode and check region itself techDiscount ::: "
			 * + techDiscount); }
			 * 
			 * } else {
			 *//**
				 * Task #206130 GridUpload Master query change
				 *//*
				 * techDiscount = getGridValueFromDB(modelCode, yAxis, gridName,
				 * inceptionDate, transactionType, productName, vehicleAge); if
				 * (techDiscount == 999.0) { LOGGER.info("techDiscount 3 ::: " +
				 * techDiscount); techDiscount = getGridValueFromDB(modelCode,
				 * region, gridName, inceptionDate, transactionType,
				 * productName, vehicleAge); LOGGER.
				 * info("WithOut stateCode and check region itself techDiscount ::: "
				 * + techDiscount); } }
				 */

			LOGGER.info("techDiscount : " + techDiscount);

			if (techDiscount == 999.0) {
				LOGGER.info("Entering into Discount Grid table...");
				LOGGER.info("Product Name ::: " + productName);
				totalDiscount = getDiscountGrid(motorInsuranceModel.getVehicleManufacturerName(), region,
						engineCapacity, inceptionDate, transactionType);
			}
			LOGGER.info("Totaldiscount:: " + totalDiscount);
			if (techDiscount == 999.0) {
				techDiscount = 0;
			}

			double doubleTechDiscount = techDiscount;
			/*
			 * totalDiscount =
			 * getDiscountGrid(motorInsuranceModel.getVehicleManufacturerName(),
			 * region, engineCapacity, inceptionDate, transactionType);
			 * LOGGER.info("totalDiscount : " + totalDiscount);
			 */

			if (totalDiscount != 0) {
				isAvailable = true;
				addlDiscount = totalDiscount - doubleTechDiscount;
				LOGGER.info("addlDiscount = totalDiscount - doubleTechDiscount : " + addlDiscount + "=" + totalDiscount
						+ "-" + doubleTechDiscount);
			}

			LOGGER.info("isAvailable in DicountGrid Value  -> " + isAvailable);

			if (techDiscount != 0) {
				totalDiscount = techDiscount;
			}

			if (isAvailable) {
				Discount = totalDiscount;
				LOGGER.info("isAvailable = Discount = totalDiscount : " + isAvailable + "=" + Discount + "="
						+ totalDiscount);
			} else {
				Discount = techDiscount;
				LOGGER.info("Discount = techDiscount : " + Discount + "=" + techDiscount);
			}

			// Task #278458 - POS DETAILS RS 154761 - TECH
			if ("Yes".equalsIgnoreCase(motorInsuranceModel.getIsPosOpted())
					&& motorInsuranceModel.getTechnicalDiscountFromRequest() != null) {
				if (motorInsuranceModel.getTechnicalDiscountFromRequest() > Discount) {
					Discount = motorInsuranceModel.getTechnicalDiscountFromRequest();
					totalDiscount = motorInsuranceModel.getTechnicalDiscountFromRequest();
				}
			}

			LOGGER.info(" Discount?: - " + Discount);
			LOGGER.info("campaign discount == " + motorInsuranceModel.isEnableCampaignDiscount());
			if (motorInsuranceModel.isEnableCampaignDiscount()) {
				BigDecimal enableCampaignDisount = getAgentPlanMappingCampaign(motorInsuranceModel.getAgentId());

				if (enableCampaignDisount != null && enableCampaignDisount.equals(new BigDecimal(1))) {
					double campaignDiscount = 0;

					campaignDiscount = getCampaignDiscount(agentGroup, Constants.PRODUCT_NAME, policyStartDate,
							vehicleManufacturerName, transactionType, vehicleAge);
					LOGGER.info("request campaing discount = " + motorInsuranceModel.getCampaignDiscountRequest());
					LOGGER.info("campaign discount from table " + campaignDiscount);
					LOGGER.info("motorInsuranceModel.getCampaignDiscountRequest() < campaignDiscount  = "
							+ (motorInsuranceModel.getCampaignDiscountRequest() < campaignDiscount));
					if (campaignDiscount != 999.0) {
						if (motorInsuranceModel.getCampaignDiscountRequest() > campaignDiscount) {
							campaignDiscount = motorInsuranceModel.getCampaignDiscountRequest();
						}
						LOGGER.info("campaignDiscount ::: " + campaignDiscount);
						motorInsuranceModel.setCampaignDiscount(campaignDiscount);
						Discount += campaignDiscount;
						totalDiscount += Discount;
					} else {
						motorInsuranceModel.setEnableCampaignDiscount(false);
					}
				}
			}
			LOGGER.info(" Discount?: - " + Discount);
			// TP Premium Discount
			if (motorInsuranceModel.getFuelType() != null && StringUtils.isNotBlank(motorInsuranceModel.getFuelType() )
					&& TP_DIS_FUEL_TYPES.contains(motorInsuranceModel.getFuelType().toUpperCase())) {
				motorInsuranceModel.setElectricOrBatteryDisc(getTPDiscount());
			}
			motorInsuranceModel.setTechnicalDiscount(techDiscount);
			motorInsuranceModel.setTotalDiscount(totalDiscount);
			motorInsuranceModel.setAdditionalDiscount(0);

		}
		return Discount;
	}

	/**
	 * <h2>Note:</h2>
	 * <p>
	 * TP Discount is only applicable, where the vehicle fuel type are
	 * TP_DIS_FUEL_TYPES,
	 * 
	 * @see com.xerago.rsa.Constants
	 * 
	 *      Get the Discount percentage
	 * @return tpDiscount
	 * 
	 * @author roshini
	 * @since 2019-05-30
	 */
	@Transactional(readOnly = true)
	private float getTPDiscount() {
		float tpDiscount = 0.0f;
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		List<BigDecimal> list;
		try {
			query = session.createSQLQuery(
					"Select ElectricOrBatteryDisc from AGENT_PLAN_MAPPING where PLAN_TYPE = 'TWOWHEELER' AND ElectricOrBatteryDisc IS NOT NULL ");
			list = query.list();
			if (list != null && !list.isEmpty() && list.size() > 0 && list.get(0) != null) {
				tpDiscount = list.get(0).floatValue();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return tpDiscount;
	}
		
	/**
	 * @author roshini
	 * @since 2019-04-05
	 * @param xAxis
	 * @param yAxis
	 * @param gridName
	 * @param inceptionDate
	 * @param transactionType
	 * @param productName
	 * @param vehicleAge
	 * @param stateCode
	 * @param withState
	 * @return
	 */
	@Transactional(readOnly = true)
	private double getGridValueFromDBForModelState(String xAxis, String yAxis, String gridName, String inceptionDate,
			String transactionType, String productName, int vehicleAge, String stateCode, boolean withState) {
		double griddiscountValue = 999.0;
		List<MotorTechnicalDiscount> grid = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = null;
		try {
			if (withState) {
				query = session.getNamedQuery("GridValueFromDBForModelRegionWithState")
						.setParameter("productName", productName).setParameter("xAxis", xAxis)
						.setParameter("YAXIS", yAxis).setParameter("gridName", gridName)
						.setParameter("transactionType", transactionType).setParameter("inceptionDate", inceptionDate)
						.setParameter("vehicleAge", vehicleAge).setParameter("stateCode", "%" + stateCode + "%");
			} else {
				query = session.getNamedQuery("GridValueFromDBForModelRegionWithoutState")
						.setParameter("productName", productName).setParameter("xAxis", xAxis)
						.setParameter("YAXIS", yAxis).setParameter("gridName", gridName)
						.setParameter("transactionType", transactionType).setParameter("inceptionDate", inceptionDate)
						.setParameter("vehicleAge", vehicleAge);
			}
			grid = query.list();
			if (grid != null && !grid.isEmpty() && grid.size() > 0 && grid.get(0) != null) {
				griddiscountValue = grid.get(0).getTechniclaDiscount().doubleValue();
			} else {
				griddiscountValue = 999.0;
			}
		} catch (Exception e) {
			LOGGER.error("Technical Discount Fetch Error ** " + e.getStackTrace());
			griddiscountValue = 999.0;
		}

		return griddiscountValue;
	}


	/**
	 * @author roshini
	 * @since 2019-04-05
	 * @param techDiscount
	 * @param modelCode
	 * @param yAxis
	 * @param gridName
	 * @param policyStartDate
	 * @param transactionType
	 * @param productName
	 * @param vehicleAge
	 * @param stateCode
	 * @param motorInsuranceModel
	 * @return Updated, Refer griduploadmaster instead of pb_griduploadmaster
	 *         table.
	 */
	@Transactional(readOnly = true)
	private double getGridValueByNCB(double techDiscount, String modelCode, String yAxis, String gridName,
			String policyStartDate, String transactionType, String productName, int vehicleAge, String stateCode,
			MotorInsuranceModel motorInsuranceModel) {
		long differenceInDays = PortalDateConversion.diffDates(motorInsuranceModel.getPreviousPolicyExpiryDate(),
				new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date()));
		String ncbCurrent = "0";
		LOGGER.info("differenceInDays == " + differenceInDays);
		if (differenceInDays > 90) {
			LOGGER.info(" 90 days :: ");
			ncbCurrent = "0";
		} else if (!Constants.NO.equalsIgnoreCase(motorInsuranceModel.getCarOwnerShip())
				|| !Constants.NO.equalsIgnoreCase(motorInsuranceModel.getClaimsMadeInPreviousPolicy())
				|| StringUtils.isBlank(motorInsuranceModel.getNoClaimBonusPercentinCurrent())) {
			LOGGER.info(" 2nd :: ");
			ncbCurrent = "0";
		} else
			ncbCurrent = motorInsuranceModel.getNoClaimBonusPercentinCurrent();
		// Task #272533 PB Gridupload master query change-150476
		techDiscount = getPBGridValueFromDB(modelCode, yAxis, gridName, policyStartDate, transactionType, productName,
				vehicleAge, null, ncbCurrent, motorInsuranceModel.getCityCode());

		if ( techDiscount == 999.0) {
			LOGGER.info("techDiscount 0::: " + techDiscount);
			techDiscount = getPBGridValueFromDB(modelCode, yAxis, gridName, policyStartDate, transactionType,
					productName, vehicleAge, stateCode, ncbCurrent, null);
		}

		return techDiscount;
	}
	
	@Transactional(readOnly = true)
	public double getPBGridValueFromDB(String xAxis, String yAxis, String gridName, String inceptionDate,
			String transactionType, String productName, int vehicleAge, String stateCode, String NCBValue,
			String cityCode) {
		LOGGER.info("xAxis: " + xAxis + ", yAxis: " + yAxis + ", gridName: " + gridName + ", inceptionDate: "
				+ inceptionDate + ", transactionType" + transactionType + ", productName: " + productName
				+ ", vehicleAge:" + vehicleAge + ", stateCode:" + stateCode);
		LOGGER.info("NCBValue ::: " + NCBValue);
		LOGGER.info("cityCode ::: " + cityCode);

		Query query;
		double gridValue = 999.0;

		List<MotorTechnicalDiscount> grid = null;

		Session session = sessionFactory.getCurrentSession();

		if (xAxis == null || yAxis == null) {
			return gridValue;
		}
		try {

			if (StringUtils.isNotBlank(cityCode)) {
				query = session.getNamedQuery("GridValueFromDBForNCBWithCity").setParameter("productName", productName)
						.setParameter("xAxis", xAxis).setParameter("YAXIS", yAxis).setParameter("gridName", gridName)
						.setParameter("transactionType", transactionType).setParameter("inceptionDate", inceptionDate)
						.setParameter("vehicleAge", vehicleAge)
						// .setParameter("stateCode", "%" + stateCode + "%")
						.setParameter("NCBValue", NCBValue.equalsIgnoreCase("0") ? NCBValue : "%" + NCBValue + "%")
						.setParameter("cityCode", "%" + cityCode + "%");
			} else {
				query = session.getNamedQuery("GridValueFromDBForNCBWithState").setParameter("productName", productName)
						.setParameter("xAxis", xAxis).setParameter("YAXIS", yAxis).setParameter("gridName", gridName)
						.setParameter("transactionType", transactionType).setParameter("inceptionDate", inceptionDate)
						.setParameter("vehicleAge", vehicleAge).setParameter("stateCode", "%" + stateCode + "%")
						.setParameter("NCBValue", NCBValue.equalsIgnoreCase("0") ? NCBValue
								: "%" + NCBValue + "%");

			}
			grid = query.list();

			if (!grid.isEmpty()) {
				gridValue = grid.get(0).getTechniclaDiscount().doubleValue();
			} else {
				gridValue = 999.0;
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return gridValue;
		}
		return gridValue;

	}

	private boolean getPbGridUploadMaster(String vehicleManufacturerName) {
		LOGGER.info("ManufactureName::" + vehicleManufacturerName);
		Session session = sessionFactory.getCurrentSession();
		boolean pbgid = false;
		try {
			Query query = session.createSQLQuery(
					"SELECT * FROM AGENT_PLAN_MAPPING WHERE ENABLE_PB_TECH_DISCOUNT = '1' and MANUFACTURE_NAME LIKE '%"
							+ vehicleManufacturerName + "%' ");

			if (!query.list().isEmpty()) {
				pbgid = true;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return pbgid;
	}

		/**
		 * @author roshini
		 * @since 2019-04-05
		 * @param productName
		 * @param vehicleManufacturerName
		 * @param agentId
		 * @param policyStartDate
		 * @param transactionType
		 * @return Fetch Discount from EMotor
		 * 
		 */
		@Transactional(readOnly = true)
		private double getTechnicalDiscountMotor(String productName, String vehicleManufacturerName, String agentId,
				String policyStartDate, String transactionType) {
			LOGGER.info("productName = " + productName + ", vehicleManufacturerName = " + vehicleManufacturerName
					+ ", agentId = " + agentId + ", policyStartDate = " + policyStartDate + ", transactionType = "
					+ transactionType);
			double griddiscountValue = 999.0;
			List<MotorTechnicalDiscount> grid = null;
			Session session = sessionFactory.getCurrentSession();
			try {
				Query q = session.getNamedQuery("DiscountFromEMotor").setParameter("agCode", agentId)
						.setParameter("productName", productName).setParameter("transactionType", transactionType)
						.setParameter("make", vehicleManufacturerName).setParameter("inceptionDate", policyStartDate);

				grid = q.list();
				if (grid != null && !grid.isEmpty() && grid.size() > 0 && grid.get(0) != null) {
					griddiscountValue = grid.get(0).getTechniclaDiscount().doubleValue();
				} else {
					griddiscountValue = 999.0;
				}
			} catch (Exception e) {
				LOGGER.info("Technical Discount Fetch Error ** " + e, e.getStackTrace());
				griddiscountValue = 999.0;
			}

			return griddiscountValue;
		}
		
	@Transactional(readOnly = true)
	public double getCampaignDiscount(String agentGroup, String productName, String policyStartDate,
			String vehicleManufacturerName, String transactionType, int vehicleAge) {
		LOGGER.info("agentGroup :: " + agentGroup + "    productName :: " + productName + "    policyStartDate :: "
				+ policyStartDate + "   modelCode :: " + vehicleManufacturerName + "   transactionType :: "
				+ transactionType);
		double campaignDiscount = 999.0;
		Session session = sessionFactory.getCurrentSession();

		try {
			Query query = session
					.createSQLQuery(
							"SELECT DCSC.MAXDISCOUNT FROM DISCOUNTCAMPAIGNSEATINGCAP DCSC JOIN DISCOUNTCAMPAIGN DC ON DC.ID = DCSC.FK_ID "
									+ " WHERE DC.PRODUCT IN (:productName,'ALL') AND UPPER(DC.AGENTGROUP) = UPPER(:agentGroup) "
									+ " AND to_date(:strStartDate,'dd/MM/yyyy') >= to_date(DC.EFFECTIVE_START_DATE,'dd/MM/yyyy') AND DC.MAKE IN (:make,'ALL') "
									+ " AND :vehicleAge BETWEEN DC.START_AGE AND DC.END_AGE "
									+ " AND DC.TRANSACTIONTYPE IN (:transactionType, 'ALL') ")
					.setParameter("productName", productName).setParameter("agentGroup", agentGroup)
					.setParameter("strStartDate", policyStartDate).setParameter("make", vehicleManufacturerName)
					.setParameter("transactionType", transactionType).setParameter("vehicleAge", vehicleAge);
			if (query.list() != null && !query.list().isEmpty() && query.list().get(0) != null) {
				campaignDiscount = ((BigDecimal) query.list().get(0)).doubleValue() / 100;
			} else {
				campaignDiscount = 999.0;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return campaignDiscount;
	}

	@Transactional
	public String getAgentGroup(String agentId, String planType) {
		Session session = sessionFactory.getCurrentSession();
		String agentGroup = "";
		String parentagentId = "";
		LOGGER.info("getAgentGroup agentId ::: " + agentId);
		LOGGER.info("getAgentGroup planType ::: " + planType);
		try {
			Query query = session.createQuery("From AgentPlanMapping where id.agentId=:agentId and planType=:planType")
					.setParameter("agentId", agentId).setParameter("planType", planType);
			List<AgentPlanMapping> list = query.list();
			if (list != null && !list.isEmpty()) {
				LOGGER.info("list.size() ::: " + list.size());
				AgentPlanMapping agentPlanMapping = list.get(0);
				agentGroup = agentPlanMapping.getAgentGroup();
				parentagentId = agentPlanMapping.getParentAgentId();
			} else {
				// By Default
				agentGroup = Constants.IMTEAM;
			}
			LOGGER.info("agentGroup ::: " + agentGroup);
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw e;
		}
		return agentGroup;
	}

	@Transactional(readOnly = true)
	public AgentMaster getAgentMaster(String agentId) {
		AgentMaster agentMaster = new AgentMaster();
		Session session = sessionFactory.getCurrentSession();
		List<AgentMaster> agentMastetList = session.createSQLQuery("select * from Agent_Master where agent_Id=:agentId")
				.addEntity(AgentMaster.class).setString("agentId", agentId).list();
		if (!agentMastetList.isEmpty()) {
			agentMaster = agentMastetList.get(0);
		}
		return agentMaster;
	}

	@Transactional(readOnly = true)
	private double getDiscountGrid(String manufacturerName, String region, double engineCapacity, String inceptionDate,
			String transactionType) throws Exception {

		Query query;
		double discountGrid = 0.0;
		List<MotorTechnicalDiscount> discount = null;
		Session session = sessionFactory.getCurrentSession();

		query = session.getNamedQuery("DiscountGridValueFromDB").setParameter("MakeName", manufacturerName)
				.setParameter("Region", region).setParameter("EngineCapacity", engineCapacity)
				.setParameter("InceptionDate", inceptionDate).setParameter("TransactionType", transactionType);

		discount = query.list();

		if (!discount.isEmpty()) {
			discountGrid = discount.get(0).getTechniclaDiscount().doubleValue();
		} else {
			discountGrid = 0.0;
		}
		return discountGrid;
	}

	/* Task #216350 GridUpload Master query change #206130 */
	public double getGridValueFromDB(String xAxis, String yAxis, String gridName, String inceptionDate,
			String transactionType, String productName, int vehicleAge) {
		LOGGER.info("xAxis: " + xAxis + ", yAxis: " + yAxis + ", gridName: " + gridName + ", inceptionDate: "
				+ inceptionDate + ", transactionType" + transactionType + ", productName: " + productName
				+ ", vehicleAge:" + vehicleAge);
		Query query;
		double gridValue = 999.0;

		List<MotorTechnicalDiscount> grid = null;

		Session session = sessionFactory.getCurrentSession();

		if (xAxis == null || yAxis == null) {
			return gridValue;
		}
		try {
			query = session.getNamedQuery("GridValueFromDBWithoutCityAndState").setParameter("productName", productName)
					.setParameter("xAxis", xAxis).setParameter("YAXIS", yAxis).setParameter("gridName", gridName)
					.setParameter("transactionType", transactionType).setParameter("inceptionDate", inceptionDate)
					.setParameter("vehicleAge", vehicleAge);
			grid = query.list();

			if (!grid.isEmpty()) {
				gridValue = grid.get(0).getTechniclaDiscount().doubleValue();
			} else {
				gridValue = 999.0;
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return gridValue;
		}
		return gridValue;

	}

	/* End */
	@Transactional(readOnly = true)
	public double getGridValueFromDB(String xAxis, String yAxis, String gridName, String inceptionDate,
			String transactionType, String productName, int vehicleAge, String stateCodeOrCity, boolean isCity) {
		LOGGER.info("xAxis: " + xAxis + ", yAxis: " + yAxis + ", gridName: " + gridName + ", inceptionDate: "
				+ inceptionDate + ", transactionType: " + transactionType + ", productName: " + productName
				+ ", vehicleAge: " + vehicleAge + ", stateCode: " + stateCodeOrCity);
		Query query;
		double gridValue = 999.0;

		List<MotorTechnicalDiscount> grid = null;
		Session session = sessionFactory.getCurrentSession();
		
		if (xAxis == null || yAxis == null) {
			return gridValue;
		}
		try {
			if (!isCity) {
				query = session.getNamedQuery("GridValueFromDBWithoutCity").setParameter("productName", productName)
						.setParameter("xAxis", xAxis).setParameter("YAXIS", yAxis).setParameter("gridName", gridName)
						.setParameter("transactionType", transactionType).setParameter("inceptionDate", inceptionDate)
						.setParameter("vehicleAge", vehicleAge).setParameter("stateCode", "%" + stateCodeOrCity + "%");
			} else {
				query = session.getNamedQuery("GridValueFromDBWithoutState").setParameter("productName", productName)
						.setParameter("xAxis", xAxis).setParameter("YAXIS", yAxis).setParameter("gridName", gridName)
						.setParameter("transactionType", transactionType).setParameter("inceptionDate", inceptionDate)
						.setParameter("vehicleAge", vehicleAge).setParameter("cityCode", "%" + stateCodeOrCity + "%");
			}
			grid = query.list();

			if (!grid.isEmpty()) {
				gridValue = grid.get(0).getTechniclaDiscount().doubleValue();
			} else {
				gridValue = 999.0;
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return gridValue;
		}

		return gridValue;

	}

	@Transactional(readOnly = true)
	private List<Double> setLimits(String productName, int covid, String limitName) throws Exception {
		Query query;
		List<Double> limitValues = null;
		Session session = sessionFactory.getCurrentSession();
		query = session
				.createSQLQuery(
						"select LIMITVALUE from productcoveragelimits where coverageid=:coverageId and productname=:ProductName and limitname=:limitName order by id ")
				.setParameter("coverageId", covid).setParameter("ProductName", productName)
				.setParameter("limitName", limitName);
		limitValues = query.list();
		LOGGER.info("Limits count : " + limitValues.size());

		return limitValues;
	}

	@Transactional(readOnly = true)
	public Integer updateVehicleDetails(String quoteId, MotorInsuranceModel motorInsuranceModel) throws Exception {
		Integer returnCode = 1;
		Session session = sessionFactory.getCurrentSession();
		List resultList = new ArrayList();
		resultList = session.createSQLQuery("select * from DQuoteDetails where quoteId =:quoteId")
				.addEntity(DQuoteDetails.class).setParameter("quoteId", quoteId).list();
		if (resultList.isEmpty()) {
			returnCode = 2;
		} else {
			DQuoteDetails dQuoteDetails = (DQuoteDetails) resultList.get(0);
			resultList.clear();
			LOGGER.info("dQuoteDetails.getPremium():" + dQuoteDetails.getPremium());
			motorInsuranceModel.setPremium(dQuoteDetails.getPremium().doubleValue());
			resultList = session
					.createQuery(
							"FROM DWebserviceUseDetails where quoteid =:quoteid and premium =:premium ORDER BY id DESC LIMIT 1")
					.setParameter("quoteid", quoteId).setBigDecimal("premium", dQuoteDetails.getPremium()).list();
			if (resultList.size() > 0) {

				dQuoteDetails.setStatus('Q');
				session.update(dQuoteDetails);

			} else {
				returnCode = 3;
			}

		}
		return returnCode;
	}

	@Transactional(readOnly = true)
	public Integer GproposalService(String quoteid, MotorInsuranceModel motorInsuranceModel) throws Exception {
		Integer returnCode = 2;
		Session session = sessionFactory.getCurrentSession();

		LOGGER.info("quoteid..::" + quoteid);
		if (org.apache.commons.lang3.StringUtils.isBlank(quoteid)) {
			motorInsuranceModel.setStatusFlag("Invalid QuoteId.");
			motorInsuranceModel.setStatusCode("E-0905");
			return returnCode = 0;
		}
		DQuoteDetails dQuoteDetails = session.get(DQuoteDetails.class, quoteid);
		Query query = session.createQuery("select clientCode from PClientDetails where lower(email)=lower(:email)")
				.setParameter("email", motorInsuranceModel.getEmailId());
		if (!query.list().isEmpty()) {
			motorInsuranceModel.setClientCode((String) query.list().get(0));
		}

		if (dQuoteDetails == null) {
			motorInsuranceModel.setStatusFlag("Invalid QuoteId.");
			motorInsuranceModel.setStatusCode("E-0905");
			return returnCode = 0;
		} else if (dQuoteDetails != null && dQuoteDetails.getStatus() == null) {
			return returnCode = 2;
		}

		List<DPolicyDetails> dPolicyDetailsList = new ArrayList<DPolicyDetails>(dQuoteDetails.getDPolicyDetailses());
		DPolicyDetails dPolicyDetails = dPolicyDetailsList.get(0);

		List<DPolicyVehicleDetails> dPolicyVehicleDetailsList = new ArrayList<>(
				dPolicyDetails.getDPolicyVehicleDetailses());
		DPolicyVehicleDetails dPolicyVehicleDetails = dPolicyVehicleDetailsList.get(0);
		LOGGER.info("premium compare..::" + (motorInsuranceModel.getPremium() >= dQuoteDetails.getPremium().doubleValue() - 2
				&& motorInsuranceModel.getPremium() <= dQuoteDetails.getPremium().doubleValue() + 2));

		if (!(motorInsuranceModel.getPremium() >= dQuoteDetails.getPremium().doubleValue() - 2
				&& motorInsuranceModel.getPremium() <= dQuoteDetails.getPremium().doubleValue() + 2)) {
			motorInsuranceModel.setStatusFlag("Premium mismatching with our records for given Quoteid.");
			motorInsuranceModel.setStatusCode("E-0901");
		} else if (!StringUtils.equalsIgnoreCase(dQuoteDetails.getClientCode(), motorInsuranceModel.getClientCode())) {
			motorInsuranceModel.setStatusFlag("Emailid mismatching with our records for given Quoteid.");
			motorInsuranceModel.setStatusCode("E-0902");
		} else if (!dQuoteDetails.getStatus().toString().equalsIgnoreCase("Q")) {
			if (dQuoteDetails.getStatus().toString().equalsIgnoreCase("B")) {
				motorInsuranceModel.setStatusFlag("Quote Already Purchased");
				motorInsuranceModel.setStatusCode("E-0903");
			} else {
				motorInsuranceModel
						.setStatusFlag("Verify the given quoteid to called for updatevehicledetails process");
				motorInsuranceModel.setStatusCode("E-0904");
			}
		} else if (StringUtils.containsIgnoreCase(dPolicyDetails.getProductName(), "Rollover")
				&& "1".equalsIgnoreCase(dPolicyDetails.getIsBreakinInsurance())) {
			motorInsuranceModel.setStatusFlag("VIR is required to Proceed payment");
			motorInsuranceModel.setStatusCode("E-0906");
		} else {
			returnCode = 1;
		}
		return returnCode;
	}

	public DPolicyDetails getBreakInInsuranceStatusForMotor(String quoteId) {
		DPolicyDetails dPolicyDetails = new DPolicyDetails();
		Session session = sessionFactory.getCurrentSession();
		try {

			Query query = session.createQuery("FROM DPolicyDetails where QUOTE_ID =:QuoteId").setParameter("QuoteId",
					quoteId);

			List quertList = query.list();

			if (!quertList.isEmpty()) {

				dPolicyDetails = (DPolicyDetails) quertList.get(0);
			}
		} catch (Exception e) {
			LOGGER.info("Exception :::" + e);
		}
		return dPolicyDetails;
	}

	@Transactional
	public boolean updateBreakInInsuranceForMotor(DPolicyDetails dPolicyDetails) {
		boolean isBreakInInsurance = false;
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(dPolicyDetails);
			isBreakInInsurance = true;
		} catch (Exception e) {
			LOGGER.info("Exception :::" + e);
			isBreakInInsurance = false;
		}
		return isBreakInInsurance;
	}

	@Transactional
	public Integer setInsetData(boolean isQuoteExistinDB, DQuoteDetails quoteDetails, DPolicyDetails policyDetails,
			List<DPolicyAttributes> policyAttributesList, List<DPolicyCoverages> policyCoveragesList,
			List<DPolicyPricingElements> policyPricingelementsList, DPolicyVehicleDetails policyVehicleDetails,
			List<DElectricalAccessoriesLimit> dElectricalAccessoriesLimitList,
			List<DNonElecAccessoriesLimit> dNonElecAccessoriesLimitList, DUpdatedClientDetails updatedClientDetails,
			List<DTppPricingElements> tppPricingElementsList, String process, DPoscodeDetails dPoscodeDetails, String source)
			throws Exception {
		Integer returnCode = 1;
		Session session = sessionFactory.getCurrentSession();
		DOtpDetails dOtpDetails = null;
		LOGGER.info("****************iNSERTING****************************" + isQuoteExistinDB);
		if (isQuoteExistinDB) {

			LOGGER.info("****************Deleting****************************");
			Query query = session.createQuery("FROM DQuoteDetails where quoteId =:quoteId").setParameter("quoteId",
					quoteDetails.getQuoteId());
			List quertList = query.list();
			LOGGER.info("****************Deleting****************************" + quertList.get(0));
			dOtpDetails = ((DQuoteDetails) quertList.get(0)).getDOtpDetails();

			session.delete(quertList.get(0));
		}
		LOGGER.info("****************iNSERTING****************************");
		session.save(quoteDetails);
		if (dOtpDetails != null) {
			session.merge(dOtpDetails);
		}
		session.save(updatedClientDetails);
		session.save(policyDetails);
		LOGGER.info("dPolicyDetails.getInceptionTime() 2 ::: " + policyDetails.getInceptionTime());
		session.save(policyVehicleDetails);
		if (!"calculatePremium".equalsIgnoreCase(process) || "STP".equalsIgnoreCase(source) ) {
			for (DPolicyAttributes policyAttributes : policyAttributesList) {
				session.save(policyAttributes);
			}
			for (DPolicyCoverages policyCoverages : policyCoveragesList) {
				session.save(policyCoverages);
			}
			for (DPolicyPricingElements policyPricingelements : policyPricingelementsList) {
				session.save(policyPricingelements);
			}
			for (DTppPricingElements tppPricingElements : tppPricingElementsList) {
				session.save(tppPricingElements);
			}
			for (DElectricalAccessoriesLimit dElectricalAccessoriesLimit : dElectricalAccessoriesLimitList) {
				session.save(dElectricalAccessoriesLimit);
			}
			for (DNonElecAccessoriesLimit dNonElecAccessoriesLimit : dNonElecAccessoriesLimitList) {
				session.save(dNonElecAccessoriesLimit);
			}
		}
		if (dPoscodeDetails != null) {
			session.saveOrUpdate(dPoscodeDetails);
		}
		session.flush();
		session.clear();

		LOGGER.info("****************iNSERTED****************************");
		return returnCode;
	}

	private CoverageModel convertCoverageMasterToCoverageModel(Productcoveragemaster coverageMaster) {
		CoverageModel coverage = new CoverageModel();
		coverage.setCoverageName(coverageMaster.getCoveragename());
		coverage.setParentCoverageId(coverageMaster.getParentcoverageid() + "");
		coverage.setCoverageDescription(coverageMaster.getCoveragedescription());
		coverage.setProductName(coverageMaster.getProductname());
		coverage.setIsselected(coverageMaster.getIsselected() + "");
		coverage.setIsComposite(coverageMaster.getIscomposite() + "");
		coverage.setIsMandatory(coverageMaster.getIsmandatory() + "");
		coverage.setLimitName(coverageMaster.getLimitname());
		coverage.setLimitType(coverageMaster.getLimittype());
		coverage.setDefaultLimitvalue(coverageMaster.getDefaultlimitvalue() + "");
		coverage.setLimitDescription(coverageMaster.getLimitdescription());
		coverage.setDeductibleName(coverageMaster.getDeductiblename());
		coverage.setDeductibleType(coverageMaster.getDeductibletype());
		coverage.setDefaultDeductiblevalue(coverageMaster.getDefaultdeductiblevalue() + "");
		coverage.setDeductibleDescription(coverageMaster.getDeductibledescription());
		coverage.setId(Integer.parseInt(coverageMaster.getId() + ""));

		return coverage;
	}

	@Transactional(readOnly = true)
	public LongtermRateMaster getLongtermRateMaster(String agentId, int vehicleAge, String policyStartDate,
			String productName) throws Exception {
		LongtermRateMaster longtermRateMaster = new LongtermRateMaster();
		AgentMaster agentMaster = getAgentMaster(agentId);
		LOGGER.info("agentMaster getAgCode() ::: " + agentMaster.getAgCode());
		LOGGER.info("agentMaster getOaCode() ::: " + agentMaster.getOaCode());
		LOGGER.info("vehicleAge ::: " + vehicleAge);
		LOGGER.info("motorInsuranceModel.getPolicyStartDate() ::: " + policyStartDate);

		List<LongtermRateMaster> list = (List<LongtermRateMaster>) getNamedQuery("LongtermRateMaster",
				agentMaster.getAgCode(), vehicleAge, policyStartDate, productName);

		if (list != null && !list.isEmpty()) {
			LOGGER.info("list size ::: " + list.size() + "_" + agentMaster.getAgCode());
			longtermRateMaster = list.get(0);
		} else {
			list = (List<LongtermRateMaster>) getNamedQuery("LongtermRateMaster", agentMaster.getOaCode(), vehicleAge,
					policyStartDate, productName);
			if (list != null && !list.isEmpty()) {
				LOGGER.info("list size ::: " + list.size() + "_" + agentMaster.getOaCode());
				longtermRateMaster = list.get(0);
			} else {
				list = (List<LongtermRateMaster>) getNamedQuery("LongtermRateMaster", "All", vehicleAge,
						policyStartDate, productName);
				if (list != null && !list.isEmpty()) {
					LOGGER.info("list size ::: " + list.size() + "_" + "All");
					longtermRateMaster = list.get(0);
				}
			}
		}
		return longtermRateMaster;
	}

	@Transactional(readOnly = true)
	private List getNamedQuery(String queryName, String partyCode, int vehicleAge, String policyStartDate,
			String productName) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		List list = new ArrayList();
		Query query = session.getNamedQuery(queryName).setParameter("partyCode", partyCode)
				.setParameter("transactiontype", "NB").setParameter("vehicleAge", vehicleAge)
				.setParameter("policyStartDate", policyStartDate).setParameter("productName", productName);
		list = query.list();
		session.clear();
		return list;
	}

	@Transactional(readOnly = true)
	public GoodFeatureDiscount getGoodFeatureDiscount(String modelCode, String stateCode) throws Exception {

		Session session = sessionFactory.getCurrentSession();

		GoodFeatureDiscount goodFeatureDiscount = new GoodFeatureDiscount();

		SimpleDateFormat objSdf = new SimpleDateFormat("dd/MM/yyyy");

		String effectiveStartDate = objSdf.format(new Date());
		String effectiveEndDate = objSdf.format(new Date());

		Query query = session
				.createSQLQuery(
						"SELECT * FROM GOOD_FEATURE_DISCOUNT WHERE MODEL_CODE=:ModelCode and STATE_CODE like :StateCode and to_date(:EffectiveStartDate,'dd/MM/yyyy') >= to_date(EFFECTIVE_START_DATE,'dd/MM/yyyy') and to_date(:EffectiveEndDate,'dd/MM/yyyy') <= to_date(EFFECTIVE_END_DATE,'dd/MM/yyyy')")
				.addEntity(GoodFeatureDiscount.class).setString("ModelCode", modelCode)
				.setString("StateCode", "%" + stateCode + "%").setString("EffectiveStartDate", effectiveStartDate)
				.setString("EffectiveEndDate", effectiveEndDate);
		List<GoodFeatureDiscount> lstGoodFeatureDiscount = query.list();
		if (!lstGoodFeatureDiscount.isEmpty()) {
			goodFeatureDiscount = lstGoodFeatureDiscount.get(0);
		}
		return goodFeatureDiscount;
	}

	@Transactional(readOnly = true)
	public ModelTppRatesMaster getModelTppRatesValues(String modelCode) throws Exception {

		ModelTppRatesMaster modelTppRatesMaster = null;
		Session session = sessionFactory.getCurrentSession();

		SimpleDateFormat objSdf = new SimpleDateFormat("dd/MM/yyyy");

		String effectiveStartDate = objSdf.format(new Date());
		String effectiveEndDate = objSdf.format(new Date());
		LOGGER.info("ModelTppRatesMaster - modelCode" + modelCode);
		LOGGER.info("ModelTppRatesMaster - effectiveStartDate" + effectiveStartDate);
		LOGGER.info("ModelTppRatesMaster - effectiveEndDate" + effectiveEndDate);
		try {

			Query query = session.getNamedQuery("ModelTppRatesMaster").setString("ModelCode", modelCode)
					.setString("EffectiveStartDate", effectiveStartDate)
					.setString("EffectiveEndDate", effectiveEndDate);

			List<ModelTppRatesMaster> lstModelTppRatesMaster = query.list();

			if (!lstModelTppRatesMaster.isEmpty()) {

				modelTppRatesMaster = lstModelTppRatesMaster.get(0);

			}

		} catch (Exception e) {

			LOGGER.info("Exception getModelTppRatesValues" + e);

		}

		return modelTppRatesMaster;
	}

	@Transactional(readOnly = true)
	public boolean checkQuotePurchasedStatus(String strQuoteId) throws Exception {
		LOGGER.info("CommonLookupDAO checkQuotePurchasedStatus 1");
		boolean quotebought = false;
		Session session = sessionFactory.getCurrentSession();
		String status = "";
		if (StringUtils.isBlank(strQuoteId))
			return quotebought;
		List query = session.createSQLQuery("select STATUS from D_QUOTE_DETAILS where QUOTE_ID= :quoteid")
				.setString("quoteid", strQuoteId).list();
		if (!query.isEmpty()) {
			status = (String) query.get(0).toString();
			if (status.trim().equalsIgnoreCase("B")) {
				quotebought = true;
			}
		} else {
			quotebought = false;
		}
		LOGGER.info("CommonLookupDAO checkQuotePurchasedStatus 2");
		return quotebought;
	}

	@Transactional(readOnly = true)
	public String getIFoundryAgentCodeAttribute(String agentId) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("select ifoundry_attribute from agent_master where agent_id=:agentId")
				.setParameter("agentId", agentId);
		if (query.list().isEmpty()) {
			return null;
		} else {
			return (String) query.list().get(0);
		}
	}

	@Transactional(readOnly = true)
	public void setQuoteExistingDB(MotorInsuranceModel motorInsuranceModel) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		DQuoteDetails dQuoteDetails = session.get(DQuoteDetails.class, motorInsuranceModel.getQuoteId());
		if (dQuoteDetails != null) {
			motorInsuranceModel.setQuoteExistingDB(true);
		}
	}

	@Transactional(readOnly = true)
	public boolean getStateValueForCity(String registrationCity) {

		Session session = sessionFactory.getCurrentSession();
		boolean status = false;
		try {
			session.clear();
			Query query = session
					.createQuery(
							"From CityStateMaster where REPLACE(upper(cityName), ' ', '') = REPLACE(upper(:city), ' ', '')")
					.setParameter("city", registrationCity);

			if (query.list().isEmpty()) {
				status = true;
			}

		} catch (Exception e) {
			LOGGER.info("Exception " + e.getMessage(), e);
		}
		LOGGER.info("status =" + status);
		return status;
	}

	@Transactional(readOnly = true)
	public DModifyidvRangeMaster getDModifyidvRangeMaster(String agentId, String product, String planName) {
		Session session = sessionFactory.getCurrentSession();
		DModifyidvRangeMaster dModifyidvRangeMaster = null;
		Query query = session
				.createQuery(
						"From DModifyidvRangeMaster where agentMaster.agentId=:agentId and lower(planMaster.planName)=lower(:planName) and lower(planMaster.planType)=lower(:product) ")
				.setParameter("agentId", agentId).setParameter("product", product).setParameter("planName", planName);

		List<DModifyidvRangeMaster> dModifyidvRangeMasterList = query.list();

		if (!dModifyidvRangeMasterList.isEmpty()) {
			dModifyidvRangeMaster = dModifyidvRangeMasterList.get(0);
		}

		return dModifyidvRangeMaster;
	}

	@Transactional
	public DQuoteDetails getQuoteDetails(String quoteId) {
		DQuoteDetails dQuoteDetails = new DQuoteDetails();
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM DQuoteDetails where quoteId=:quoteId").setParameter("quoteId",
					quoteId);
			List<DQuoteDetails> queryList = query.list();
			LOGGER.info("getQuoteDetails List Size ::: " + queryList.size());
			if (!queryList.isEmpty()) {
				LOGGER.info("Quote Data Exist");
				dQuoteDetails = queryList.get(0);
			}
		} catch (Exception e) {
			dQuoteDetails = new DQuoteDetails();
			LOGGER.info("Exception getQuoteDetails  :: " + e.getMessage(), e);
			return dQuoteDetails;
		}
		return dQuoteDetails;
	}

	public boolean saveOrUpdate(Object... objects) {
		Session session = sessionFactory.getCurrentSession();
		boolean status = false;
		try {
			for (Object object : objects) {
				session.saveOrUpdate(object);
			}
			session.flush();
			status = true;
		} catch (Exception e) {
			LOGGER.error("saveOrUpdate", e);
		}
		return status;
	}
	public boolean save(Object... objects) {
		Session session = sessionFactory.getCurrentSession();
		boolean status = false;
		try {
			for (Object object : objects) {
				session.save(object);
			}
			session.flush();
			status = true;
		} catch (Exception e) {
			LOGGER.error("save", e);
		}
		return status;
	}
	
	@Transactional
	public boolean delete(Serializable... objs) {
		try{
			Session session = sessionFactory.getCurrentSession();
			for(Object obj : objs){
				if( obj != null){
					session.delete(obj);
					session.flush();
				}
			}
			return true;
		}catch(Exception e){
			LOGGER.info(e.getMessage(), e);
			return false;
		}
	}
	public void saveDetails(DTpSurveyorUseDetails dTpSurveyorUseDetails, String request, String response) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Clob clob = Hibernate.getLobCreator(session).createClob(request);
			dTpSurveyorUseDetails.setRequest(clob);
			clob = response != null ? Hibernate.getLobCreator(session).createClob(response) : null;
			dTpSurveyorUseDetails.setResponse(clob);
			session.save(dTpSurveyorUseDetails);
			transaction.commit();
		} catch (Exception e) {
			LOGGER.info("While saving DTpSurveyorUseDetails got Exception::: " + e.getMessage(), e);
			if (transaction != null)
				transaction.rollback();
		} finally {
			if (session != null)
				session.close();
		}
	}

	public boolean getmailStatus(String quoteId) {
		LOGGER.info("quoteId =" + quoteId);
		boolean isMailSent = false;
		Session session = sessionFactory.getCurrentSession();
		try {
			List list = null;
			Query query = session
					.createSQLQuery(
							"SELECT * FROM MAILSTATUS where QUOTE_ID=:quoteId AND MAIL_TYPE = 'ProposalPDFMail' AND IS_MAIL_SENT = 'Yes'")
					.setString("quoteId", quoteId);
			list = query.list();
			if (list != null && !list.isEmpty()) {
				isMailSent = true;
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return isMailSent;
	}

	public String getStateForCity(String city) {
		LOGGER.info("daocity :: " + city);
		Session session = sessionFactory.getCurrentSession();
		String state = null;
		try {
			Query query = session
					.createSQLQuery("select STATE_NAME from CITY_STATE_MASTER" + " where upper(CITY_NAME)=upper(:city)")
					.setParameter("city", city);
			query.setMaxResults(1);
			state = (String) query.uniqueResult();
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
		return state;

	}

	public GstinStateMaster getGstinStateMaster(String contactState) {
		GstinStateMaster gstinStateMaster = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM GstinStateMaster where STATE_NAME =:contactState")
				.setParameter("contactState", contactState);
		List<GstinStateMaster> queryList = query.list();
		if (!queryList.isEmpty()) {
			LOGGER.info("State Exist");
			gstinStateMaster = queryList.get(0);
		}
		return gstinStateMaster;
	}

	public BigDecimal getAgentPlanMappingCampaign(String agentId) {
		LOGGER.info("AgentId for Campaign discount == " + agentId);
		Session session = sessionFactory.getCurrentSession();

		List<BigDecimal> list = session
				.createSQLQuery(
						"SELECT APM.ENABLE_CAMP_TECH_DISCOUNT FROM AGENT_PLAN_MAPPING APM WHERE APM.AGENT_ID=:agentId and plaN_TYPE ='TWOWHEELER'")
				.setParameter("agentId", agentId).list();
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			return new BigDecimal(0);
		}

	}

	public <T> T get(Class<T> clazz, Serializable id) {
		Session session = sessionFactory.getCurrentSession();
		T object = null;
		try {
			object = (T) session.get(clazz, id);
		} catch (Exception e) {
			LOGGER.error("get", e);
		}
		return object;
	}

	public BigDecimal getCampaignDiscountValue(String agentId) {
		LOGGER.info("AgentId for Campaign discount == " + agentId);
		Session session = sessionFactory.getCurrentSession();

		List<BigDecimal> list = session
				.createSQLQuery(
						"SELECT APM.CAMP_DISCOUNT_VALUE FROM AGENT_PLAN_MAPPING APM WHERE APM.AGENT_ID=:agentId and plaN_TYPE ='TWOWHEELER'")
				.setParameter("agentId", agentId).list();
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			return new BigDecimal(0);
		}

	}

	public boolean getDPolicyVehicleDetails(String engineNumber, String chassisNumber, String registrationNumber)
			throws ParseException {
		boolean validation = false;

		if (org.apache.commons.lang3.StringUtils.isBlank(engineNumber)
				&& org.apache.commons.lang3.StringUtils.isBlank(chassisNumber)
				&& org.apache.commons.lang3.StringUtils.isBlank(registrationNumber)) {
			return validation;
		}
		
		String engineNumberWithCase = StringUtils.isNotBlank(engineNumber) ? engineNumber.toUpperCase() : "";
        String chassisNumberWithCase = StringUtils.isNotBlank(chassisNumber) ? chassisNumber.toUpperCase() : "";
        String registrationNumberWithCase = StringUtils.isNotBlank(registrationNumber) ? registrationNumber.toUpperCase() : ""; 
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String date1 = dateFormat.format(new Date());
		LOGGER.info("date1::" + date1);
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("SELECT dppd.quote_id FROM d_policy_purchased_details dppd, "
						+ " d_policy_details dpd, d_policy_vehicle_details dpvd WHERE "
						+ "dppd.expiry_date >= TO_DATE (:date1, 'DD/MM/YYYY') AND dppd.status = 'B' "
						+ "AND dppd.quote_id = dpd.quote_id and dpd.ID = dpvd.policy_id "
						+ "and ( (dpvd.engine_number) = (:engineNumber) OR (dpvd.chassis_number) = (:chassisNumber) OR (dpvd.registration_number) = (:registrationNumber))")
				.setParameter("engineNumber", engineNumberWithCase).setParameter("chassisNumber", chassisNumberWithCase)
				.setParameter("registrationNumber", registrationNumberWithCase).setParameter("date1", date1);
		if (!query.list().isEmpty() && query.list().size() > 0) {
			LOGGER.info("Quote Data Exist");
			validation = true;
		}
		return validation;
	}

	public boolean getDPolicyVehicleDetails(String registrationNumber)
			throws ParseException {
		boolean validation = false;

		if (org.apache.commons.lang3.StringUtils.isBlank(registrationNumber)) {
			return validation;
		}
		

        String registrationNumberWithCase = StringUtils.isNotBlank(registrationNumber) ? registrationNumber.toUpperCase() : ""; 
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String date1 = dateFormat.format(new Date());
		LOGGER.info("date1::" + date1);
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("SELECT dppd.quote_id FROM d_policy_purchased_details dppd, "
						+ " d_policy_details dpd, d_policy_vehicle_details dpvd WHERE "
						+ "dppd.expiry_date >= TO_DATE (:date1, 'DD/MM/YYYY') AND dppd.status = 'B' "
						+ "AND dppd.quote_id = dpd.quote_id and dpd.ID = dpvd.policy_id "
						+ "and  (dpvd.registration_number) = (:registrationNumber)")
				
				.setParameter("registrationNumber", registrationNumberWithCase).setParameter("date1", date1);
		if (!query.list().isEmpty() && query.list().size() > 0) {
			LOGGER.info("Quote Data Exist");
			validation = true;
		}
		return validation;
	}
	
	public DPolicyDetails getDPolicyDetailsId(long id) {
		DPolicyDetails dPolicyDetailsId = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM DPolicyDetails where id =:id").setParameter("id", id);
		List<DPolicyDetails> queryList = query.list();
		if (!queryList.isEmpty()) {
			LOGGER.info("Quote Data Exist");
			dPolicyDetailsId = queryList.get(0);
		}
		return dPolicyDetailsId;
	}

	public DPolicyPurchasedDetails getDPolicyPurchasedDetailsQuoteId(String quoteId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			LOGGER.info("quoteId ::: " + quoteId);
			List<DPolicyPurchasedDetails> list = session
					.createQuery("From DPolicyPurchasedDetails where quoteId=:quoteId").setParameter("quoteId", quoteId)
					.list();
			if (list != null && !list.isEmpty()) {
				LOGGER.info("DPolicyPurchasedDetails list ::: " + list.size());
				return list.get(0);
			} else
				return null;

		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			return null;
		}
	}

	public DPolicyVehicleDetails getRegistrationNumber(String registrationNumber) {
		DPolicyVehicleDetails dPolicyVehicleDetails = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM DPolicyVehicleDetails where registrationNumber =:registrationNumber")
				.setParameter("registrationNumber", registrationNumber);
		List<DPolicyVehicleDetails> queryList = query.list();
		if (!queryList.isEmpty()) {
			LOGGER.info("Quote Data Exist");
			dPolicyVehicleDetails = queryList.get(0);
		}
		return dPolicyVehicleDetails;
	}

	public boolean getPreviousInsurer(String previousInsurerName) {
		// TODO Auto-generated method stub

		boolean previousInsurer = false;
		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.createSQLQuery(
					"SELECT * FROM PREVIOUS_INSURERS WHERE UPPER(VALUE) = upper('" + previousInsurerName + "')  ");
			if (query.list() != null && !query.list().isEmpty() && query.list().size() > 0) {
				previousInsurer = true;
			}
		} catch (HibernateException e) {
			previousInsurer = false;
			LOGGER.info("getpreviousInsurer exception:" + e);
		}
		LOGGER.info("previousInsurer = " + previousInsurer);
		return previousInsurer;
	}

	/**
	 * @author Paul [Paul Samuel V]
	 *
	 *         <b><**NOTE**</b> This should be refactored into the current
	 *         archetecture in future
	 *
	 *         Any logic which dictates whether a coverage should be included or
	 *         excluded should be included here, And should be called when
	 *         adding coverages into the IF request during IF request formation
	 *
	 * @param coverage
	 *            CoverageModel parsed directly from the db, unaltered
	 * @param motorInsuranceModel
	 *            Domain model parsed from service request
	 * @return whether the given cover is applicable to the particular quote is
	 *         question
	 */
	private boolean canIncludeCover(CoverageModel coverage, MotorInsuranceModel motorInsuranceModel) {
		// skip adding this "VMC_PAOwnerDriverCover" coverage, if
		// cpaCoverIsRequired is "No"
		if ("VMC_PAOwnerDriverCover".equalsIgnoreCase(coverage.getCoverageName())
				&& "No".equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())) {
			return false;
		}

		return true;
	}

	/**
	 * @author roshini
	 * @since 2019-03-08
	 * @param agentId
	 * @param product
	 * @param planName
	 * @return
	 *         <h3>Plan mapping for discount, deviation, etc.,</h3>
	 *         <p>
	 * 		Common details according to plan and product.
	 */
	public AgentPlanMapping getAgentPlanMapping(String agentId, String product, String planName) {
		AgentPlanMapping agentPlanMapping = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			if (StringUtils.isNotBlank(product) || StringUtils.isNotBlank(planName)) {
				LOGGER.info("agentId :::: " + agentId + ", product ::: " + product + ", planName :::: " + planName);
				Query query = session
						.createQuery(
								"From AgentPlanMapping where agentMaster.agentId=:agentId and ( :planName is null or lower(planMaster.planName)=lower(:planName) ) and ( :product is null or lower(planMaster.planType)=lower(:product) ) ")
						.setString("agentId", agentId).setString("product", product).setString("planName", planName);
				if (query.list() != null && !query.list().isEmpty() && query.list().get(0) != null) {
					agentPlanMapping = (AgentPlanMapping) query.list().get(0);
				}
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return agentPlanMapping;
	}
	public DPoscodeMaster getDPoscodeMaster(String posCode) {
		Session session =  sessionFactory.getCurrentSession();
		DPoscodeMaster posCodeMaster = null;
		try{
		List<DPoscodeMaster> list = session.createQuery("FROM DPoscodeMaster where posCode=:posCode")
				.setParameter("posCode", posCode).list();
		if(list.size()>0) {
			posCodeMaster=list.get(0);
			LOGGER.info("pos Mobile :::"+posCodeMaster.getPosMobile());
		}
		} catch(Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		
		return posCodeMaster;
	}

	
	private List<Productcoveragemaster> getODCoversOnly(List<Productcoveragemaster> lstCoverageModel) {
		List<Productcoveragemaster> standaloneCover = null;

		/*List<Productcoveragemaster> liabilityCoverList = lstCoverageModel.stream()
				.filter(cover -> !(MotorUtils.getLiabilityCovers().contains(cover.getCoveragename())))
				.collect(Collectors.toList());  */
		if(lstCoverageModel != null && CollectionUtils.isNotEmpty(lstCoverageModel) ) {
			standaloneCover = lstCoverageModel.stream()
											.filter(cover -> !(MotorUtils.getLiabilityCovers().contains(cover.getCoveragename())))
											.collect(Collectors.toList());  ;
		}
		return standaloneCover;
	}




	@Transactional
	public boolean validateApiKey(String apiKey, String agentId) {
		
		boolean checkAuth = false;
		
		LOGGER.info("validateApiKey - apikey4 :::" + apiKey +"::: agentId ::" + agentId);
		
		Session session = sessionFactory.getCurrentSession();
		LOGGER.info("apiKey :::" + apiKey + "AgentId" + agentId);
		try {

			session.clear();
			Query query = session
					.createSQLQuery("select CASE WHEN count(*)>0 THEN 'TRUE' ELSE 'FALSE' END as STATUS FROM D_SERVER_MASTER where SERVER_STATUS = 'Y' and API_KEY=:ApiKey and AGENT_ID=:AgentId")
					.setParameter("ApiKey", apiKey).setParameter("AgentId", agentId);

			List<String> validate = query.list();

			if("TRUE".equalsIgnoreCase(validate.get(0))) {
				
				checkAuth = true;
				
			} else {
				
				checkAuth = false;
			}
			
			LOGGER.info("checkAuth :::" +checkAuth);
			
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		
		
		return checkAuth;
	}
	
	public CityStateLookupModel getCityState(String city) {
		Session session = sessionFactory.getCurrentSession();
		LOGGER.info("city: " + city);
		// Bug #225251 IDV is not fetching.
		// Since citynames with commas are replaced with hyphen doing vice versa
		// to pass it to DB
		city = city.replace("-", ",");
		/*******/
		CityStateLookupModel cityStateLookupModel = null;
		try {
			Query query = session.getNamedQuery("GetCityState").setString("city", city);
			List<CityStateLookupModel> resultSets = query.list();

			if (!resultSets.isEmpty()) {
				cityStateLookupModel = resultSets.get(0);
				LOGGER.info("Region: "+cityStateLookupModel.getRegion());
			}
		} catch (Exception e) {
			LOGGER.error("CityStateLookupModel exception:", e);
		}
		return cityStateLookupModel;
	}

	public String getVehicleClassification(String vehicleModelCode, String vehicleType) {
		String bikeOrScooter = "";

		Session session = sessionFactory.getCurrentSession();
		try {
			Query query = session.getNamedQuery("GetVehicleClassification").setString("modelCode", vehicleModelCode)
					.setString("vehicleType", vehicleType);
			List<VehicleClassification> resultSets = query.list();

			if (!resultSets.isEmpty() && resultSets.get(0) != null && resultSets.get(0) != null) {
				bikeOrScooter = resultSets.get(0).getBikeOrScooter();
				LOGGER.info("BikeOrScooter : " + bikeOrScooter);
			}
		} catch (Exception e) {
			LOGGER.error("VehicleClassification Error : " + e.getMessage(), e);
		}
		return bikeOrScooter;
	}

	public boolean allowTPPolicyByCityOrRegion(String agentId, String productName, String businessType, String bikeOrScooter,
			String vehicleRegisteredCityOrRegion, boolean isCity) {
		boolean allowTpPolicy = false;
		LOGGER.info("agentId : "+agentId);
		LOGGER.info("bikeOrScooter : "+bikeOrScooter);
		LOGGER.info("isCity : "+isCity);
		LOGGER.info("productName : "+productName);
		Session session = sessionFactory.getCurrentSession();
		try{
			Query query = null;
			if(isCity){
				query = session.createSQLQuery("SELECT * FROM TP_ONLY_POLICY_AGENT_MASTER  WHERE upper(AGENT_ID) = upper(:agentId) and "
						+ " upper(PRODUCT_NAME)  like upper(:productName) and upper(BUSINESS_TYPE) =upper(:businessType) and "
						+ " upper(TYPE_OF_VEHICLE) =upper(:bikeOrScooter) and upper(ALLOWED_CITY)=upper(:vehicleRegisteredCity)")
						.setParameter("agentId", agentId)
						.setParameter("productName", "%"+productName+"%")
						.setParameter("businessType", businessType)
						.setParameter("bikeOrScooter", bikeOrScooter)
						.setParameter("vehicleRegisteredCity", vehicleRegisteredCityOrRegion.trim());
			} else {
				query = session.createSQLQuery("SELECT * FROM TP_ONLY_POLICY_AGENT_MASTER WHERE upper(AGENT_ID) = upper(:agentId) and "
						+ " upper(PRODUCT_NAME) like upper(:productName) and upper(BUSINESS_TYPE) =upper(:businessType) and "
						+ " upper(TYPE_OF_VEHICLE) = upper(:bikeOrScooter) and upper(ALLOWED_REGION)=upper(:region)")
						.setParameter("agentId", agentId)
						.setParameter("productName", "%"+productName+"%")
						.setParameter("businessType", businessType)
						.setParameter("bikeOrScooter", bikeOrScooter)
						.setParameter("region", vehicleRegisteredCityOrRegion.trim());
			}
				
			if(CollectionUtils.isNotEmpty(query.list()) && query.list().size() > 0&& query.list().get(0) != null ){
				allowTpPolicy = true;
			}
		}catch(Exception e){
			LOGGER.error("Allow TPPolicy By City or Region: "+e.getMessage(), e);
		}
		
		return allowTpPolicy;
	}

	public boolean getAgentForTPPolicy(String agentId, String bikeOrScooter) {
		
		boolean agentAvail = false;
		Session session = sessionFactory.getCurrentSession();
		try{
			Query query = session.createSQLQuery("SELECT AGENT_ID FROM TP_ONLY_POLICY_AGENT_MASTER"
					+ "  WHERE upper(TYPE_OF_VEHICLE) =upper(:bikeOrScooter) AND upper(AGENT_ID) =upper(:agentId) GROUP BY AGENT_ID")
						.setParameter("agentId", agentId)
						.setParameter("bikeOrScooter", bikeOrScooter);
				
			if(CollectionUtils.isNotEmpty(query.list()) && query.list().size() > 0&& query.list().get(0) != null ){
				agentAvail = true;
			}
			LOGGER.info("agentAvail for TP Policy is "+agentAvail);
		}catch(Exception e){
			LOGGER.error("Allow agentAvail : "+e.getMessage(), e);
		}
		return agentAvail;
	}

	
	
    public List<Map<String, String>> getVahanServicePlan(String productName,String agentId){
		Session session =sessionFactory.getCurrentSession();
		List<Map<String, String>> list = session.createSQLQuery("SELECT AGENT_ID,PRODUCT_NAME, SUBLINE, BUSINESS_TYPE, SERVICE_CALL_AVAIL, VAHAN_SERVICE_TYPE, SHOW_SUCCESS, SHOW_FAILURE, FAIL_FLOW_BLOCK FROM D_VAHAN_SERVICE_PLAN where AGENT_ID=:AGENT_ID")
				.setString("AGENT_ID",agentId)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
    
    public List<Map<String, String>> getVahanResponseMaster(String ENGINE_NUMBER,String CHASSIS_NUMBER){
		Session session =sessionFactory.getCurrentSession();
		List<Map<String, String>> list = session.createSQLQuery("SELECT ID, PRODUCT_NAME, SUBLIME, BUSINESS_TYPE, MAKER_NAME, MODEL_NAME, ENGINE_NUMBER, CHASSIS_NUMBER, REGISTRATION_NUMBER" + 
				" FROM VAHAN_RESPONSE_MASTER where ENGINE_NUMBER=:ENGINE_NUMBER and CHASSIS_NUMBER=:CHASSIS_NUMBER")
				.setString("ENGINE_NUMBER",ENGINE_NUMBER)
				.setString("CHASSIS_NUMBER",CHASSIS_NUMBER)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	public List<Map<String, String>> getVahanResponseMasterRN(String ENGINE_NUMBER,String CHASSIS_NUMBER,String REGISTRATION_NUMBER){
		Session session =sessionFactory.getCurrentSession();
		List<Map<String, String>> list = session.createSQLQuery("SELECT ID, PRODUCT_NAME, SUBLIME, BUSINESS_TYPE, MAKER_NAME, MODEL_NAME, ENGINE_NUMBER, CHASSIS_NUMBER, REGISTRATION_NUMBER" + 
				" FROM VAHAN_RESPONSE_MASTER where ENGINE_NUMBER=:ENGINE_NUMBER and CHASSIS_NUMBER=:CHASSIS_NUMBER and REGISTRATION_NUMBER=:REGISTRATION_NUMBER")
				.setString("ENGINE_NUMBER",ENGINE_NUMBER)
				.setString("CHASSIS_NUMBER",CHASSIS_NUMBER)
				.setString("REGISTRATION_NUMBER",REGISTRATION_NUMBER)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	public String getNomineeCodeSequence(String agentId) {
		
		Session session = sessionFactory.getCurrentSession();
		String clientCode = null;
		try{
			DNomineeClientcodeSequence dNomineeClientcodeSequence = (DNomineeClientcodeSequence) session.get(DNomineeClientcodeSequence.class, agentId.toUpperCase());
			if( dNomineeClientcodeSequence!= null ){
				StringBuilder clientCodeBuilder= new StringBuilder();
				clientCodeBuilder.append(dNomineeClientcodeSequence.getSequence());
				Query query = session.createSQLQuery("SELECT NOMINEE_XGEN_SEQ.NEXTVAL FROM DUAL");
				BigDecimal clientId = (BigDecimal) query.uniqueResult();
				clientCodeBuilder.append(clientId);
				clientCode = clientCodeBuilder.toString();
			}
		}catch(Exception e){
			LOGGER.error("generateClientCode", e);
		}
		LOGGER.info("clientCode :::" +clientCode);
		return clientCode; 
	
	}
	
	@Transactional
	public DUpdatedClientDetails getDUpdatedClientDetails(String quoteId) {
		DUpdatedClientDetails dUpdatedClientDetails = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("FROM DUpdatedClientDetails where quoteId=:quoteId").setParameter("quoteId",
					quoteId);
			List<DUpdatedClientDetails> queryList = query.list();
			if (queryList != null && !queryList.isEmpty()) {
				LOGGER.info("Quote Data Exist");
				dUpdatedClientDetails = queryList.get(0);
			}
		} catch (Exception e) {
			LOGGER.error("Exception getQuoteDetails  :: " + e.getMessage(), e);
		}
		return dUpdatedClientDetails;
	}

	public Integer setInsetData(DQuoteDetails dQuoteDetails, DPolicyDetails dPolicyDetails,
			DPolicyVehicleDetails dPolicyVehicleDetails, DUpdatedClientDetails updatedClientDetails,
			DPoscodeDetails dPoscodeDetails) {
		Integer returnCode = 1;
		Session session = sessionFactory.getCurrentSession();
		try {
		LOGGER.info("****************Updating Data****************************");
		session.update(dQuoteDetails);
		session.update(dPolicyDetails);
		session.update(dPolicyVehicleDetails);
		session.save(updatedClientDetails);
		
		if (dPoscodeDetails != null) {
			session.saveOrUpdate(dPoscodeDetails);
		}
		session.flush();
		session.clear();
		}catch(Exception e) {
			returnCode = 0;
			LOGGER.info("Data updation failed : "+e.getMessage(), e);
		}
		LOGGER.info("****************Data Updated****************************");
		return returnCode;
	}

}
