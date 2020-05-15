package com.xerago.rsa.model;

import java.io.Serializable;

public class TppPricingValuesModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7053653436562876095L;
	private String VPC_ODPremiumA_rate;
	private String VPC_ODPremiumB_rate;
	private String VPC_ODPremiumC_rate;
	private String VPC_ODIDVAdjustor_rate;
	private String VPC_ODLeapofFaith_rate;
	private String VPC_ODStreetPrice_rate;
	private String VPC_ODPremiumA;
	private String VPC_ODPremiumB;
	private String VPC_ODPremiumC;
	private String VPC_ODIDVAdjustor;
	private String VPC_ODLeapofFaith;
	private String VPC_ODStreetPrice;
	private String VPC_TheftPremiumA_rate;
	private String VPC_TheftPremiumB_rate;
	private String VPC_TheftPremiumC_rate;
	private String VPC_TheftIDVAdjustor_rate;
	private String VPC_TheftLeapofFaith_rate;
	private String VPC_TheftStreetPrice_rate;
	private String VPC_TheftPremiumA;
	private String VPC_TheftPremiumB;
	private String VPC_TheftPremiumC;
	private String VPC_TheftIDVAdjustor;
	private String VPC_TheftLeapofFaith;
	private String VPC_TheftStreetPrice;
	private String VPC_TPPremiumA_rate;
	private String VPC_TPPremiumB_rate;
	private String VPC_TPPremiumC_rate;
	private String VPC_TPIDVAdjustor_rate;
	private String VPC_TPLeapofFaith_rate;
	private String VPC_TPPremiumAdjustor_rate;
	private String VPC_TPPremiumA;
	private String VPC_TPPremiumB;
	private String VPC_TPPremiumC;
	private String VPC_TPIDVAdjustor;
	private String VPC_TPLeapofFaith;
	private String VPC_TPPremiumAdjustor;
	private String ODTotalExpenseMultiplier;
	private String TPTotalExpenseMultiplier;
	private String TheftTotalExpenseMultiplier;
	private String ThirdPartyAdjustedPremium;
	private String VPC_ODPremiumD;
	private String VPC_TheftPremiumD;
	private String VPC_TPPremiumD;
	private String VPC_TariffODPremium;
	private String VPC_TPPODPremium;
	private String PremiumAfterTechDiscount;
	private String PremiumAfterCapDiscount;
	private String CapDiscount;
	private String PremiumAfterCapLoading;
	private String CapLoading;
	private String PremiumAdjustmentPercentage;
	
	public TppPricingValuesModel() {
		// TODO Auto-generated constructor stub
	}

	public TppPricingValuesModel(String vPC_ODPremiumA_rate, String vPC_ODPremiumB_rate, String vPC_ODPremiumC_rate,
			String vPC_ODIDVAdjustor_rate, String vPC_ODLeapofFaith_rate, String vPC_ODStreetPrice_rate, String vPC_ODPremiumA,
			String vPC_ODPremiumB, String vPC_ODPremiumC, String vPC_ODIDVAdjustor, String vPC_ODLeapofFaith,
			String vPC_ODStreetPrice, String vPC_TheftPremiumA_rate, String vPC_TheftPremiumB_rate,
			String vPC_TheftPremiumC_rate, String vPC_TheftIDVAdjustor_rate, String vPC_TheftLeapofFaith_rate,
			String vPC_TheftStreetPrice_rate, String vPC_TheftPremiumA, String vPC_TheftPremiumB, String vPC_TheftPremiumC,
			String vPC_TheftIDVAdjustor, String vPC_TheftLeapofFaith, String vPC_TheftStreetPrice, String vPC_TPPremiumA_rate,
			String vPC_TPPremiumB_rate, String vPC_TPPremiumC_rate, String vPC_TPIDVAdjustor_rate, String vPC_TPLeapofFaith_rate,
			String vPC_TPPremiumAdjustor_rate, String vPC_TPPremiumA, String vPC_TPPremiumB, String vPC_TPPremiumC,
			String vPC_TPIDVAdjustor, String vPC_TPLeapofFaith, String vPC_TPPremiumAdjustor, String oDTotalExpenseMultiplier,
			String tPTotalExpenseMultiplier, String theftTotalExpenseMultiplier, String thirdPartyAdjustedPremium,
			String vPC_ODPremiumD, String vPC_TheftPremiumD, String vPC_TPPremiumD, String vPC_TariffODPremium,
			String vPC_TPPODPremium, String premiumAfterTechDiscount, String premiumAfterCapDiscount, String capDiscount,
			String premiumAfterCapLoading, String capLoading, String premiumAdjustmentPercentage) {

		VPC_ODPremiumA_rate = vPC_ODPremiumA_rate;
		VPC_ODPremiumB_rate = vPC_ODPremiumB_rate;
		VPC_ODPremiumC_rate = vPC_ODPremiumC_rate;
		VPC_ODIDVAdjustor_rate = vPC_ODIDVAdjustor_rate;
		VPC_ODLeapofFaith_rate = vPC_ODLeapofFaith_rate;
		VPC_ODStreetPrice_rate = vPC_ODStreetPrice_rate;
		VPC_ODPremiumA = vPC_ODPremiumA;
		VPC_ODPremiumB = vPC_ODPremiumB;
		VPC_ODPremiumC = vPC_ODPremiumC;
		VPC_ODIDVAdjustor = vPC_ODIDVAdjustor;
		VPC_ODLeapofFaith = vPC_ODLeapofFaith;
		VPC_ODStreetPrice = vPC_ODStreetPrice;
		VPC_TheftPremiumA_rate = vPC_TheftPremiumA_rate;
		VPC_TheftPremiumB_rate = vPC_TheftPremiumB_rate;
		VPC_TheftPremiumC_rate = vPC_TheftPremiumC_rate;
		VPC_TheftIDVAdjustor_rate = vPC_TheftIDVAdjustor_rate;
		VPC_TheftLeapofFaith_rate = vPC_TheftLeapofFaith_rate;
		VPC_TheftStreetPrice_rate = vPC_TheftStreetPrice_rate;
		VPC_TheftPremiumA = vPC_TheftPremiumA;
		VPC_TheftPremiumB = vPC_TheftPremiumB;
		VPC_TheftPremiumC = vPC_TheftPremiumC;
		VPC_TheftIDVAdjustor = vPC_TheftIDVAdjustor;
		VPC_TheftLeapofFaith = vPC_TheftLeapofFaith;
		VPC_TheftStreetPrice = vPC_TheftStreetPrice;
		VPC_TPPremiumA_rate = vPC_TPPremiumA_rate;
		VPC_TPPremiumB_rate = vPC_TPPremiumB_rate;
		VPC_TPPremiumC_rate = vPC_TPPremiumC_rate;
		VPC_TPIDVAdjustor_rate = vPC_TPIDVAdjustor_rate;
		VPC_TPLeapofFaith_rate = vPC_TPLeapofFaith_rate;
		VPC_TPPremiumAdjustor_rate = vPC_TPPremiumAdjustor_rate;
		VPC_TPPremiumA = vPC_TPPremiumA;
		VPC_TPPremiumB = vPC_TPPremiumB;
		VPC_TPPremiumC = vPC_TPPremiumC;
		VPC_TPIDVAdjustor = vPC_TPIDVAdjustor;
		VPC_TPLeapofFaith = vPC_TPLeapofFaith;
		VPC_TPPremiumAdjustor = vPC_TPPremiumAdjustor;
		ODTotalExpenseMultiplier = oDTotalExpenseMultiplier;
		TPTotalExpenseMultiplier = tPTotalExpenseMultiplier;
		TheftTotalExpenseMultiplier = theftTotalExpenseMultiplier;
		ThirdPartyAdjustedPremium = thirdPartyAdjustedPremium;
		VPC_ODPremiumD = vPC_ODPremiumD;
		VPC_TheftPremiumD = vPC_TheftPremiumD;
		VPC_TPPremiumD = vPC_TPPremiumD;
		VPC_TariffODPremium = vPC_TariffODPremium;
		VPC_TPPODPremium = vPC_TPPODPremium;
		PremiumAfterTechDiscount = premiumAfterTechDiscount;
		PremiumAfterCapDiscount = premiumAfterCapDiscount;
		CapDiscount = capDiscount;
		PremiumAfterCapLoading = premiumAfterCapLoading;
		CapLoading = capLoading;
		PremiumAdjustmentPercentage = premiumAdjustmentPercentage;
	}

	public String getCapDiscount() {
		return CapDiscount;
	}

	public String getCapLoading() {
		return CapLoading;
	}

	public String getODTotalExpenseMultiplier() {
		return ODTotalExpenseMultiplier;
	}

	public String getPremiumAdjustmentPercentage() {
		return PremiumAdjustmentPercentage;
	}

	public String getPremiumAfterCapDiscount() {
		return PremiumAfterCapDiscount;
	}

	public String getPremiumAfterCapLoading() {
		return PremiumAfterCapLoading;
	}

	public String getPremiumAfterTechDiscount() {
		return PremiumAfterTechDiscount;
	}

	public String getTheftTotalExpenseMultiplier() {
		return TheftTotalExpenseMultiplier;
	}

	public String getThirdPartyAdjustedPremium() {
		return ThirdPartyAdjustedPremium;
	}

	public String getTPTotalExpenseMultiplier() {
		return TPTotalExpenseMultiplier;
	}

	public String getVPC_ODIDVAdjustor() {
		return VPC_ODIDVAdjustor;
	}

	public String getVPC_ODIDVAdjustor_rate() {
		return VPC_ODIDVAdjustor_rate;
	}

	public String getVPC_ODLeapofFaith() {
		return VPC_ODLeapofFaith;
	}

	public String getVPC_ODLeapofFaith_rate() {
		return VPC_ODLeapofFaith_rate;
	}

	public String getVPC_ODPremiumA() {
		return VPC_ODPremiumA;
	}

	public String getVPC_ODPremiumA_rate() {
		return VPC_ODPremiumA_rate;
	}

	public String getVPC_ODPremiumB() {
		return VPC_ODPremiumB;
	}

	public String getVPC_ODPremiumB_rate() {
		return VPC_ODPremiumB_rate;
	}

	public String getVPC_ODPremiumC() {
		return VPC_ODPremiumC;
	}

	public String getVPC_ODPremiumC_rate() {
		return VPC_ODPremiumC_rate;
	}

	public String getVPC_ODPremiumD() {
		return VPC_ODPremiumD;
	}

	public String getVPC_ODStreetPrice() {
		return VPC_ODStreetPrice;
	}

	public String getVPC_ODStreetPrice_rate() {
		return VPC_ODStreetPrice_rate;
	}

	public String getVPC_TariffODPremium() {
		return VPC_TariffODPremium;
	}

	public String getVPC_TheftIDVAdjustor() {
		return VPC_TheftIDVAdjustor;
	}

	public String getVPC_TheftIDVAdjustor_rate() {
		return VPC_TheftIDVAdjustor_rate;
	}

	public String getVPC_TheftLeapofFaith() {
		return VPC_TheftLeapofFaith;
	}

	public String getVPC_TheftLeapofFaith_rate() {
		return VPC_TheftLeapofFaith_rate;
	}

	public String getVPC_TheftPremiumA() {
		return VPC_TheftPremiumA;
	}

	public String getVPC_TheftPremiumA_rate() {
		return VPC_TheftPremiumA_rate;
	}

	public String getVPC_TheftPremiumB() {
		return VPC_TheftPremiumB;
	}

	public String getVPC_TheftPremiumB_rate() {
		return VPC_TheftPremiumB_rate;
	}

	public String getVPC_TheftPremiumC() {
		return VPC_TheftPremiumC;
	}

	public String getVPC_TheftPremiumC_rate() {
		return VPC_TheftPremiumC_rate;
	}

	public String getVPC_TheftPremiumD() {
		return VPC_TheftPremiumD;
	}

	public String getVPC_TheftStreetPrice() {
		return VPC_TheftStreetPrice;
	}

	public String getVPC_TheftStreetPrice_rate() {
		return VPC_TheftStreetPrice_rate;
	}

	public String getVPC_TPIDVAdjustor() {
		return VPC_TPIDVAdjustor;
	}

	public String getVPC_TPIDVAdjustor_rate() {
		return VPC_TPIDVAdjustor_rate;
	}

	public String getVPC_TPLeapofFaith() {
		return VPC_TPLeapofFaith;
	}

	public String getVPC_TPLeapofFaith_rate() {
		return VPC_TPLeapofFaith_rate;
	}

	public String getVPC_TPPODPremium() {
		return VPC_TPPODPremium;
	}

	public String getVPC_TPPremiumA() {
		return VPC_TPPremiumA;
	}

	public String getVPC_TPPremiumA_rate() {
		return VPC_TPPremiumA_rate;
	}

	public String getVPC_TPPremiumAdjustor() {
		return VPC_TPPremiumAdjustor;
	}

	public String getVPC_TPPremiumAdjustor_rate() {
		return VPC_TPPremiumAdjustor_rate;
	}

	public String getVPC_TPPremiumB() {
		return VPC_TPPremiumB;
	}

	public String getVPC_TPPremiumB_rate() {
		return VPC_TPPremiumB_rate;
	}

	public String getVPC_TPPremiumC() {
		return VPC_TPPremiumC;
	}

	public String getVPC_TPPremiumC_rate() {
		return VPC_TPPremiumC_rate;
	}

	public String getVPC_TPPremiumD() {
		return VPC_TPPremiumD;
	}

	public void setCapDiscount(String capDiscount) {
		CapDiscount = capDiscount;
	}

	public void setCapLoading(String capLoading) {
		CapLoading = capLoading;
	}

	public void setODTotalExpenseMultiplier(String oDTotalExpenseMultiplier) {
		ODTotalExpenseMultiplier = oDTotalExpenseMultiplier;
	}

	public void setPremiumAdjustmentPercentage(String premiumAdjustmentPercentage) {
		PremiumAdjustmentPercentage = premiumAdjustmentPercentage;
	}

	public void setPremiumAfterCapDiscount(String premiumAfterCapDiscount) {
		PremiumAfterCapDiscount = premiumAfterCapDiscount;
	}

	public void setPremiumAfterCapLoading(String premiumAfterCapLoading) {
		PremiumAfterCapLoading = premiumAfterCapLoading;
	}

	public void setPremiumAfterTechDiscount(String premiumAfterTechDiscount) {
		PremiumAfterTechDiscount = premiumAfterTechDiscount;
	}

	public void setTheftTotalExpenseMultiplier(String theftTotalExpenseMultiplier) {
		TheftTotalExpenseMultiplier = theftTotalExpenseMultiplier;
	}

	public void setThirdPartyAdjustedPremium(String thirdPartyAdjustedPremium) {
		ThirdPartyAdjustedPremium = thirdPartyAdjustedPremium;
	}

	public void setTPTotalExpenseMultiplier(String tPTotalExpenseMultiplier) {
		TPTotalExpenseMultiplier = tPTotalExpenseMultiplier;
	}

	public void setVPC_ODIDVAdjustor(String vPC_ODIDVAdjustor) {
		VPC_ODIDVAdjustor = vPC_ODIDVAdjustor;
	}

	public void setVPC_ODIDVAdjustor_rate(String vPC_ODIDVAdjustor_rate) {
		VPC_ODIDVAdjustor_rate = vPC_ODIDVAdjustor_rate;
	}

	public void setVPC_ODLeapofFaith(String vPC_ODLeapofFaith) {
		VPC_ODLeapofFaith = vPC_ODLeapofFaith;
	}

	public void setVPC_ODLeapofFaith_rate(String vPC_ODLeapofFaith_rate) {
		VPC_ODLeapofFaith_rate = vPC_ODLeapofFaith_rate;
	}

	public void setVPC_ODPremiumA(String vPC_ODPremiumA) {
		VPC_ODPremiumA = vPC_ODPremiumA;
	}

	public void setVPC_ODPremiumA_rate(String vPC_ODPremiumA_rate) {
		VPC_ODPremiumA_rate = vPC_ODPremiumA_rate;
	}

	public void setVPC_ODPremiumB(String vPC_ODPremiumB) {
		VPC_ODPremiumB = vPC_ODPremiumB;
	}

	public void setVPC_ODPremiumB_rate(String vPC_ODPremiumB_rate) {
		VPC_ODPremiumB_rate = vPC_ODPremiumB_rate;
	}

	public void setVPC_ODPremiumC(String vPC_ODPremiumC) {
		VPC_ODPremiumC = vPC_ODPremiumC;
	}

	public void setVPC_ODPremiumC_rate(String vPC_ODPremiumC_rate) {
		VPC_ODPremiumC_rate = vPC_ODPremiumC_rate;
	}

	public void setVPC_ODPremiumD(String vPC_ODPremiumD) {
		VPC_ODPremiumD = vPC_ODPremiumD;
	}

	public void setVPC_ODStreetPrice(String vPC_ODStreetPrice) {
		VPC_ODStreetPrice = vPC_ODStreetPrice;
	}

	public void setVPC_ODStreetPrice_rate(String vPC_ODStreetPrice_rate) {
		VPC_ODStreetPrice_rate = vPC_ODStreetPrice_rate;
	}

	public void setVPC_TariffODPremium(String vPC_TariffODPremium) {
		VPC_TariffODPremium = vPC_TariffODPremium;
	}

	public void setVPC_TheftIDVAdjustor(String vPC_TheftIDVAdjustor) {
		VPC_TheftIDVAdjustor = vPC_TheftIDVAdjustor;
	}

	public void setVPC_TheftIDVAdjustor_rate(String vPC_TheftIDVAdjustor_rate) {
		VPC_TheftIDVAdjustor_rate = vPC_TheftIDVAdjustor_rate;
	}

	public void setVPC_TheftLeapofFaith(String vPC_TheftLeapofFaith) {
		VPC_TheftLeapofFaith = vPC_TheftLeapofFaith;
	}

	public void setVPC_TheftLeapofFaith_rate(String vPC_TheftLeapofFaith_rate) {
		VPC_TheftLeapofFaith_rate = vPC_TheftLeapofFaith_rate;
	}

	public void setVPC_TheftPremiumA(String vPC_TheftPremiumA) {
		VPC_TheftPremiumA = vPC_TheftPremiumA;
	}

	public void setVPC_TheftPremiumA_rate(String vPC_TheftPremiumA_rate) {
		VPC_TheftPremiumA_rate = vPC_TheftPremiumA_rate;
	}

	public void setVPC_TheftPremiumB(String vPC_TheftPremiumB) {
		VPC_TheftPremiumB = vPC_TheftPremiumB;
	}

	public void setVPC_TheftPremiumB_rate(String vPC_TheftPremiumB_rate) {
		VPC_TheftPremiumB_rate = vPC_TheftPremiumB_rate;
	}

	public void setVPC_TheftPremiumC(String vPC_TheftPremiumC) {
		VPC_TheftPremiumC = vPC_TheftPremiumC;
	}

	public void setVPC_TheftPremiumC_rate(String vPC_TheftPremiumC_rate) {
		VPC_TheftPremiumC_rate = vPC_TheftPremiumC_rate;
	}

	public void setVPC_TheftPremiumD(String vPC_TheftPremiumD) {
		VPC_TheftPremiumD = vPC_TheftPremiumD;
	}

	public void setVPC_TheftStreetPrice(String vPC_TheftStreetPrice) {
		VPC_TheftStreetPrice = vPC_TheftStreetPrice;
	}

	public void setVPC_TheftStreetPrice_rate(String vPC_TheftStreetPrice_rate) {
		VPC_TheftStreetPrice_rate = vPC_TheftStreetPrice_rate;
	}

	public void setVPC_TPIDVAdjustor(String vPC_TPIDVAdjustor) {
		VPC_TPIDVAdjustor = vPC_TPIDVAdjustor;
	}

	public void setVPC_TPIDVAdjustor_rate(String vPC_TPIDVAdjustor_rate) {
		VPC_TPIDVAdjustor_rate = vPC_TPIDVAdjustor_rate;
	}

	public void setVPC_TPLeapofFaith(String vPC_TPLeapofFaith) {
		VPC_TPLeapofFaith = vPC_TPLeapofFaith;
	}

	public void setVPC_TPLeapofFaith_rate(String vPC_TPLeapofFaith_rate) {
		VPC_TPLeapofFaith_rate = vPC_TPLeapofFaith_rate;
	}

	public void setVPC_TPPODPremium(String vPC_TPPODPremium) {
		VPC_TPPODPremium = vPC_TPPODPremium;
	}

	public void setVPC_TPPremiumA(String vPC_TPPremiumA) {
		VPC_TPPremiumA = vPC_TPPremiumA;
	}

	public void setVPC_TPPremiumA_rate(String vPC_TPPremiumA_rate) {
		VPC_TPPremiumA_rate = vPC_TPPremiumA_rate;
	}

	public void setVPC_TPPremiumAdjustor(String vPC_TPPremiumAdjustor) {
		VPC_TPPremiumAdjustor = vPC_TPPremiumAdjustor;
	}

	public void setVPC_TPPremiumAdjustor_rate(String vPC_TPPremiumAdjustor_rate) {
		VPC_TPPremiumAdjustor_rate = vPC_TPPremiumAdjustor_rate;
	}

	public void setVPC_TPPremiumB(String vPC_TPPremiumB) {
		VPC_TPPremiumB = vPC_TPPremiumB;
	}

	public void setVPC_TPPremiumB_rate(String vPC_TPPremiumB_rate) {
		VPC_TPPremiumB_rate = vPC_TPPremiumB_rate;
	}

	public void setVPC_TPPremiumC(String vPC_TPPremiumC) {
		VPC_TPPremiumC = vPC_TPPremiumC;
	}

	public void setVPC_TPPremiumC_rate(String vPC_TPPremiumC_rate) {
		VPC_TPPremiumC_rate = vPC_TPPremiumC_rate;
	}

	public void setVPC_TPPremiumD(String vPC_TPPremiumD) {
		VPC_TPPremiumD = vPC_TPPremiumD;
	}

}
