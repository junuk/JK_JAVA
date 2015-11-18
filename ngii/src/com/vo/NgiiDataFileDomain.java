package com.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @FileName  : NgiiDataFileDomain.java
 * @Project     : ngii
 * @Date         : 2015. 10. 14. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 : 공간정보 데이터 파일 VO
 */
public class NgiiDataFileDomain implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String dataSn;				// 데이터 정보 일련번호
	private String dataFileSize;		// 데이터 파일 크기
	private String dataZipFileNm;		// 데이터 압축 파일명
	private String dataZipFileSize;		// 데이터 압축 파일 크기
	private String dataZipFileByte;		// 데이터 압축 파일 바이트
	
	
	public String getDataSn() {
		return dataSn;
	}
	public void setDataSn(String dataSn) {
		this.dataSn = dataSn;
	}
	public String getDataFileSize() {
		return dataFileSize;
	}
	public void setDataFileSize(String dataFileSize) {
		this.dataFileSize = dataFileSize;
	}
	public String getDataZipFileNm() {
		return dataZipFileNm;
	}
	public void setDataZipFileNm(String dataZipFileNm) {
		this.dataZipFileNm = dataZipFileNm;
	}
	public String getDataZipFileSize() {
		return dataZipFileSize;
	}
	public void setDataZipFileSize(String dataZipFileSize) {
		this.dataZipFileSize = dataZipFileSize;
	}
	public String getDataZipFileByte() {
		return dataZipFileByte;
	}
	public void setDataZipFileByte(String dataZipFileByte) {
		this.dataZipFileByte = dataZipFileByte;
	}
	
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	


}
