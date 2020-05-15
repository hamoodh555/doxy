package com.xerago.rsa.result;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "OD_PREMIUM")
@XmlType (propOrder={"BASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES","ELECTRICAL_ACCESSORIES","BI_FUEL_KIT","FIBER_GLASS_TANK","AUTOMOBILE_ASSOCIATION_DISCOUNT","VOLUNTARY_DEDUCTABLES","VOLUNTARY_DEDUCTABLE","NO_CLAIM_BONUS","DEPRECIATION_WAIVER","ENGINE_PROTECTOR","NCB_PROTECTOR","WIND_SHIELD_GLASS","LIFE_TIME_ROAD_TAX","SPARE_CAR","INVOICE_PRICE_INSURANCE","LOSS_OF_BAGGAGE","TOTAL_OD_PREMIUM","KEY_REPLACEMENT", "nilIntermediationCoverPremium", "bulkDealDiscountCoverPremium", "additionalTowingChargesCoverPremium"})
public class OdPremium {
	private String BASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES;
	private String ELECTRICAL_ACCESSORIES;
	private String BI_FUEL_KIT;
	private String FIBER_GLASS_TANK;
	private String AUTOMOBILE_ASSOCIATION_DISCOUNT;
	private String VOLUNTARY_DEDUCTABLES;
	private String VOLUNTARY_DEDUCTABLE;
	private String NO_CLAIM_BONUS;
	private String DEPRECIATION_WAIVER;
	private String ENGINE_PROTECTOR;
	private String NCB_PROTECTOR;
	private String KEY_REPLACEMENT;
	private String WIND_SHIELD_GLASS;
	private String LIFE_TIME_ROAD_TAX;
	private String SPARE_CAR;
	private String INVOICE_PRICE_INSURANCE;
	private String LOSS_OF_BAGGAGE;
	private String TOTAL_OD_PREMIUM;
	
	private String nilIntermediationCoverPremium;
	private String bulkDealDiscountCoverPremium;
	private String additionalTowingChargesCoverPremium;

	public String getAUTOMOBILE_ASSOCIATION_DISCOUNT() {
		return AUTOMOBILE_ASSOCIATION_DISCOUNT;
	}

	public String getBASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES() {
		return BASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES;
	}

	public String getBI_FUEL_KIT() {
		return BI_FUEL_KIT;
	}

	public String getDEPRECIATION_WAIVER() {
		return DEPRECIATION_WAIVER;
	}

	public String getELECTRICAL_ACCESSORIES() {
		return ELECTRICAL_ACCESSORIES;
	}

	public String getENGINE_PROTECTOR() {
		return ENGINE_PROTECTOR;
	}

	public String getFIBER_GLASS_TANK() {
		return FIBER_GLASS_TANK;
	}

	public String getINVOICE_PRICE_INSURANCE() {
		return INVOICE_PRICE_INSURANCE;
	}

	public String getLIFE_TIME_ROAD_TAX() {
		return LIFE_TIME_ROAD_TAX;
	}

	public String getLOSS_OF_BAGGAGE() {
		return LOSS_OF_BAGGAGE;
	}

	public String getNCB_PROTECTOR() {
		return NCB_PROTECTOR;
	}

	public String getNO_CLAIM_BONUS() {
		return NO_CLAIM_BONUS;
	}

	public String getSPARE_CAR() {
		return SPARE_CAR;
	}

	public String getTOTAL_OD_PREMIUM() {
		return TOTAL_OD_PREMIUM;
	}

	public String getVOLUNTARY_DEDUCTABLE() {
		return VOLUNTARY_DEDUCTABLE;
	}

	public String getVOLUNTARY_DEDUCTABLES() {
		return VOLUNTARY_DEDUCTABLES;
	}

	public String getWIND_SHIELD_GLASS() {
		return WIND_SHIELD_GLASS;
	}

	public void setAUTOMOBILE_ASSOCIATION_DISCOUNT(String aUTOMOBILE_ASSOCIATION_DISCOUNT) {
		AUTOMOBILE_ASSOCIATION_DISCOUNT = aUTOMOBILE_ASSOCIATION_DISCOUNT;
	}

	public void setBASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES(String bASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES) {
		BASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES = bASIC_PREMIUM_AND_NON_ELECTRICAL_ACCESSORIES;
	}

	public void setBI_FUEL_KIT(String bI_FUEL_KIT) {
		BI_FUEL_KIT = bI_FUEL_KIT;
	}

	public void setDEPRECIATION_WAIVER(String dEPRECIATION_WAIVER) {
		DEPRECIATION_WAIVER = dEPRECIATION_WAIVER;
	}

	public void setELECTRICAL_ACCESSORIES(String eLECTRICAL_ACCESSORIES) {
		ELECTRICAL_ACCESSORIES = eLECTRICAL_ACCESSORIES;
	}

	public void setENGINE_PROTECTOR(String eNGINE_PROTECTOR) {
		ENGINE_PROTECTOR = eNGINE_PROTECTOR;
	}

	public void setFIBER_GLASS_TANK(String fIBER_GLASS_TANK) {
		FIBER_GLASS_TANK = fIBER_GLASS_TANK;
	}

	public void setINVOICE_PRICE_INSURANCE(String iNVOICE_PRICE_INSURANCE) {
		INVOICE_PRICE_INSURANCE = iNVOICE_PRICE_INSURANCE;
	}

	public void setLIFE_TIME_ROAD_TAX(String lIFE_TIME_ROAD_TAX) {
		LIFE_TIME_ROAD_TAX = lIFE_TIME_ROAD_TAX;
	}

	public void setLOSS_OF_BAGGAGE(String lOSS_OF_BAGGAGE) {
		LOSS_OF_BAGGAGE = lOSS_OF_BAGGAGE;
	}

	public void setNCB_PROTECTOR(String nCB_PROTECTOR) {
		NCB_PROTECTOR = nCB_PROTECTOR;
	}

	public void setNO_CLAIM_BONUS(String nO_CLAIM_BONUS) {
		NO_CLAIM_BONUS = nO_CLAIM_BONUS;
	}

	public void setSPARE_CAR(String sPARE_CAR) {
		SPARE_CAR = sPARE_CAR;
	}

	public void setTOTAL_OD_PREMIUM(String tOTAL_OD_PREMIUM) {
		TOTAL_OD_PREMIUM = tOTAL_OD_PREMIUM;
	}

	public void setVOLUNTARY_DEDUCTABLE(String vOLUNTARY_DEDUCTABLE) {
		VOLUNTARY_DEDUCTABLE = vOLUNTARY_DEDUCTABLE;
	}

	public void setVOLUNTARY_DEDUCTABLES(String vOLUNTARY_DEDUCTABLES) {
		VOLUNTARY_DEDUCTABLES = vOLUNTARY_DEDUCTABLES;
	}

	public void setWIND_SHIELD_GLASS(String wIND_SHIELD_GLASS) {
		WIND_SHIELD_GLASS = wIND_SHIELD_GLASS;
	}

	public String getKEY_REPLACEMENT() {
		return KEY_REPLACEMENT;
	}

	public void setKEY_REPLACEMENT(String kEY_REPLACEMENT) {
		KEY_REPLACEMENT = kEY_REPLACEMENT;
	}

	@XmlElement(name = "NIL_INTERMEDIATION_COVER_PREMIUM")
	public String getNilIntermediationCoverPremium() {
		return nilIntermediationCoverPremium;
	}

	public void setNilIntermediationCoverPremium(String nilIntermediationCoverPremium) {
		this.nilIntermediationCoverPremium = nilIntermediationCoverPremium;
	}

	@XmlElement(name = "BULKDEAL_DISCOUNT_COVER_PREMIUM")
	public String getBulkDealDiscountCoverPremium() {
		return bulkDealDiscountCoverPremium;
	}

	public void setBulkDealDiscountCoverPremium(String bulkDealDiscountCoverPremium) {
		this.bulkDealDiscountCoverPremium = bulkDealDiscountCoverPremium;
	}

	@XmlElement(name = "ADDITIONAL_TOWING_CHARGES_COVER_PREMIUM")
	public String getAdditionalTowingChargesCoverPremium() {
		return additionalTowingChargesCoverPremium;
	}

	public void setAdditionalTowingChargesCoverPremium(String additionalTowingChargesCoverPremium) {
		this.additionalTowingChargesCoverPremium = additionalTowingChargesCoverPremium;
	}

}
