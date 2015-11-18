package com.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.common.sdiException;
import com.common.sdiLog;


/**
 * @FileName  : NgiiStater.java
 * @Project     : ngii
 * @Date         : 2015. 9. 4. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 : 변동처리 스타터 클래스
 */
public class NgiiStater{
	
	
	/* 
	 * 로그 처리 
	*/
	private sdiLog logging = null;
	
	public NgiiStater() {
		super();
		this.logging = new sdiLog();
		this.logging.startLog();						// 인자없으면 일반 처리, 인자있으면 모듈용처리
	}
	
	/**
	 * @throws IOException 
	 * @Method Name  : CronTriggerStart
	 * @작성일   : 2015. 9. 4. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 :		스케줄러 수행 및 기능 별 실행 리모트 메서드
	 * @throws Exception
	 */
	public void CronTriggerStart() throws sdiException, IOException 
	{
		/*sdiMethod proc = new sdiMethod();
		
		// 프로퍼티파일 로딩
		Properties pro = new Properties();
		pro = proc.readProperties("property/ngii.properties");*/

		/*
		 *  개별수치지형도 갱신처리
		 */
		procNgii thread = new procNgii();
		try {
			/*
			 * [0] 초기화 설정 (DB Session, Query Handler)
			 */
			thread.init();
			
			/*
			 * [1] 프로세스 수행
			 */
			thread.process();
		}
		catch (sdiException e) {
			// <예외 (에러)> 처리
			switch (e.getType())
			{
				// 준비 오류
				case ABORT:
				// 처리 오류
				case ERROR:
					break;
				// 비정상 오류
				case ABNORMAL:					
					break;
				// 미확인 오류
				default:					
					break;
			}
		}
		catch (Exception e)
		{
			// 미예측 오류
			System.out.println(
				"*ERR* 예측되지 않은 오류가 발생하였습니다. => " + e.toString());
		}
		finally
		{
			// <PROCESS> 종료
			thread.clear();
		}
		
		/*
		 *  연속수치지형도 갱신처리
		 */
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		/*
		 * [00] 압축 테스트
		 */
		try {
			/*sdiMethod proc = new sdiMethod();
			//proc.sdiDirZip("C:/DEV_JK_TEST", "C:/DEV_JK/test.zip");			// 압축 타겟디렉토리 경로  // 압축 파일 저장경로 (같은 경로로 하지말 것)
			
			File zipFile = new File("C:/DEV_JK" + File.separator + "test.zip");			// 압축파일 경로+파일명
			File zipFileDir = new File("C:/DEV_JK");													// 압축을 해제할 경로
			proc.sdiUnZip(zipFile, zipFileDir, false);													// false는 압축 내부 파일명 그대로 출력 true는 소문자 변환
			*/
			/*
			 * 추가적으로 압축 풀기가 완료되면 기존 파일 지우는것??? 이게 필요하면 추가 구현 
			 */
			
		} catch (Exception e) {
			// TODO: DBIO Exception Process
			logging.error("===== (sdiMethod) 메인 에러!! \n" + e.getMessage());
		}

	}
	
	public static void main(String[] args) throws Exception {
		NgiiStater ngiict = new NgiiStater();
		ngiict.CronTriggerStart();
		
		
		// DB 테스트
		//dbTest();
		
	}
	
	/**
	 * @Method Name  : dbTest
	 * @작성일   : 2015. 9. 30. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : DB 테스트 (삭제예정)
	 * @throws IOException
	 */
	public static void dbTest() throws IOException
	{
		String res = "com/mapper/Configuration.xml";
		InputStream is = Resources.getResourceAsStream(res);
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
		SqlSession session = factory.openSession();
		
		try {
			String str = session.selectOne("sqlQuery.test1");
			System.out.println("str ----------> " + str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(session != null)
				session.close();
		}
	}


}
