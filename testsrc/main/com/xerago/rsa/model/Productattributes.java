package com.xerago.rsa.model;
// Generated 11 Mar, 2017 3:01:04 PM by Hibernate Tools 4.3.1

/**
 * Productattributes generated by hbm2java
 */
public class Productattributes  implements java.io.Serializable {
	
     private long id;
     private String productname;
     private String coveragename;
     private String attributename;
     private String attributetype;
     private String attributedatatype;
     private String attributevalue;
     private String attributelevel;
     private Long productid;
     private Long coverageid;

    public Productattributes() {
    }

	
    public Productattributes(long id) {
        this.id = id;
    }
    public Productattributes(long id, String productname, String coveragename, String attributename, String attributetype, String attributedatatype, String attributevalue, String attributelevel, Long productid, Long coverageid) {
       this.id = id;
       this.productname = productname;
       this.coveragename = coveragename;
       this.attributename = attributename;
       this.attributetype = attributetype;
       this.attributedatatype = attributedatatype;
       this.attributevalue = attributevalue;
       this.attributelevel = attributelevel;
       this.productid = productid;
       this.coverageid = coverageid;
    }
   
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public String getProductname() {
        return this.productname;
    }
    
    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getCoveragename() {
        return this.coveragename;
    }
    
    public void setCoveragename(String coveragename) {
        this.coveragename = coveragename;
    }

    public String getAttributename() {
        return this.attributename;
    }
    
    public void setAttributename(String attributename) {
        this.attributename = attributename;
    }

    public String getAttributetype() {
        return this.attributetype;
    }
    
    public void setAttributetype(String attributetype) {
        this.attributetype = attributetype;
    }

    public String getAttributedatatype() {
        return this.attributedatatype;
    }
    
    public void setAttributedatatype(String attributedatatype) {
        this.attributedatatype = attributedatatype;
    }

    public String getAttributevalue() {
        return this.attributevalue;
    }
    
    public void setAttributevalue(String attributevalue) {
        this.attributevalue = attributevalue;
    }

    public String getAttributelevel() {
        return this.attributelevel;
    }
    
    public void setAttributelevel(String attributelevel) {
        this.attributelevel = attributelevel;
    }

    public Long getProductid() {
        return this.productid;
    }
    
    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getCoverageid() {
        return this.coverageid;
    }
    
    public void setCoverageid(Long coverageid) {
        this.coverageid = coverageid;
    }

}


