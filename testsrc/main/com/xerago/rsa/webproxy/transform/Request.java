/**
 * 
 */
package com.xerago.rsa.webproxy.transform;

import com.xerago.rsa.model.MotorInsuranceModel;

/**
 * @author pandiaraj
 *
 */
public interface Request {
	
	<T> MotorInsuranceModel parseValue(T t) throws Exception;
	
	<T> void googleCampaignCodeCapture(T t, MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	<T> void setIDV_PreviousPolicyDetails(T t, MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	<T> void setCovers(T t, MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	<T> void setAdditionalDetails(T t, MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	<T> void setNominationforPA(T t, MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	<T> void setContactAddress(T t, MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	<T> void setVehicleRegistrationAddress(T t, MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	<T> void setLongTermProductValues(MotorInsuranceModel motorInsuranceModel) throws Exception;

}
