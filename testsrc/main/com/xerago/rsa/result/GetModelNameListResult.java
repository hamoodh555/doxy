package com.xerago.rsa.result;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MODELNAMELIST")
public class GetModelNameListResult extends Result {

	private String modelNameList;

	public String getModelNameList() {
		return modelNameList;
	}

	public void setModelNameList(String modelNameList) {
		this.modelNameList = modelNameList;
	}

}
