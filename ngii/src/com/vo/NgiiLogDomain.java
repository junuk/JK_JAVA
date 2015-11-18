package com.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @FileName  : NgiiLogDomain.java
 * @Project     : ngii
 * @Date         : 2015. 10. 16. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 : 지리원 연계데이터 갱신 로그 VO
 */
public class NgiiLogDomain {
	
	private String dataSn;				// 타겟데이터순번 (TS_DATA)
	private String ngiiUptnSecd;		// 데이터 구분 (공통코드 : 115, 01(수치1.0) 02(수치2.0) 03(연속수치)
	private String ngiiUptnDataNm;		// 데이터 명 (개별수치 : 갱신파일명, 연속수치 : 공통명 (전국 연속수치지형도 GDB)
	private String ngiiUptnDept;		// 갱신기관 (국토지리정보원)
	
	
	
	public String getDataSn() {
		return dataSn;
	}



	public void setDataSn(String dataSn) {
		this.dataSn = dataSn;
	}



	public String getNgiiUptnSecd() {
		return ngiiUptnSecd;
	}



	public void setNgiiUptnSecd(String ngiiUptnSecd) {
		this.ngiiUptnSecd = ngiiUptnSecd;
	}



	public String getNgiiUptnDataNm() {
		return ngiiUptnDataNm;
	}



	public void setNgiiUptnDataNm(String ngiiUptnDataNm) {
		this.ngiiUptnDataNm = ngiiUptnDataNm;
	}



	public String getNgiiUptnDept() {
		return ngiiUptnDept;
	}



	public void setNgiiUptnDept(String ngiiUptnDept) {
		this.ngiiUptnDept = ngiiUptnDept;
	}



	public String ToString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	

}
