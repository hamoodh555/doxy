package com.xerago.rsa.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.valuemomentum.plm.parser.Attributes;
import com.valuemomentum.plm.parser.Coverage;
import com.valuemomentum.plm.parser.Deductibles;
import com.valuemomentum.plm.parser.Limits;
import com.valuemomentum.plm.parser.Product;
import com.xerago.rsa.model.MotorInsuranceModel;
import com.xerago.rsa.util.Constants;
import com.xerago.rsa.util.GenericUtils;

public class Ifoundry  implements Constants {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	public static void setSelectedLimitValues(Product objProduct, MotorInsuranceModel motorInsuranceModel, String quickProcess) {
		try {

			List<Coverage> lstCoverages = objProduct.getCoverages();

			if (lstCoverages != null && lstCoverages.size() > 0) {
				for (Coverage objCoverage : lstCoverages) {
					String strSelectedLimitValue = "";
					String coverageName = objCoverage.getCoverageName();
					List<Coverage> lstSubCoverages = objCoverage.getSubCoverages();
					if (lstSubCoverages != null && lstSubCoverages.size() > 0) {
						for (Coverage objSubCoverage : lstSubCoverages) {
							LOGGER.info(objSubCoverage.getCoverageName() + "	" + objSubCoverage.getIsMandatory() + "		"
									+ objSubCoverage.isSelected());
							if ("VMC_ODBasicCover".equalsIgnoreCase(objSubCoverage.getCoverageName())) {
								strSelectedLimitValue = GenericUtils.checkString(motorInsuranceModel.getVoluntarydeductible());
								if ("".equalsIgnoreCase(GenericUtils.checkString(strSelectedLimitValue))) {
									strSelectedLimitValue = "0";
								}
								List<Deductibles> lstDeductibles = objSubCoverage.getDeductibles();
								if (lstDeductibles != null && lstDeductibles.size() > 0) {
									for (Deductibles objDeductibles : lstDeductibles) {
										objDeductibles.setSelectedDeductibleValue(Double.parseDouble(strSelectedLimitValue));
										objSubCoverage.setSelected(true);
									}
								}
							}
							strSelectedLimitValue = "0";

						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception raised is ", e);
		}
	}
	
	public static void setSelectedLimitValues(Product objProduct, MotorInsuranceModel motorInsuranceModel) {
		try {

			List<Coverage> lstCoverages = objProduct.getCoverages();

			if (lstCoverages != null && lstCoverages.size() > 0) {
				for (Coverage objCoverage : lstCoverages) {
					String strSelectedLimitValue = "";
					String strSelectedLimitValueforYear2 = "0.0";
					String strSelectedLimitValueforYear3 = "0.0";
					String strSelectedLimitValueforYear4 = "0.0";
					String strSelectedLimitValueforYear5 = "0.0";
					
					String coverageName = objCoverage.getCoverageName();

					LOGGER.info("coverageName ::: " + coverageName + "		" + objCoverage.getIsMandatory()
							+ "		" + objCoverage.isSelected());



					if ("VMC_PAUnnamed".equalsIgnoreCase(coverageName)) {
						strSelectedLimitValue = motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengers();
						strSelectedLimitValueforYear2 = motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengersforyear2();
						strSelectedLimitValueforYear3 = motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengersforyear3();
						strSelectedLimitValueforYear4 =  motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengersforyear4();
						strSelectedLimitValueforYear5 =  motorInsuranceModel.getPersonalaccidentcoverforunnamedpassengersforyear5();
					} else if ("VMC_PAPaidDriver".equalsIgnoreCase(coverageName)) {
						strSelectedLimitValue = motorInsuranceModel.getAccidentcoverforpaiddriver();
						strSelectedLimitValueforYear2 = motorInsuranceModel.getAccidentcoverforpaiddriverforyear2();
						strSelectedLimitValueforYear3 = motorInsuranceModel.getAccidentcoverforpaiddriverforyear3();
						strSelectedLimitValueforYear4 =   motorInsuranceModel.getAccidentcoverforpaiddriverforyear4();
						strSelectedLimitValueforYear5 =   motorInsuranceModel.getAccidentcoverforpaiddriverforyear5();
					} else if ("VMC_PAOwnerDriverCover".equalsIgnoreCase(coverageName)) {
						LOGGER.info("VMC_PAOwnerDriverCover ::: " + coverageName);
						LOGGER.info("motorInsuranceModel.getPaToOwnerDriver() = "+motorInsuranceModel.getPaToOwnerDriver());
						if (YES.equalsIgnoreCase(motorInsuranceModel.getPaToOwnerDriver())) {
							objCoverage.setSelected(true);
							continue;
						} else {
							objCoverage.setSelected(false);
							continue;
						}
					} else if ("VMC_LLEMPLOYEES".equalsIgnoreCase(coverageName)) {
						LOGGER.info("VMC_LLEMPLOYEES ::: " + VMC_LLEMPLOYEES.equalsIgnoreCase(coverageName)
						+motorInsuranceModel.getLegalliabilitytoemployees());
						if (YES.equalsIgnoreCase(motorInsuranceModel.getLegalliabilitytoemployees())) {
							objCoverage.setSelected(true);
							continue;
						} else {
							objCoverage.setSelected(false);
							continue;
						}
					} else if ("VMC_LLPaidDriverCover".equalsIgnoreCase(coverageName)) {
						LOGGER.info("VMC_LLPaidDriverCover ::: ");
						if (YES.equalsIgnoreCase(motorInsuranceModel.getLegalliabilitytopaiddriver())) {
							objCoverage.setSelected(true);
							continue;
						} else {
							objCoverage.setSelected(false);
							continue;
						}
					}

					if ("".equalsIgnoreCase(GenericUtils.checkString(strSelectedLimitValue))) {
						strSelectedLimitValue = "0";
					}
					if (!objCoverage.getIsMandatory()) {
						List<Limits> lstLimits = objCoverage.getLimits();
						if (lstLimits != null && lstLimits.size() > 0) {
							for (Limits objLimits : lstLimits) {
								LOGGER.info("objCoverage objLimits.getLimitName() ::: " + objLimits.getLimitName());
								objLimits.setSelectedLimitValue(Double.parseDouble(strSelectedLimitValue));
								if (Double.parseDouble(strSelectedLimitValue) > 0) {
									objCoverage.setSelected(true);
								} else {
									objCoverage.setSelected(false);
								}

								if (objLimits.getLimitName().contains("2ndYear")) {
									objLimits.setSelectedLimitValue(Double.parseDouble(strSelectedLimitValueforYear2));
								}
								if (objLimits.getLimitName().contains("3rdYear")) {
									objLimits.setSelectedLimitValue(Double.parseDouble(strSelectedLimitValueforYear3));
								}
								if (objLimits.getLimitName().contains("4thYear")) {
									objLimits.setSelectedLimitValue(Double.parseDouble(strSelectedLimitValueforYear4));
								}
								if (objLimits.getLimitName().contains("5thYear")) {
									objLimits.setSelectedLimitValue(Double.parseDouble(strSelectedLimitValueforYear5));
								}
							}
						}
					}

					List<Coverage> lstSubCoverages = objCoverage.getSubCoverages();
					if (lstSubCoverages != null && lstSubCoverages.size() > 0) {
						strSelectedLimitValueforYear2 = "0";
						strSelectedLimitValueforYear3 = "0";
						strSelectedLimitValueforYear4 = "0";
						strSelectedLimitValueforYear5 = "0";
						for (Coverage objSubCoverage : lstSubCoverages) {
							LOGGER.info("objSubCoverage ::: " + objSubCoverage.getCoverageName() + "	"
									+ objSubCoverage.getIsMandatory() + "		" + objSubCoverage.isSelected());
							if ("VMC_ElecAccessoriesCover".equalsIgnoreCase(objSubCoverage.getCoverageName())) {
								LOGGER.info("Electrical Accessories = "
										+ motorInsuranceModel.getValueofelectricalaccessories());
								strSelectedLimitValue = motorInsuranceModel.getValueofelectricalaccessories();
								LOGGER.info("Selected limited value = " + strSelectedLimitValue);

								strSelectedLimitValueforYear2 = motorInsuranceModel
										.getValueofelectricalaccessoriesforyear2();
								strSelectedLimitValueforYear3 = motorInsuranceModel
										.getValueofelectricalaccessoriesforyear3();
								LOGGER.info("VMC_ElecAccessoriesCover strSelectedLimitValueforYear2 ::: "
										+ strSelectedLimitValueforYear2);
								LOGGER.info("VMC_ElecAccessoriesCover strSelectedLimitValueforYear3 ::: "
										+ strSelectedLimitValueforYear3);
								strSelectedLimitValueforYear4 = motorInsuranceModel.getValueofelectricalaccessoriesforyear4();
								strSelectedLimitValueforYear5 = motorInsuranceModel.getValueofelectricalaccessoriesforyear5();
							} else if ("VMC_NonElecAccessoriesCover"
									.equalsIgnoreCase(objSubCoverage.getCoverageName())) {
								LOGGER.info("motorInsuranceModel.getValueofnonelectricalaccessories() ::: "
										+ motorInsuranceModel.getValueofnonelectricalaccessories());
								strSelectedLimitValue = motorInsuranceModel.getValueofnonelectricalaccessories();

								strSelectedLimitValueforYear2 = motorInsuranceModel
										.getValueofnonelectricalaccessoriesforyear2();
								strSelectedLimitValueforYear3 = motorInsuranceModel
										.getValueofnonelectricalaccessoriesforyear3();
								LOGGER.info("VMC_NonElecAccessoriesCover strSelectedLimitValueforYear2 ::: "
										+ strSelectedLimitValueforYear2);
								LOGGER.info("VMC_NonElecAccessoriesCover strSelectedLimitValueforYear3 ::: "
										+ strSelectedLimitValueforYear3);
								strSelectedLimitValueforYear4 = motorInsuranceModel.getValueofnonelectricalaccessoriesforyear4();
								strSelectedLimitValueforYear5 = motorInsuranceModel.getValueofnonelectricalaccessoriesforyear5();
							} else if ("VMC_ODBasicCover".equalsIgnoreCase(objSubCoverage.getCoverageName())) {
								strSelectedLimitValue = GenericUtils
										.checkString(motorInsuranceModel.getVoluntarydeductible());
								if ("".equalsIgnoreCase(GenericUtils.checkString(strSelectedLimitValue))) {
									strSelectedLimitValue = "0";
								}
								List<Deductibles> lstDeductibles = objSubCoverage.getDeductibles();
								if (lstDeductibles != null && lstDeductibles.size() > 0) {
									for (Deductibles objDeductibles : lstDeductibles) {
										objDeductibles
												.setSelectedDeductibleValue(Double.parseDouble(strSelectedLimitValue));
										objSubCoverage.setSelected(true);
									}
								}
							} else if ("AntiTheftDevice".equalsIgnoreCase(objSubCoverage.getCoverageName())) {
								if (TRUE.equalsIgnoreCase(motorInsuranceModel.getCarFittedWithAntiTheftDevice())) {
									objSubCoverage.setSelected(true);
								} else {
									objSubCoverage.setSelected(false);
								}
							} else if ("AdditionalTowingChargesCover"
									.equalsIgnoreCase(objSubCoverage.getCoverageName())) {
								strSelectedLimitValue = GenericUtils
										.checkString(motorInsuranceModel.getAdditionalTowingChargesCover());
								strSelectedLimitValueforYear2 = motorInsuranceModel
										.getAdditionalTowingChargesCoverforyear2();
								strSelectedLimitValueforYear3 = motorInsuranceModel
										.getAdditionalTowingChargesCoverforyear3();
								strSelectedLimitValueforYear4 = motorInsuranceModel
										.getAdditionalTowingChargesCoverforyear4();
								strSelectedLimitValueforYear5 = motorInsuranceModel
										.getAdditionalTowingChargesCoverforyear5();
							} else {
				                LOGGER.info("If no implementations found on this " + objSubCoverage.getCoverageName()
			                    + ", change the strSelectedLimitValue to '' because strSelectedLimitValue variable taking prevoius coverage limit value.");
				                strSelectedLimitValue = "";
			              }

							if (StringUtils.isBlank(GenericUtils.checkString(strSelectedLimitValue))) {
								strSelectedLimitValue = "0";
							}
							if (!objSubCoverage.getIsMandatory()) {
								List<Limits> lstLimits = objSubCoverage.getLimits();
								if (lstLimits != null && lstLimits.size() > 0) {

									for (Limits objLimits : lstLimits) {
										LOGGER.info("objSubCoverage objLimits.getLimitName() ::: "
												+ objLimits.getLimitName() + " ::: strSelectedLimitValue ::: "
												+ strSelectedLimitValue);
										objLimits.setSelectedLimitValue(Double.parseDouble(strSelectedLimitValue));
										if (Double.parseDouble(strSelectedLimitValue) > 0) {
											objSubCoverage.setSelected(true);
										} else {
											objSubCoverage.setSelected(false);
										}

										if (objLimits.getLimitName().contains("2ndYear")) {
											objLimits.setSelectedLimitValue(
													Double.parseDouble(strSelectedLimitValueforYear2));
										}
										if (objLimits.getLimitName().contains("3rdYear")) {
											objLimits.setSelectedLimitValue(
													Double.parseDouble(strSelectedLimitValueforYear3));
										}
										if (objLimits.getLimitName().contains("4thYear")) {
											objLimits.setSelectedLimitValue(
													Double.parseDouble(strSelectedLimitValueforYear4));
										}
										if(objLimits.getLimitName().contains("5thYear")) {
											objLimits.setSelectedLimitValue(
													Double.parseDouble(strSelectedLimitValueforYear5));
										}

									}
								}
							}
							if (!objSubCoverage.getIsMandatory()) {
								List<Deductibles> lstDeductibles = objSubCoverage.getDeductibles();
								if (lstDeductibles != null && lstDeductibles.size() > 0) {
									for (Deductibles objDeductibles : lstDeductibles) {
										objDeductibles
												.setSelectedDeductibleValue(Double.parseDouble(strSelectedLimitValue));
										if (Double.parseDouble(strSelectedLimitValue) > 0) {
											objSubCoverage.setSelected(true);
										} else {
											objSubCoverage.setSelected(false);
										}

									}
								}
							}
						}
					}
				}
			}
			
			} catch (Exception e) {
			LOGGER.error("Exception raised is ", e);
		}
	}
	
	public static void setlstCoverages(Product objProduct, MotorInsuranceModel motorInsuranceModel) {
		try {

			List<Coverage> lstCoverages = objProduct.getCoverages();
			if (lstCoverages != null && lstCoverages.size() > 0) {
				LOGGER.info("setlstCoverages ***** motorInsuranceModel.getVehicleRegisteredInTheNameOf()"
						+ motorInsuranceModel.getVehicleRegisteredInTheNameOf());
				for (Coverage objCoverage : lstCoverages) {
					LOGGER.info("objCoverage.getCoverageName() ::: " + objCoverage.getCoverageName() +objCoverage.isSelected());
					if (INDIVIDUAL.equalsIgnoreCase(motorInsuranceModel.getVehicleRegisteredInTheNameOf())) {
						if ("VMC_PAOwnerDriverCover".equalsIgnoreCase(objCoverage.getCoverageName())
								&& "YES".equalsIgnoreCase(motorInsuranceModel.getCpaCoverIsRequired())) {
							objCoverage.setSelected(true);
							objCoverage.setMandatory(true);
							continue;
						} else if ("VMC_PAOwnerDriverCover".equalsIgnoreCase(objCoverage.getCoverageName())) {
							objCoverage.setSelected(false);
							objCoverage.setMandatory(false);
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception raised is ", e);
		}
		motorInsuranceModel.setProduct(objProduct);
	}
}
