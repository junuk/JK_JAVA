package com.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @FileName  : NgiiDataImageDomain.java
 * @Project     : ngii
 * @Date         : 2015. 10. 14. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 :	공간정보 데이터 이미지 VO
 */
public class NgiiDataImageDomain implements Serializable{

	private static final long serialVersionUID = 1L;

	private String dataSn;			// 데이터 정보 일련번호
	private String imageOrdr;		// 이미지 순번
	private String imageByte;		// 이미지 파일
	
	
	public String getDataSn() {
		return dataSn;
	}
	public void setDataSn(String dataSn) {
		this.dataSn = dataSn;
	}
	public String getImageOrdr() {
		return imageOrdr;
	}
	public void setImageOrdr(String imageOrdr) {
		this.imageOrdr = imageOrdr;
	}
	public String getImageByte() {
		return imageByte;
	}
	public void setImageByte(String imageByte) {
		this.imageByte = imageByte;
	}
	
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
