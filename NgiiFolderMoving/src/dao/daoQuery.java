package dao;

import org.apache.ibatis.session.SqlSession;

import vo.NgiiGDBHistDomain;
import vo.NgiiLogDomain;


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
	 * @Method Name  : addThNrmCstdyHist
	 * @작성일   : 2015. 11. 12. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 연속수치지형도 GDB 폴더 갱신 이력 등록 (추출 모듈에서 읽을 용도)
	 * @param vo
	 * @return
	 */
	public int addThNrmCstdyHist(NgiiGDBHistDomain vo)
	{
		return dbSession.insert(queryMsg + "addThNrmCstdyHist", vo);
	}
	
	/**
	 * @Method Name  : insNgiiLog
	 * @작성일   : 2015. 11. 13. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 연속수치지형도 GDB 수신 및 갱신 이력 등록 (웹페이지용)
	 * @param vo
	 * @return
	 */
	public int insNgiiLog(NgiiLogDomain vo)
	{
		return dbSession.insert(queryMsg + "insNgiiLog", vo);
	}
	
	
}