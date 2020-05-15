package com.xerago.rsa.model;



import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="D_TP_SURVEYOR_USE_DETAILS")
@GenericGenerator(name = "auth-generator", strategy = "sequence-identity", parameters = @org.hibernate.annotations.Parameter(name = "sequence", value = "D_TP_SURVEYOR_USE_DETAILS_SEQ"))
public class DTpSurveyorUseDetails  implements java.io.Serializable {


     private BigDecimal versionNo;
     private String quoteId;
     private Clob request;
     private Clob response;
     private String status;
     private Date requestTime;
     private Date responseTime;
     private String serviceName;

    public DTpSurveyorUseDetails() {
    }

	
    public DTpSurveyorUseDetails(BigDecimal versionNo, String quoteId) {
        this.versionNo = versionNo;
        this.quoteId = quoteId;
    }
    public DTpSurveyorUseDetails(BigDecimal versionNo, String quoteId, Clob request, Clob response, String status, Date requestTime, Date responseTime, String serviceName) {
       this.versionNo = versionNo;
       this.quoteId = quoteId;
       this.request = request;
       this.response = response;
       this.status = status;
       this.requestTime = requestTime;
       this.responseTime = responseTime;
       this.serviceName = serviceName;
    }
    @Id 
	@GeneratedValue(generator = "auth-generator")
	@Column(name = "VERSION_NO", unique = true, nullable = false, precision = 30, scale = 0)
    public BigDecimal getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(BigDecimal versionNo) {
        this.versionNo = versionNo;
    }
    
    @Column(name = "QUOTE_ID",length=30)
    public String getQuoteId() {
        return this.quoteId;
    }
    
    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }
    
    @Column(name = "REQUEST")
    public Clob getRequest() {
        return this.request;
    }
    
    public void setRequest(Clob request) {
        this.request = request;
    }
    @Column(name = "RESPONSE")
    public Clob getResponse() {
        return this.response;
    }
    
    public void setResponse(Clob response) {
        this.response = response;
    }
    @Column(name = "STATUS", length = 3)
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name="REQUEST_TIME", nullable=false, length=7)
    public Date getRequestTime() {
        return this.requestTime;
    }
    
    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }
    @Column(name="RESPONSE_TIME", nullable=false, length=7)
    public Date getResponseTime() {
        return this.responseTime;
    }
    
    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }
    @Column(name="SERVICE_NAME", length=100)
    public String getServiceName() {
        return this.serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }




}


