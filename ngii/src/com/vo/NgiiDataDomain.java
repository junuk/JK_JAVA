package com.vo;

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @FileName  : NgiiDataDomain.java
 * @Project     : ngii
 * @Date         : 2015. 9. 30. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 : TS_DATA (데이터 이동을 위한 직렬화)
 */
public class NgiiDataDomain {

	/*
	 * 공간정보 데이터
	 */
	private String dataSn;				// 데이터 정보 일련번호
	private String metdataSn;			// 메타데이터 일련번호
	private String dataNm;				// 데이터 명
	private String dataFileFormat;		// 데이터 파일 포맷
	private String dataMapdmcNo;		// 데이터 도엽 번호
	private String dataMnfctDe;			// 데이터 제작 년월일
	private String prplcid;				// 가격정책식별자
	private String provdMthSecd;		// 제공 방법 구분코드 (507)
	private String sleMthSecd;			// 판매 방법 구분코드 (66)
	private String purchsPosblYn;		// 구매 가능 여부코드 (504)
	private String dataLinkUrl;			// 데이터 링크 URL
	private String dataSumryDc;			// 데이터 요약 설명
	private String dataVrifySecd;		// 데이터 검증 구분코드 (512)
	private String distbConfmClcd;		// 유통 승인 분류코드 (25)
	private String distbReturnResn;		// 유통 반려 사유
	private String distbConfmProcsDt;	// 유통 승인 처리 일시
	private String dataDeleteYn;		// 데이터 삭제 여부코드
	private String dataDeleteDt;		// 데이터 삭제 일시
	private String frstCreatDt;			// 최초 생성 일시
	private String lastUpdtDt;			// 최종 수정 일시
	
	/*
	 * 공간정보 파일 정보
	 */
	private String spatialFileName;		// 폴더 검색 파일 명 (확장자제외)
	private File spatialFile;			// 폴더 검색 파일
	private File subSpatialFile;		// NGI의 경우 NDA가 들어감
	private String spatialFileFormat;	// 폴더 검색 파일 포맷
	private String spatialFileDir;		// 폴더 검색 파일 경로 (파일명 포함)
	private long spatialFileSize;		// 폴더 검색 파일 사이즈
	
	/*
	 * 공간정보 데이터 파일
	 */
	private long dataFileSize;			// 데이터 파일 크기
	private String dataZipFileNm;		// 데이터 압축 파일 명
	private long dataZipFileSize;		// 데이터 압축 파일 크기
	private byte[] dataZipFileByte;		// 데이터 압축 파일 바이트
	
	// 신규추가
	private String dataFileDir;			// 데이터 파일 경로 (저장경로)
	private String dataCuGbn;			// 신규_수정 구분코드 (C : 신규, U : 기존수정)
	
	
	/*
	 * Getter & Setter
	 */
	
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}


	public long getSpatialFileSize() {
		return spatialFileSize;
	}


	public void setSpatialFileSize(long spatialFileSize) {
		this.spatialFileSize = spatialFileSize;
	}


	public String getSpatialFileName() {
		return spatialFileName;
	}


	public void setSpatialFileName(String spatialFileName) {
		this.spatialFileName = spatialFileName;
	}


	public File getSpatialFile() {
		return spatialFile;
	}


	public void setSpatialFile(File spatialFile) {
		this.spatialFile = spatialFile;
	}


	public String getSpatialFileFormat() {
		return spatialFileFormat;
	}


	public void setSpatialFileFormat(String spatialFileFormat) {
		this.spatialFileFormat = spatialFileFormat;
	}


	public String getSpatialFileDir() {
		return spatialFileDir;
	}


	public void setSpatialFileDir(String spatialFileDir) {
		this.spatialFileDir = spatialFileDir;
	}


	public long getDataFileSize() {
		return dataFileSize;
	}


	public void setDataFileSize(long dataFileSize) {
		this.dataFileSize = dataFileSize;
	}


	public String getDataZipFileNm() {
		return dataZipFileNm;
	}


	public void setDataZipFileNm(String dataZipFileNm) {
		this.dataZipFileNm = dataZipFileNm;
	}


	public long getDataZipFileSize() {
		return dataZipFileSize;
	}


	public void setDataZipFileSize(long dataZipFileSize) {
		this.dataZipFileSize = dataZipFileSize;
	}
	

	public byte[] getDataZipFileByte() {
		return dataZipFileByte;
	}


	public void setDataZipFileByte(byte[] dataZipFileByte) {
		this.dataZipFileByte = dataZipFileByte;
	}


	public String getDataFileDir() {
		return dataFileDir;
	}


	public void setDataFileDir(String dataFileDir) {
		this.dataFileDir = dataFileDir;
	}

	public File getSubSpatialFile() {
		return subSpatialFile;
	}


	public void setSubSpatialFile(File subSpatialFile) {
		this.subSpatialFile = subSpatialFile;
	}


	public String getDataCuGbn() {
		return dataCuGbn;
	}


	public void setDataCuGbn(String dataCuGbn) {
		this.dataCuGbn = dataCuGbn;
	}


	public String getDataSn() {
		return dataSn;
	}

	public void setDataSn(String dataSn) {
		this.dataSn = dataSn;
	}

	public String getMetdataSn() {
		return metdataSn;
	}

	public void setMetdataSn(String metdataSn) {
		this.metdataSn = metdataSn;
	}

	public String getDataNm() {
		return dataNm;
	}

	public void setDataNm(String dataNm) {
		this.dataNm = dataNm;
	}

	public String getDataFileFormat() {
		return dataFileFormat;
	}

	public void setDataFileFormat(String dataFileFormat) {
		this.dataFileFormat = dataFileFormat;
	}

	public String getDataMapdmcNo() {
		return dataMapdmcNo;
	}

	public void setDataMapdmcNo(String dataMapdmcNo) {
		this.dataMapdmcNo = dataMapdmcNo;
	}

	public String getDataMnfctDe() {
		return dataMnfctDe;
	}

	public void setDataMnfctDe(String dataMnfctDe) {
		this.dataMnfctDe = dataMnfctDe;
	}

	public String getPrplcid() {
		return prplcid;
	}

	public void setPrplcid(String prplcid) {
		this.prplcid = prplcid;
	}

	public String getProvdMthSecd() {
		return provdMthSecd;
	}

	public void setProvdMthSecd(String provdMthSecd) {
		this.provdMthSecd = provdMthSecd;
	}

	public String getSleMthSecd() {
		return sleMthSecd;
	}

	public void setSleMthSecd(String sleMthSecd) {
		this.sleMthSecd = sleMthSecd;
	}

	public String getPurchsPosblYn() {
		return purchsPosblYn;
	}

	public void setPurchsPosblYn(String purchsPosblYn) {
		this.purchsPosblYn = purchsPosblYn;
	}

	public String getDataLinkUrl() {
		return dataLinkUrl;
	}

	public void setDataLinkUrl(String dataLinkUrl) {
		this.dataLinkUrl = dataLinkUrl;
	}

	public String getDataSumryDc() {
		return dataSumryDc;
	}

	public void setDataSumryDc(String dataSumryDc) {
		this.dataSumryDc = dataSumryDc;
	}

	public String getDataVrifySecd() {
		return dataVrifySecd;
	}

	public void setDataVrifySecd(String dataVrifySecd) {
		this.dataVrifySecd = dataVrifySecd;
	}

	public String getDistbConfmClcd() {
		return distbConfmClcd;
	}

	public void setDistbConfmClcd(String distbConfmClcd) {
		this.distbConfmClcd = distbConfmClcd;
	}

	public String getDistbReturnResn() {
		return distbReturnResn;
	}

	public void setDistbReturnResn(String distbReturnResn) {
		this.distbReturnResn = distbReturnResn;
	}

	public String getDistbConfmProcsDt() {
		return distbConfmProcsDt;
	}

	public void setDistbConfmProcsDt(String distbConfmProcsDt) {
		this.distbConfmProcsDt = distbConfmProcsDt;
	}

	public String getDataDeleteYn() {
		return dataDeleteYn;
	}

	public void setDataDeleteYn(String dataDeleteYn) {
		this.dataDeleteYn = dataDeleteYn;
	}

	public String getDataDeleteDt() {
		return dataDeleteDt;
	}

	public void setDataDeleteDt(String dataDeleteDt) {
		this.dataDeleteDt = dataDeleteDt;
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
	

}
