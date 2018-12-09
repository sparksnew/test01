package com.oristartech.cinema.pojo;

import java.sql.Timestamp;
import java.util.Date;

public class Dw_Dim_Cinema_Info {
	 private   Integer     ID      		 ;	  		//'����ID'  
	 private   String      CINEMA_CODE   ;	 		//'ӰԺ����'  
	 private   String      CINEMA_NAME   ;	 		//'ӰԺ����'  
	 private   Integer     STATUS    	;  		//'ӰԺ״̬��1 Ӫҵ  2���� '  
	 private   Integer     CINEMA_INDEX  ;	  		//'ӰԺ����'  
	 private   String      CONTACT_MAN   ;	 		//'��ϵ��'  
	 private   String      CINEMA_PHONE  ;	 		//'ӰԺ��ϵ�绰'  
	 private   String      CINEMA_ADDRESS;    		//'ӰԺ��ַ'  
	 private   String      CINEMA_COMPANY;    		//'ӰԺ������˾'  
	 private   Integer     CREATE_USER   ;	  		//'�����û�����Ӧ�û���Ψһ��ʶ��INDEX_'  
	 private   Timestamp   CREATE_TIME   ;	 		//'����ʱ��'  
	 private   Integer     UPDATE_USER   ;	  		//'�޸��û�����Ӧ�û���Ψһ��ʶ��INDEX_'  
	 private   Date 	     UPDATE_TIME ;  		
	 private   String      ORG_CODE    	; 		
	 private   String      GROUP_CODE    ;	 		
	 private   String      AREA_CODE    ;	 	
	 private   Date 	     OPEN_TIME   ;			//'��ҵʱ��'  
	 private   String      ALIAS_NAME    ;	 		//'����'  
	 private   String      MBRSYS_CODE   ;	 		//'��Աϵͳ����'  
	 private   String      CMS_VERSION_NO;    		//'CMSϵͳ�汾��'  
	 private   String      CRM_VERSION_NO;    		//'CRMϵͳ�汾��'  
	 private   String      CINEMAS;    		 		//'����Ժ������' 
	 private   Integer  count; 
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getCINEMA_CODE() {
		return CINEMA_CODE;
	}
	public void setCINEMA_CODE(String cINEMA_CODE) {
		CINEMA_CODE = cINEMA_CODE;
	}
	public String getCINEMA_NAME() {
		return CINEMA_NAME;
	}
	public void setCINEMA_NAME(String cINEMA_NAME) {
		CINEMA_NAME = cINEMA_NAME;
	}
	public Integer getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(Integer sTATUS) {
		STATUS = sTATUS;
	}
	public Integer getCINEMA_INDEX() {
		return CINEMA_INDEX;
	}
	public void setCINEMA_INDEX(Integer cINEMA_INDEX) {
		CINEMA_INDEX = cINEMA_INDEX;
	}
	public String getCONTACT_MAN() {
		return CONTACT_MAN;
	}
	public void setCONTACT_MAN(String cONTACT_MAN) {
		CONTACT_MAN = cONTACT_MAN;
	}
	public String getCINEMA_PHONE() {
		return CINEMA_PHONE;
	}
	public void setCINEMA_PHONE(String cINEMA_PHONE) {
		CINEMA_PHONE = cINEMA_PHONE;
	}
	public String getCINEMA_ADDRESS() {
		return CINEMA_ADDRESS;
	}
	public void setCINEMA_ADDRESS(String cINEMA_ADDRESS) {
		CINEMA_ADDRESS = cINEMA_ADDRESS;
	}
	public String getCINEMA_COMPANY() {
		return CINEMA_COMPANY;
	}
	public void setCINEMA_COMPANY(String cINEMA_COMPANY) {
		CINEMA_COMPANY = cINEMA_COMPANY;
	}
	public Integer getCREATE_USER() {
		return CREATE_USER;
	}
	public void setCREATE_USER(Integer cREATE_USER) {
		CREATE_USER = cREATE_USER;
	}
	public Timestamp getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(Timestamp cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	public Integer getUPDATE_USER() {
		return UPDATE_USER;
	}
	public void setUPDATE_USER(Integer uPDATE_USER) {
		UPDATE_USER = uPDATE_USER;
	}
	public Date getUPDATE_TIME() {
		return UPDATE_TIME;
	}
	public void setUPDATE_TIME(Date uPDATE_TIME) {
		UPDATE_TIME = uPDATE_TIME;
	}
	public String getORG_CODE() {
		return ORG_CODE;
	}
	public void setORG_CODE(String oRG_CODE) {
		ORG_CODE = oRG_CODE;
	}
	public String getGROUP_CODE() {
		return GROUP_CODE;
	}
	public void setGROUP_CODE(String gROUP_CODE) {
		GROUP_CODE = gROUP_CODE;
	}
	public String getAREA_CODE() {
		return AREA_CODE;
	}
	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}
	public Date getOPEN_TIME() {
		return OPEN_TIME;
	}
	public void setOPEN_TIME(Date oPEN_TIME) {
		OPEN_TIME = oPEN_TIME;
	}
	public String getALIAS_NAME() {
		return ALIAS_NAME;
	}
	public void setALIAS_NAME(String aLIAS_NAME) {
		ALIAS_NAME = aLIAS_NAME;
	}
	public String getMBRSYS_CODE() {
		return MBRSYS_CODE;
	}
	public void setMBRSYS_CODE(String mBRSYS_CODE) {
		MBRSYS_CODE = mBRSYS_CODE;
	}
	public String getCMS_VERSION_NO() {
		return CMS_VERSION_NO;
	}
	public void setCMS_VERSION_NO(String cMS_VERSION_NO) {
		CMS_VERSION_NO = cMS_VERSION_NO;
	}
	public String getCRM_VERSION_NO() {
		return CRM_VERSION_NO;
	}
	public void setCRM_VERSION_NO(String cRM_VERSION_NO) {
		CRM_VERSION_NO = cRM_VERSION_NO;
	}
	public String getCINEMAS() {
		return CINEMAS;
	}
	public void setCINEMAS(String cINEMAS) {
		CINEMAS = cINEMAS;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	 
}
