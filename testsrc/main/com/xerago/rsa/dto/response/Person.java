package com.xerago.rsa.dto.response;

public class Person {

	private String title;
	private String firstName;
	private String lastName;
	private String fullName;
	private String gender;
	private String dateOfBirth;
	private String emailId;
	private String mobileNo;
	private String stdCode;
	private String phoneNo;
	private String addressOne;
	private String addressTwo;
	private String addressThree;
	private String addressFour;
	private String addressFive;
	private String city;
	private String cityOther;
	private String pincode;
	private String state;
	private String income;
	private String mstatus;
	private String occupation;
	private String occupationOther;
	
	private String areYouPoliticallyExposed;
	private String nomineeTitle;
	private String nomineeFirstName;
	private String nomineeLastName;
	private String nomineeAge;
	private String nomineeRelationship;
	
	private String nomineeGuardianTitle;
	private String nomineeGuardianFirstName;
	private String nomineeGuardianLastName;
	private String nomineeGuardianAge;
	private String nomineeGuardianRelationship;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		updateFullName();
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		if( !"Last Name (Optional)".equalsIgnoreCase(lastName) ){
			this.lastName = lastName;	
		}
		updateFullName();
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getStdCode() {
		return stdCode;
	}
	public void setStdCode(String stdCode) {
		if( !"STD".equalsIgnoreCase(stdCode) ){
			this.stdCode = stdCode;	
		}
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		if( !"Optional".equalsIgnoreCase(phoneNo) ){
			this.phoneNo = phoneNo;	
		}
	}
	public String getAddressOne() {
		return addressOne;
	}
	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}
	public String getAddressTwo() {
		return addressTwo;
	}
	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}
	public String getAddressThree() {
		return addressThree;
	}
	public void setAddressThree(String addressThree) {
		this.addressThree = addressThree;
	}
	public String getAddressFour() {
		return addressFour;
	}
	public void setAddressFour(String addressFour) {
		this.addressFour = addressFour;
	}
	public String getAddressFive() {
		return addressFive;
	}
	public void setAddressFive(String addressFive) {
		this.addressFive = addressFive;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityOther() {
		return cityOther;
	}
	public void setCityOther(String cityOther) {
		this.cityOther = cityOther;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}

	public String getMstatus() {
		return mstatus;
	}
	public void setMstatus(String mstatus) {
		this.mstatus = mstatus;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getOccupationOther() {
		return occupationOther;
	}
	public void setOccupationOther(String occupationOther) {
		this.occupationOther = occupationOther;
	}
	public String getAreYouPoliticallyExposed() {
		return areYouPoliticallyExposed;
	}
	public void setAreYouPoliticallyExposed(String areYouPoliticallyExposed) {
		this.areYouPoliticallyExposed = areYouPoliticallyExposed;
	}
	public String getNomineeTitle() {
		return nomineeTitle;
	}
	public void setNomineeTitle(String nomineeTitle) {
		this.nomineeTitle = nomineeTitle;
	}
	public String getNomineeFirstName() {
		return nomineeFirstName;
	}
	public void setNomineeFirstName(String nomineeFirstName) {
		this.nomineeFirstName = nomineeFirstName;
	}
	public String getNomineeLastName() {
		return nomineeLastName;
	}
	public void setNomineeLastName(String nomineeLastName) {
		this.nomineeLastName = nomineeLastName;
	}
	public String getNomineeAge() {
		return nomineeAge;
	}
	public void setNomineeAge(String nomineeAge) {
		this.nomineeAge = nomineeAge;
	}
	public String getNomineeRelationship() {
		return nomineeRelationship;
	}
	public void setNomineeRelationship(String nomineeRelationship) {
		this.nomineeRelationship = nomineeRelationship;
	}
	public String getNomineeGuardianTitle() {
		return nomineeGuardianTitle;
	}
	public void setNomineeGuardianTitle(String nomineeGuardianTitle) {
		this.nomineeGuardianTitle = nomineeGuardianTitle;
	}
	public String getNomineeGuardianFirstName() {
		return nomineeGuardianFirstName;
	}
	public void setNomineeGuardianFirstName(String nomineeGuardianFirstName) {
		this.nomineeGuardianFirstName = nomineeGuardianFirstName;
	}
	public String getNomineeGuardianLastName() {
		return nomineeGuardianLastName;
	}
	public void setNomineeGuardianLastName(String nomineeGuardianLastName) {
		this.nomineeGuardianLastName = nomineeGuardianLastName;
	}
	public String getNomineeGuardianAge() {
		return nomineeGuardianAge;
	}
	public void setNomineeGuardianAge(String nomineeGuardianAge) {
		this.nomineeGuardianAge = nomineeGuardianAge;
	}
	public String getNomineeGuardianRelationship() {
		return nomineeGuardianRelationship;
	}
	public void setNomineeGuardianRelationship(String nomineeGuardianRelationship) {
		this.nomineeGuardianRelationship = nomineeGuardianRelationship;
	}
	
	private void updateFullName(){
		if( this.firstName != null){
			if(this.lastName != null){
				this.fullName = firstName + " " + lastName;
			}else{
				this.fullName = firstName;
			}
		}
	}
	
}
