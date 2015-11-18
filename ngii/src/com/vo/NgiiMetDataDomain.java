package com.vo;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @FileName  : NgiiMetDataDomain.java
 * @Project     : ngii
 * @Date         : 2015. 9. 30. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 : TS_Metdata (데이터 전송이 필요하므로 직렬화 선언)
 */
public class NgiiMetDataDomain implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * 공간정보 메타데이터 (마스터)
	 */
	private String metdataSn;		// 메타데이터 일련번호
	private String supplierid;		// 공급자 ID
	private String registSecd;		// 등록_구분코드 (511)
	private String metdataSj;		// 메타데이터 제목
	private String sptlLclasClcd;	// 공간정보 분야 분류코드(대) (509, 510)
	private String sptlMclasClcd;	// 공간정보 분야 분류코드(중) (651~653, 701~715)
	private String ovalClcd;		// 타원체 분류코드 (502)
	private String cntmClcd;		// 좌표계 분류코드 (503)
	private String trgnptClcd;		// 원점 구분코드 (601 ~ 650)
	private String scClcd;			// 축척 분류코드 (501)
	private String scClcdNm;		// 축척 분류코드 명
	private String updtCycleClcd;	// 갱신 주기 분류코드 (505)
	private String frstCreatDt;		// 최초 생성 일시
	private String lastUpdtDt;		// 최종 수정 일시
	
	private ArrayList<NgiiDataDomain> ngiiDataDomainList;			// 공간정보 데이터 리스트
	private ArrayList<NgiiDataFileDomain> ngiiDataFileDomainList;	// 공간정보 데이터 파일 리스트
	private ArrayList<NgiiDataImageDomain> ngiiDataImageDomain;		// 공간정보 이미지 파일 리스트
	
	
	/*
	 * Getter & Setter
	 */
	
	public String getMetdataSn() {
		return metdataSn;
	}
	public ArrayList<NgiiDataDomain> getNgiiDataDomainList() {
		return ngiiDataDomainList;
	}
	public void setNgiiDataDomainList(ArrayList<NgiiDataDomain> ngiiDataDomainList) {
		this.ngiiDataDomainList = ngiiDataDomainList;
	}
	public ArrayList<NgiiDataFileDomain> getNgiiDataFileDomainList() {
		return ngiiDataFileDomainList;
	}
	public void setNgiiDataFileDomainList(
			ArrayList<NgiiDataFileDomain> ngiiDataFileDomainList) {
		this.ngiiDataFileDomainList = ngiiDataFileDomainList;
	}
	public ArrayList<NgiiDataImageDomain> getNgiiDataImageDomain() {
		return ngiiDataImageDomain;
	}
	public void setNgiiDataImageDomain(
			ArrayList<NgiiDataImageDomain> ngiiDataImageDomain) {
		this.ngiiDataImageDomain = ngiiDataImageDomain;
	}
	public void setMetdataSn(String metdataSn) {
		this.metdataSn = metdataSn;
	}
	public String getSupplierid() {
		return supplierid;
	}
	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid;
	}
	public String getRegistSecd() {
		return registSecd;
	}
	public void setRegistSecd(String registSecd) {
		this.registSecd = registSecd;
	}
	public String getMetdataSj() {
		return metdataSj;
	}
	public void setMetdataSj(String metdataSj) {
		this.metdataSj = metdataSj;
	}
	public String getSptlLclasClcd() {
		return sptlLclasClcd;
	}
	public void setSptlLclasClcd(String sptlLclasClcd) {
		this.sptlLclasClcd = sptlLclasClcd;
	}
	public String getSptlMclasClcd() {
		return sptlMclasClcd;
	}
	public void setSptlMclasClcd(String sptlMclasClcd) {
		this.sptlMclasClcd = sptlMclasClcd;
	}
	public String getOvalClcd() {
		return ovalClcd;
	}
	public void setOvalClcd(String ovalClcd) {
		this.ovalClcd = ovalClcd;
	}
	public String getCntmClcd() {
		return cntmClcd;
	}
	public void setCntmClcd(String cntmClcd) {
		this.cntmClcd = cntmClcd;
	}
	public String getTrgnptClcd() {
		return trgnptClcd;
	}
	public void setTrgnptClcd(String trgnptClcd) {
		this.trgnptClcd = trgnptClcd;
	}
	public String getScClcd() {
		return scClcd;
	}
	public void setScClcd(String scClcd) {
		this.scClcd = scClcd;
	}
	public String getScClcdNm() {
		return scClcdNm;
	}
	public void setScClcdNm(String scClcdNm) {
		this.scClcdNm = scClcdNm;
	}
	public String getUpdtCycleClcd() {
		return updtCycleClcd;
	}
	public void setUpdtCycleClcd(String updtCycleClcd) {
		this.updtCycleClcd = updtCycleClcd;
	}
	public String getFrstCreatDt() {
		return frstCreatDt;
	}
	public void setFrstCreatDt(String frstCreatDt) {
		this.frstCreatDt = frstCreatDt;
	}
	public String getLastUpdtDt() {
		return lastUpdtDt;
	}
	public void setLastUpdtDt(String lastUpdtDt) {
		this.lastUpdtDt = lastUpdtDt;
	}


	/**
	 * @Method Name  : ToString
	 * @작성일   : 2015. 9. 30. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : Domain 객체 toString
	 * @return
	 */
	public String ToString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
