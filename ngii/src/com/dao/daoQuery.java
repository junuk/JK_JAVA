package com.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.vo.NgiiDataDomain;
import com.vo.NgiiLogDomain;


/**
 * @FileName  : daoQuery.java
 * @Project     : ngii
 * @Date         : 2015. 9. 21. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 : Dao
 */
public class daoQuery {

	private SqlSession dbSession = null;
	
	private String queryMsg = "sqlQuery.";
	
	public daoQuery(SqlSession dbSession)
	{
		this.dbSession = dbSession;
	}
	
	/**
	 * @Method Name  : getTsDataCnt
	 * @작성일   : 2015. 10. 1. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 :	파일 메타데이터 갯수 조회
	 * @param dataMapdmcNo
	 * @return
	 * @throws Exception
	 */
	public int getTsDataCnt(String dataMapdmcNo) throws Exception
	{
		return dbSession.selectOne(queryMsg + "getTsDataCnt", dataMapdmcNo);
	}
	
	/**
	 * @Method Name  : getTsData
	 * @작성일   : 2015. 10. 1. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 :	파일 메타데이터 조회
	 * @param dataMapdmcNo
	 * @return
	 * @throws Exception
	 */
	public NgiiDataDomain getTsData(NgiiDataDomain vo) throws Exception
	{
		return dbSession.selectOne(queryMsg + "getTsData", vo);
	}
	
	/**
	 * @Method Name  : insertMetData
	 * @작성일   : 2015. 10. 6. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 개별수치지형도 메타데이터 신규등록 (1건) [Step 1]
	 * @param vo
	 * @return
	 */
	public int insertMetData(NgiiDataDomain vo)
	{
		return dbSession.insert(queryMsg + "insertMetData", vo);
	}
	
	/**
	 * @Method Name  : insertMetDataSfpu
	 * @작성일   : 2015. 10. 6. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 개별수치지형도 메타데이터 특징 및 활용 신규등록 (1건) [Step 2]
	 * @param vo
	 * @return
	 */
	public int insertMetDataSfpu(NgiiDataDomain vo)
	{
		return dbSession.insert(queryMsg + "insertMetDataSfpu", vo);
	}
	
	/**
	 * @Method Name  : insertMetDataPrcUse
	 * @작성일   : 2015. 10. 6. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 개별수치지형도 메타데이터 활용 분류 및 기타 신규등록 (1건) [Step 3]
	 * @param vo
	 * @return
	 */
	public int insertMetDataPrcUse(NgiiDataDomain vo)
	{
		return dbSession.insert(queryMsg + "insertMetDataPrcUse", vo);
	}
	
	
	/**
	 * @Method Name  : selectListSidoSigungu
	 * @작성일   : 2015. 10. 6. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 도엽번호에 대한 행정구역 리스트를 조회 (시도 - 시군구)
	 * @param sMap
	 * @return
	 */
	public List<NgiiDataDomain> selectListSidoSigungu(HashMap<String, Object> sMap)
	{
		return dbSession.selectList(queryMsg + "selectListSidoSigungu", sMap);
	}
	
	/**
	 * @Method Name  : selectSidoSigungu
	 * @작성일   : 2015. 10. 6. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 도엽번호에 대한 행정구역 단건 조회 (시도 - 시군구)
	 * @param sMap
	 * @return
	 */
	public NgiiDataDomain selectSidoSigungu(HashMap<String, Object> sMap)
	{
		return (NgiiDataDomain) dbSession.selectList(queryMsg + "selectSidoSigungu", sMap);
	}
	
	/**
	 * @Method Name  : insertThumNail
	 * @작성일   : 2015. 10. 6. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 썸네일 Byte[] 저장
	 * @param vo
	 * @return
	 */
	public int insertThumNail(NgiiDataDomain vo)
	{
		return dbSession.insert(queryMsg + "insertThumNail", vo);
	}
	
	/**
	 * @Method Name  : uptTsData
	 * @작성일   : 2015. 10. 16. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 도엽번호에 따른 TS_DATA UPDATE 갱신처리
	 * @param vo
	 * @return
	 */
	public int uptTsData(NgiiDataDomain vo)
	{
		return dbSession.update(queryMsg + "uptTsData", vo);
	}
	
	/**
	 * @Method Name  : uptTsDataFile
	 * @작성일   : 2015. 10. 16. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 도엽번호에 따른 TS_DATA_FILE (공간정보파일) 갱신처리
	 * @param vo
	 * @return
	 */
	public int uptTsDataFile(NgiiDataDomain vo)
	{
		return dbSession.update(queryMsg + "uptTsDataFile", vo);
	}
	
	/**
	 * @Method Name  : insNgiiLog
	 * @작성일   : 2015. 10. 17. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 개별수치지형도 갱신 로그 입력
	 * @param vo
	 * @return
	 */
	public int insNgiiLog(NgiiLogDomain vo)
	{
		return dbSession.insert(queryMsg + "insNgiiLog", vo);
	}
	
}