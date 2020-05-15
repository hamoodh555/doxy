package com.xerago.rsa.communication;

import java.util.HashMap;

import com.xerago.rsa.dto.response.MakerModelResponse;
import com.xerago.rsa.dto.response.PremiumDetails;
import com.xerago.rsa.model.MotorInsuranceModel;

public interface IPCServices {

	/**
	 * @param vehicleRegisteredCity
	 * @param motorInsuranceModel
	 * @throws Exception
	 */
	void getManufacturerNameList(MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	/**
	 * @param motorInsuranceModel
	 * @throws Exception
	 */
	void getModelIdvResult(MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	/**
	 * @param motorInsuranceModel
	 * @throws Exception
	 */
	void customerRegistration(MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	void setContactState(MotorInsuranceModel motorInsuranceModel) throws Exception;
	
	PremiumDetails fileUpload(HashMap<String, String> requestMap) throws Exception;
	
	PremiumDetails checkUploadStatus(HashMap<String, String> requestMap) throws Exception;
	
	MakerModelResponse getVahanServicePlan(MotorInsuranceModel motorInsuranceModel , String Subline) throws Exception;
}
