package com.xerago.rsa.result;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "LIABILITY")
@XmlType (propOrder={"BASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD","BI_FUEL_KIT_CNG","PERSONAL_ACCIDENT_BENEFITS","UNDER_SECTION_III_OWNER_DRIVER","UNNAMED_PASSENGRS","PA_COVER_TO_PAID_DRIVER","TO_PAID_DRIVERS","TO_EMPLOYESES","TOTAL_LIABILITY_PREMIUM","LEGALLIABILITY_TO_PASSENGERS","LLDriverConductorCleaner","TPPDStatutoryDiscount"})
public class Liability {
	private String BASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD;
	private String BI_FUEL_KIT_CNG;
	private String PERSONAL_ACCIDENT_BENEFITS;
	private String UNDER_SECTION_III_OWNER_DRIVER;
	private String UNNAMED_PASSENGRS;
	private String PA_COVER_TO_PAID_DRIVER;
	private String TO_PAID_DRIVERS;
	private String TO_EMPLOYESES;
	private String TOTAL_LIABILITY_PREMIUM;
	private String LEGALLIABILITY_TO_PASSENGERS;
	private String LLDriverConductorCleaner;
	private String TPPDStatutoryDiscount;

	public String getBASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD() {
		return BASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD;
	}

	public String getBI_FUEL_KIT_CNG() {
		return BI_FUEL_KIT_CNG;
	}

	public String getLEGALLIABILITY_TO_PASSENGERS() {
		return LEGALLIABILITY_TO_PASSENGERS;
	}

	public String getLLDriverConductorCleaner() {
		return LLDriverConductorCleaner;
	}

	public String getPA_COVER_TO_PAID_DRIVER() {
		return PA_COVER_TO_PAID_DRIVER;
	}

	public String getPERSONAL_ACCIDENT_BENEFITS() {
		return PERSONAL_ACCIDENT_BENEFITS;
	}

	public String getTO_EMPLOYESES() {
		return TO_EMPLOYESES;
	}

	public String getTO_PAID_DRIVERS() {
		return TO_PAID_DRIVERS;
	}

	public String getTOTAL_LIABILITY_PREMIUM() {
		return TOTAL_LIABILITY_PREMIUM;
	}

	public String getTPPDStatutoryDiscount() {
		return TPPDStatutoryDiscount;
	}

	public String getUNDER_SECTION_III_OWNER_DRIVER() {
		return UNDER_SECTION_III_OWNER_DRIVER;
	}

	public String getUNNAMED_PASSENGRS() {
		return UNNAMED_PASSENGRS;
	}

	public void setBASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD(String bASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD) {
		BASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD = bASIC_PREMIUM_INCLUDING_PREMIUM_FOR_TPPD;
	}

	public void setBI_FUEL_KIT_CNG(String bI_FUEL_KIT_CNG) {
		BI_FUEL_KIT_CNG = bI_FUEL_KIT_CNG;
	}

	public void setLEGALLIABILITY_TO_PASSENGERS(String lEGALLIABILITY_TO_PASSENGERS) {
		LEGALLIABILITY_TO_PASSENGERS = lEGALLIABILITY_TO_PASSENGERS;
	}

	public void setLLDriverConductorCleaner(String lLDriverConductorCleaner) {
		LLDriverConductorCleaner = lLDriverConductorCleaner;
	}

	public void setPA_COVER_TO_PAID_DRIVER(String pA_COVER_TO_PAID_DRIVER) {
		PA_COVER_TO_PAID_DRIVER = pA_COVER_TO_PAID_DRIVER;
	}

	public void setPERSONAL_ACCIDENT_BENEFITS(String pERSONAL_ACCIDENT_BENEFITS) {
		PERSONAL_ACCIDENT_BENEFITS = pERSONAL_ACCIDENT_BENEFITS;
	}

	public void setTO_EMPLOYESES(String tO_EMPLOYESES) {
		TO_EMPLOYESES = tO_EMPLOYESES;
	}

	public void setTO_PAID_DRIVERS(String tO_PAID_DRIVERS) {
		TO_PAID_DRIVERS = tO_PAID_DRIVERS;
	}

	public void setTOTAL_LIABILITY_PREMIUM(String tOTAL_LIABILITY_PREMIUM) {
		TOTAL_LIABILITY_PREMIUM = tOTAL_LIABILITY_PREMIUM;
	}

	public void setTPPDStatutoryDiscount(String tPPDStatutoryDiscount) {
		TPPDStatutoryDiscount = tPPDStatutoryDiscount;
	}

	public void setUNDER_SECTION_III_OWNER_DRIVER(String uNDER_SECTION_III_OWNER_DRIVER) {
		UNDER_SECTION_III_OWNER_DRIVER = uNDER_SECTION_III_OWNER_DRIVER;
	}

	public void setUNNAMED_PASSENGRS(String uNNAMED_PASSENGRS) {
		UNNAMED_PASSENGRS = uNNAMED_PASSENGRS;
	}

}
