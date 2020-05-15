package com.xerago.rsa.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.xerago.rsa.bean.validation.constraints.NotContains;
import com.xerago.rsa.bean.validation.groups.CalculatePremiumProposer;
import com.xerago.rsa.bean.validation.groups.QuoteIdEmailID;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.xerago.rsa.bean.validation.groups.ProposalDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xerago.rsa.Regex;


@ApiModel(value="ProposerDetails", description = "TODO", parent = CalculatePremiumRequest.class)
public class ProposerDetails {
	
	@NotNull(groups={ProposalDetails.class})
	@NotBlank(groups={ProposalDetails.class}, message= "Please select your Title.")	
	@Size(max = 2, groups={ProposalDetails.class} )
	@NotContains(contains = { "Mr", "Ms" }, groups={ProposalDetails.class})
	@ApiModelProperty(value="Title Mr/Ms/Mrs.Applicable for Both BrandNew and RollOver",required = true, example = "Mr")
	private String title;
	
	@NotNull(groups={ProposalDetails.class, CalculatePremiumProposer.class})
	@NotBlank(groups={ProposalDetails.class, CalculatePremiumProposer.class}, message= "Please enter your First Name.")
	@Size(min = 1, max = 30, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	@Pattern(regexp = Regex.NAME, message = "Please enter a valid First Name - Alphabet and Spaces only.", groups={ProposalDetails.class})
	@ApiModelProperty(value="Applicable for Both BrandNew and RollOver",required = true, example = "abcdef")
	private String firstName;
	
	@NotNull(groups={ProposalDetails.class})
	@NotBlank(groups={ProposalDetails.class}, message= "Please enter your Last Name.")
	@Size(min = 1, max = 30, groups={ProposalDetails.class})
	@Pattern(regexp = Regex.NAME, message="Please enter your valid Last Name - Alphabet and Spaces only.", groups={ProposalDetails.class})
	@ApiModelProperty(required = false, example = "ghijkl", value = "Not Required While Get Quote.Applicable for Both BrandNew and RollOver")
	private String lastName;	
	
	@NotNull(groups={CalculatePremiumProposer.class, QuoteIdEmailID.class})
	@NotBlank(groups={CalculatePremiumProposer.class, QuoteIdEmailID.class})
	@Email
	@ApiModelProperty(value="Applicable for Both BrandNew and RollOver",required = true, example = "blablabla@xerago.com")
	private String emailId;
	
	@ApiModelProperty(hidden = true)
	private long userID;
	
	@NotNull(groups={com.xerago.rsa.bean.validation.groups.Proposer.class, CalculatePremiumProposer.class})
	@NotBlank(groups={com.xerago.rsa.bean.validation.groups.Proposer.class, CalculatePremiumProposer.class}, message = "Please enter your Mobile Number")
	@Size(min= 10, max =10, groups={com.xerago.rsa.bean.validation.groups.Proposer.class, CalculatePremiumProposer.class})
	@Pattern(regexp = Regex.MOBILE, groups={com.xerago.rsa.bean.validation.groups.Proposer.class, CalculatePremiumProposer.class}, message = "Please enter your valid Mobile Number - Numbers only")
	@ApiModelProperty(value="Applicable for Both BrandNew and RollOver",required = true, example = "9000000004")
	private String mobileNo;
	
	@ApiModelProperty(required = true, example = "12/08/1990", value = "Not Required While Get Quote")
	private String dateOfBirth;
	
	@ApiModelProperty(hidden = true)
	private String isNewUser;
	
	@ApiModelProperty(hidden = true)
	private String isLoginCheck;
	
	@NotNull(groups={ProposalDetails.class})
	@NotBlank(groups={ProposalDetails.class}, message= "Please select your Occupation")
	@Size(max = 60, groups={Proposer.class})
	@ApiModelProperty(required = true, example = "IT Profession", allowableValues = "Armed Forces , Business / Sales Profession , Central / State Government Employee , Corporate Executive , Engineering Profession , Financial Services Profession , Heads of Government , Heads of State , Home Maker / Housewife , IT Profession , Medical Profession , Musician / Artist , Sports Person , Student , Teaching Profession , Political Party Official , Politician , Senior Government Official , Senior Judicial Official , Senior Military Official , State-owned Corporation Official ,  Others", value = "Not Required While Get Quote")
	private String occupation;
	
	@ApiModelProperty(required = true, example = "abcdef", value = "Not Required While Get Quote")
	@Size(min = 1, max = 30, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String nomineeName;
	
	@ApiModelProperty(required = true, example = "14", value = "If Nominee Age is below 18 Years then guardianName and guardianAge, relationshipwithGuardian parameters are required. Not Required While Get Quote")
	private String nomineeAge;
	
	
	@ApiModelProperty(required = true, example = "Son", allowableValues = "Spouse, Father, Mother, Son, Daughter, Brother, Sister, Grand Son, Grand Daughter, Grand Father, Grand Mother, Father-in-Law, Mother-in-Law, Son-in-Law, Daughter-in-Law, Brother-in-Law, Sister-in-Law, Uncle, Aunt, Cousin, Nephew, Niece, Legal Guardian", value = "Not Required While Get Quote")
	@Size(min = 1, max = 30, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String relationshipWithNominee;
	
	@ApiModelProperty(required = true, example = "ghijkl", value = "Not Required While Get Quote")
	@Size(min = 1, max = 30, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String guardianName;
	
	@ApiModelProperty(required = true, example = "54", value = "Not Required While Get Quote")
	@Size(min = 1, max = 2, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String guardianAge;
	
	@ApiModelProperty(required = true, example = "Grand-Father", value = "Not Required While Get Quote",allowableValues ="Spouse, Father, Mother, Son, Daughter, Brother, Sister, Grand Son, Grand Daughter, Grand Father, Grand Mother, Father-in-Law, Mother-in-Law, Son-in-Law, Daughter-in-Law, Brother-in-Law, Sister-in-Law, Uncle, Aunt, Cousin, Nephew, Niece, Legal Guardian")
	@Size(min = 1, max = 30, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String relationshipwithGuardian;	
	
	@ApiModelProperty(required = true, example = "twlmbkd", value = "Not Required While Get Quote")
	@Size(min = 1, max = 255, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String permanentAddress1;
	
	@ApiModelProperty(required = true, example = "bqjwels", value = "Not Required While Get Quote")
	@Size(min = 1, max = 255, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String permanentAddress2;
	
	@ApiModelProperty(required = false, example = "", value = "Not Required While Get Quote")
	@Size(min = 1, max = 255, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String permanentAddress3;
	
	@ApiModelProperty(required = false, example = "", value = "Not Required While Get Quote")
	@Size(min = 1, max = 255, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String permanentAddress4;
	
	@ApiModelProperty(required = true, example = "CHENNAI", value = "Not Required While Get Quote")
	@Size(min = 1, max = 40, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String permanentCity;
	
	@ApiModelProperty(required = true, example = "600032", value = "Not Required While Get Quote")
	@Size(min = 1, max = 6, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String permanentPincode;
	
	@ApiModelProperty(required = true, example = "No", value = "Not Required While Get Quote")
	@Size(min = 2, max = 3, groups={ProposalDetails.class, CalculatePremiumProposer.class})
	private String sameAdressReg;
	
	@NotNull(groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	@NotBlank(groups={com.xerago.rsa.bean.validation.groups.Proposer.class}, message= "Please enter your House number and name of the Building/Apartment")
	@Size(max = 255, groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	@ApiModelProperty(required = true, example = "twlmbkd", value = "Not Required While Get Quote")
	private String residenceAddressOne;
	
	@NotNull(groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	@NotBlank(groups={com.xerago.rsa.bean.validation.groups.Proposer.class}, message= "Please enter your House number and name of the Building/Apartment")
	@Size(max = 255, groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	@ApiModelProperty(required = true, example = "bqjwels", value = "Not Required While Get Quote")
	private String residenceAddressTwo;
	
	@ApiModelProperty(required = false, example = "", value = "Not Required While Get Quote")
	@Size(max = 255, groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	private String residenceAddressThree;
	
	@ApiModelProperty(required = false, example = "", value = "Not Required While Get Quote")
	@Size(max = 255, groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	private String residenceAddressFour;
	
	@NotNull(groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	@NotBlank(groups={com.xerago.rsa.bean.validation.groups.Proposer.class}, message= "Please select your City Name")
	@Size(max = 40, groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	@Pattern(regexp = Regex.CITY, groups={com.xerago.rsa.bean.validation.groups.Proposer.class},  message= "Please enter your valid City Name")
	@ApiModelProperty(required = true, example = "CHENNAI", value = "Not Required While Get Quote")
	private String residenceCity;
	
	@NotNull(groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	@NotBlank(groups={com.xerago.rsa.bean.validation.groups.Proposer.class}, message= "Please enter your valid Pincode")
	@Size(max = 6, groups={com.xerago.rsa.bean.validation.groups.Proposer.class})
	@Pattern(regexp = Regex.PINCODE, groups={com.xerago.rsa.bean.validation.groups.Proposer.class},  message= "Please enter your valid Pincode")
	@ApiModelProperty(required = true, example = "600032", value = "Not Required While Get Quote")
	private String residencePinCode;
	
	@ApiModelProperty(hidden = true)
	private String passwordResetted;
	
	@ApiModelProperty(hidden = true)
	private String isTransCheck;
	
	@ApiModelProperty(hidden = true)
	private String clientName;
	
	/** Task #119207 MIBL for Phase II **/
	@ApiModelProperty(required = false, example = "", value = "Not Required While Get Quote")
	private String strStdCode;
	@ApiModelProperty(required = false, example = "", value = "Not Required While Get Quote")
	private String strPhoneNo;
	/** Task #119207 MIBL for Phase II **/
	
	@ApiModelProperty(value = "Proposer Aadhar Number. Not Required While Get Quote", required = false, example = "987456321012")
	private String aadharNumber;
	
	@ApiModelProperty(value = "Proposer Pan Number. Not Required While Get Quote", required = false, example = "EAVPS1254J")
	private String panNumber;
	
	@JsonProperty("GSTIN")
	private String gstin;
	
	@ApiModelProperty(value ="EIA number. Not Required",required=false,example="EIA251205")
	private String eiaNumber;
	
	@ApiModelProperty(required= false,example="NSDL Database Management Limited")
	private String irName;

	@ApiModelProperty(required= false,example="Yes / No")
	private String updatePanAaadharLater;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public long getUserID() {
		return userID;
	}	
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getIsNewUser() {
		return isNewUser;
	}
	public void setIsNewUser(String isNewUser) {
		this.isNewUser = isNewUser;
	}
	public String getIsLoginCheck() {
		return isLoginCheck;
	}
	public void setIsLoginCheck(String isLoginCheck) {
		this.isLoginCheck = isLoginCheck;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getNomineeAge() {
		return nomineeAge;
	}
	@JsonProperty("ResidenceAddressOne")
	public String getResidenceAddressOne() {
		return residenceAddressOne;
	}
	public void setResidenceAddressOne(String residenceAddressOne) {
		this.residenceAddressOne = residenceAddressOne;
	}
	public void setNomineeAge(String nomineeAge) {
		this.nomineeAge = nomineeAge;
	}
	public String getRelationshipWithNominee() {
		return relationshipWithNominee;
	}
	public void setRelationshipWithNominee(String relationshipWithNominee) {
		this.relationshipWithNominee = relationshipWithNominee;
	}
	public String getGuardianName() {
		return guardianName;
	}
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	public String getGuardianAge() {
		return guardianAge;
	}
	public void setGuardianAge(String guardianAge) {
		this.guardianAge = guardianAge;
	}
	public String getRelationshipwithGuardian() {
		return relationshipwithGuardian;
	}
	public void setRelationshipwithGuardian(String relationshipwithGuardian) {
		this.relationshipwithGuardian = relationshipwithGuardian;
	}
	public String getPermanentAddress1() {
		return permanentAddress1;
	}
	public void setPermanentAddress1(String permanentAddress1) {
		this.permanentAddress1 = permanentAddress1;
	}
	public String getPermanentAddress2() {
		return permanentAddress2;
	}
	public void setPermanentAddress2(String permanentAddress2) {
		this.permanentAddress2 = permanentAddress2;
	}
	public String getPermanentCity() {
		return permanentCity;
	}
	public void setPermanentCity(String permanentCity) {
		this.permanentCity = permanentCity;
	}
	public String getPermanentPincode() {
		return permanentPincode;
	}
	public void setPermanentPincode(String permanentPincode) {
		this.permanentPincode = permanentPincode;
	}
	public String getSameAdressReg() {
		return sameAdressReg;
	}
	public void setSameAdressReg(String sameAdressReg) {
		this.sameAdressReg = sameAdressReg;
	}
	@JsonProperty("ResidenceAddressTwo")
	public String getResidenceAddressTwo() {
		return residenceAddressTwo;
	}
	public void setResidenceAddressTwo(String residenceAddressTwo) {
		this.residenceAddressTwo = residenceAddressTwo;
	}
	@JsonProperty("ResidenceCity")
	public String getResidenceCity() {
		return residenceCity;
	}
	public void setResidenceCity(String residenceCity) {
		this.residenceCity = residenceCity;
	}
	@JsonProperty("ResidencePinCode")
	public String getResidencePinCode() {
		return residencePinCode;
	}
	public void setResidencePinCode(String residencePinCode) {
		this.residencePinCode = residencePinCode;
	}
	public String getPasswordResetted() {
		return passwordResetted;
	}
	public void setPasswordResetted(String passwordResetted) {
		this.passwordResetted = passwordResetted;
	}
	public String getIsTransCheck() {
		return isTransCheck;
	}
	public void setIsTransCheck(String isTransCheck) {
		this.isTransCheck = isTransCheck;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getPermanentAddress3() {
		return permanentAddress3;
	}
	public void setPermanentAddress3(String permanentAddress3) {
		this.permanentAddress3 = permanentAddress3;
	}
	public String getPermanentAddress4() {
		return permanentAddress4;
	}
	public void setPermanentAddress4(String permanentAddress4) {
		this.permanentAddress4 = permanentAddress4;
	}
	@JsonProperty("ResidenceAddressThree")
	public String getResidenceAddressThree() {
		return residenceAddressThree;
	}
	public void setResidenceAddressThree(String residenceAddressThree) {
		this.residenceAddressThree = residenceAddressThree;
	}
	@JsonProperty("ResidenceAddressFour")
	public String getResidenceAddressFour() {
		return residenceAddressFour;
	}
	public void setResidenceAddressFour(String residenceAddressFour) {
		this.residenceAddressFour = residenceAddressFour;
	}
	/** Task #119207 MIBL for Phase II **/
	public String getStrStdCode() {
		return strStdCode;
	}
	public void setStrStdCode(String strStdCode) {
		this.strStdCode = strStdCode;
	}
	
	public String getStrPhoneNo() {
		return strPhoneNo;
	}
	public void setStrPhoneNo(String strPhoneNo) {
		this.strPhoneNo = strPhoneNo;
	}
	/** Task #119207 MIBL for Phase II **/
	public String getGstin() {
		return gstin;
	}
	public void setGstin(String gstin) {
		this.gstin = gstin;
	}
	public String getAadharNumber() {
		return aadharNumber;
	}
	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getEiaNumber() {
		return eiaNumber;
	}
	public void setEiaNumber(String eiaNumber) {
		this.eiaNumber = eiaNumber;
	}
	public String getIrName() {
		return irName;
	}
	public void setIrName(String irName) {
		this.irName = irName;
	}

	public String getUpdatePanAaadharLater() {
		return updatePanAaadharLater;
	}

	public void setUpdatePanAaadharLater(String updatePanAaadharLater) {
		this.updatePanAaadharLater = updatePanAaadharLater;
	}
}
