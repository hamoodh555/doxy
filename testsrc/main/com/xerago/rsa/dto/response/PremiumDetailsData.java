/**
 * 
 */
package com.xerago.rsa.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author rajadurai
 *
 */
public class PremiumDetailsData {
	
	
	@JsonProperty("GROSS_PREMIUM")
	private String grossPremium;
	
	@JsonProperty("USER_ID")
    private String userId;
    
    @JsonProperty("NET_PREMIUM")
    private String netPremium;
    
    @JsonProperty("PREMIUM_WITHOUT_COVERS")
    private String premiumWithoutCovers;
    
    @JsonProperty("IDV")
    private Double idv;
        
	@JsonProperty("QUOTE_ID")
    private String quoteId;
    
    @JsonProperty("PACKAGE_PREMIUM")
    private String packagePremium;
    
    @JsonProperty("PREMIUM")
    private String premium;
    
    @JsonProperty("SERVICETAX")
    private String serviceTax;
    
    @JsonProperty("ECESS")
    private String ecess;
    
    @JsonProperty("KRISHI_CESS")
    private String krishiCess;
    
    @JsonProperty("POLICY_START_DATE")
    private String policyStartDate;
    
    @JsonProperty("POLICY_EXPIRY_DATE")
    private String policyExpiryDate;
    
    @JsonProperty("CLIENTCODE")
    private String clientCode;
    
    @JsonProperty("VERSION_NO")
    private String versionNo;
    
    private String description;
    
    @JsonProperty("Description")
    private String descriptions;	
    
    @JsonProperty("OD_PREMIUM")
    private OdPremium odPremium;
    
    @JsonProperty("LIABILITY")
    private Liability liability;
    
    @JsonProperty("POLICY_TERM")
    private Integer policyTerm;
    

    @JsonProperty("TAX_TYPE")
    private String taxType;
    
    @JsonProperty("IGST")
	private String igst;
    
    @JsonProperty("CGST")
	private String cgst;
    
    @JsonProperty("SGST")
	private String sgst;
    
    @JsonProperty("UTGST")
	private String utgst;
    
    @JsonProperty("IDV_FOR_1ST_YEAR")
	private String idvFor1stYear;
    @JsonProperty("IDV_FOR_2ND_YEAR")
	private String idvFor2ndYear;
    @JsonProperty("IDV_FOR_3RD_YEAR")
	private String idvFor3rdYear;
    @JsonProperty("IDV_FOR_4TH_YEAR")
	private String idvFor4thYear;
    @JsonProperty("IDV_FOR_5TH_YEAR")
	private String idvFor5thYear;
    @JsonProperty("TECHNICAL_DISCOUNT")
    private String technicalDiscount;
    @JsonProperty("LIABILITY_POLICY_TERM")
    private String liabilityPolicyTerm;
    
    
    
    @JsonProperty("POLICY_NUMBER")
    private String policyNumber;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss")
    @JsonProperty("BUY_DATE")
	private Date buyDate;
	
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    @JsonProperty("INCEPTION_DATE")
	private Date inceptionDate;
	
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    @JsonProperty("EXPIRY_DATE")
	private Date expiryDate;
	
    @JsonProperty("POLICY_DOWNLOAD_LINK")
	private String policyDownloadLink;
	
    @JsonProperty("PREMIUM_PAID")
    private String premiumPaid;
    
    
    @JsonProperty("LIABILITY_PREMIUM")
    private String liabilityPremium;
    
    @JsonProperty("LIABILITY_PREMIUM_FOR_2YEAR")
    private String liabilityPremiumFor2ndYear;
    
    @JsonProperty("LIABILITY_PREMIUM_FOR_3YEAR")
    private String liabilityPremiumFor3rdYear;
    
    @JsonProperty("BUNDLE_PREMIUM")
    private String bundlePremium;
    
    @JsonProperty("COMPREHENSIVE_PREMIUM")
    private String comprehensivePremium;
    
    @JsonProperty("PREMIUM_FOR_1YEAR")
    private String firstYearPremium;
    
    @JsonProperty("PREMIUM_FOR_2YEAR")
    private String secondYearPremium;
    
    @JsonProperty("PREMIUM_FOR_3YEAR")
    private String thirdYearPremium;
    
    @JsonProperty("CAMPAIGN_DISCOUNT")
    private String campaignDiscount;
    

    @JsonProperty("OD_START_DATE")
    private String odStartDate;
    
    @JsonProperty("OD_EXPIRY_DATE")
    private String odExpiryDate;
    
    @JsonProperty("KERALA_FLOOD_CESS")
	private String keralaFloodCess;
    
    @JsonProperty("VAHAN")
    private VahanResponse vahanResponse;
    

    public String getLiabilityPolicyTerm() {
		return liabilityPolicyTerm;
	}

	public void setLiabilityPolicyTerm(String liabilityPolicyTerm) {
		this.liabilityPolicyTerm = liabilityPolicyTerm;
	}

	public Integer getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(Integer policyTerm) {
		this.policyTerm = policyTerm;
	}

	public String getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(String grossPremium) {
		this.grossPremium = grossPremium;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNetPremium() {
		return netPremium;
	}

	public void setNetPremium(String netPremium) {
		this.netPremium = netPremium;
	}

	public String getPremiumWithoutCovers() {
		return premiumWithoutCovers;
	}

	public void setPremiumWithoutCovers(String premiumWithoutCovers) {
		this.premiumWithoutCovers = premiumWithoutCovers;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getPackagePremium() {
		return packagePremium;
	}

	public void setPackagePremium(String packagePremium) {
		this.packagePremium = packagePremium;
	}

	public String getPremium() {
		return premium;
	}

	public void setPremium(String premium) {
		this.premium = premium;
	}

	public String getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(String serviceTax) {
		this.serviceTax = serviceTax;
	}

	public String getEcess() {
		return ecess;
	}

	public void setEcess(String ecess) {
		this.ecess = ecess;
	}

	public String getKrishiCess() {
		return krishiCess;
	}

	public void setKrishiCess(String krishiCess) {
		this.krishiCess = krishiCess;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getPolicyExpiryDate() {
		return policyExpiryDate;
	}

	public void setPolicyExpiryDate(String policyExpiryDate) {
		this.policyExpiryDate = policyExpiryDate;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OdPremium getOdPremium() {
		return odPremium;
	}

	public void setOdPremium(OdPremium odPremium) {
		this.odPremium = odPremium;
	}

	public Liability getLiability() {
		return liability;
	}

	public void setLiability(Liability liability) {
		this.liability = liability;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getIgst() {
		return igst;
	}

	public void setIgst(String igst) {
		this.igst = igst;
	}

	public String getCgst() {
		return cgst;
	}

	public void setCgst(String cgst) {
		this.cgst = cgst;
	}

	public String getSgst() {
		return sgst;
	}

	public void setSgst(String sgst) {
		this.sgst = sgst;
	}

	public String getUtgst() {
		return utgst;
	}

	public void setUtgst(String utgst) {
		this.utgst = utgst;
	}

	public Double getIdv() {
		return idv;
	}

	public void setIdv(Double idv) {
		this.idv = idv;
	}

	public String getIdvFor1stYear() {
		return idvFor1stYear;
	}

	public void setIdvFor1stYear(String idvFor1stYear) {
		this.idvFor1stYear = idvFor1stYear;
	}

	public String getIdvFor2ndYear() {
		return idvFor2ndYear;
	}

	public void setIdvFor2ndYear(String idvFor2ndYear) {
		this.idvFor2ndYear = idvFor2ndYear;
	}

	public String getIdvFor3rdYear() {
		return idvFor3rdYear;
	}

	public void setIdvFor3rdYear(String idvFor3rdYear) {
		this.idvFor3rdYear = idvFor3rdYear;
	}

	public String getIdvFor4thYear() {
		return idvFor4thYear;
	}

	public void setIdvFor4thYear(String idvFor4thYear) {
		this.idvFor4thYear = idvFor4thYear;
	}

	public String getIdvFor5thYear() {
		return idvFor5thYear;
	}

	public void setIdvFor5thYear(String idvFor5thYear) {
		this.idvFor5thYear = idvFor5thYear;
	}

	public String getTechnicalDiscount() {
		return technicalDiscount;
	}

	public void setTechnicalDiscount(String technicalDiscount) {
		this.technicalDiscount = technicalDiscount;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public Date getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(Date inceptionDate) {
		this.inceptionDate = inceptionDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPolicyDownloadLink() {
		return policyDownloadLink;
	}

	public void setPolicyDownloadLink(String policyDownloadLink) {
		this.policyDownloadLink = policyDownloadLink;
	}

	public String getPremiumPaid() {
		return premiumPaid;
	}

	public void setPremiumPaid(String premiumPaid) {
		this.premiumPaid = premiumPaid;
	}

	

	public String getLiabilityPremium() {
		return liabilityPremium;
	}

	public void setLiabilityPremium(String liabilityPremium) {
		this.liabilityPremium = liabilityPremium;
	}

	public String getLiabilityPremiumFor2ndYear() {
		return liabilityPremiumFor2ndYear;
	}

	public void setLiabilityPremiumFor2ndYear(String liabilityPremiumFor2ndYear) {
		this.liabilityPremiumFor2ndYear = liabilityPremiumFor2ndYear;
	}

	public String getLiabilityPremiumFor3rdYear() {
		return liabilityPremiumFor3rdYear;
	}

	public void setLiabilityPremiumFor3rdYear(String liabilityPremiumFor3rdYear) {
		this.liabilityPremiumFor3rdYear = liabilityPremiumFor3rdYear;
	}

	public String getBundlePremium() {
		return bundlePremium;
	}

	public void setBundlePremium(String bundlePremium) {
		this.bundlePremium = bundlePremium;
	}

	public String getComprehensivePremium() {
		return comprehensivePremium;
	}

	public void setComprehensivePremium(String comprehensivePremium) {
		this.comprehensivePremium = comprehensivePremium;
	}

	public String getFirstYearPremium() {
		return firstYearPremium;
	}

	public void setFirstYearPremium(String firstYearPremium) {
		this.firstYearPremium = firstYearPremium;
	}

	public String getSecondYearPremium() {
		return secondYearPremium;
	}

	public void setSecondYearPremium(String secondYearPremium) {
		this.secondYearPremium = secondYearPremium;
	}

	public String getThirdYearPremium() {
		return thirdYearPremium;
	}

	public void setThirdYearPremium(String thirdYearPremium) {
		this.thirdYearPremium = thirdYearPremium;
	}

	public String getCampaignDiscount() {
		return campaignDiscount;
	}

	public void setCampaignDiscount(String campaignDiscount) {
		this.campaignDiscount = campaignDiscount;
	}

	public String getOdStartDate() {
		return odStartDate;
	}

	public void setOdStartDate(String odStartDate) {
		this.odStartDate = odStartDate;
	}

	public String getOdExpiryDate() {
		return odExpiryDate;
	}

	public void setOdExpiryDate(String odExpiryDate) {
		this.odExpiryDate = odExpiryDate;
	}

	public String getKeralaFloodCess() {
		return keralaFloodCess;
	}

	public void setKeralaFloodCess(String keralaFloodCess) {
		this.keralaFloodCess = keralaFloodCess;
	}

	public VahanResponse getVahanResponse() {
		return vahanResponse;
	}

	public void setVahanResponse(VahanResponse vahanResponse) {
		this.vahanResponse = vahanResponse;
	}

	
    
}
