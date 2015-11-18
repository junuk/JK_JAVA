/**
 * 
 */
package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import com.common.enException;
import com.common.enTransaction;
import com.common.sdiDB;
import com.common.sdiException;
import com.common.sdiLog;
import com.common.sdiMethod;
import com.common.sdiZip;
import com.dao.daoQuery;
import com.vo.NgiiDataDomain;
import com.vo.NgiiLogDomain;

/**
 * @FileName  : procNgii.java
 * @Project     : ngii
 * @Date         : 2015. 9. 14. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 : 지리원 변동자료 처리 Proc
 */
public class procNgii {
	
	// Log
	private sdiLog logging = null;
	
	// 쿼리 핸들러
	private daoQuery dao = null;
	
	// DB 연결_트랜잭션 처리
	private sdiDB db = null;
	
	// 프로퍼티파일
	Properties pro = null; 
	
	// 공통 메서드 정의
	sdiMethod proc = null; 
	
	// 압축 메서드 정의
	sdiZip zipProc = null;
	
	// Common Proc
	
	public procNgii() throws IOException
	{
		this.logging = new sdiLog();
		this.db = new sdiDB();
		this.logging.startLog();
		this.pro = new Properties();
		this.proc = new sdiMethod();
		this.pro = this.proc.readProperties("property/ngii.properties");
		this.zipProc = new sdiZip();
	}
	
	/**
	 * @Method Name  : process
	 * @작성일   : 2015. 9. 30. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 기능 프로세스
	 * @throws sdiException
	 * @throws IOException
	 */
	public void process() throws sdiException, IOException
	{
		/*
		 * [0] 프로세스 시작
		 */
		try {
			logging.info("===== (start) 처리 시작 =====");
			this.start();
		} catch (sdiException e) {
			throw this.error(e);
		}
		
		/*
		 * [1] 지정 디렉토리 조회 
		 */
		ArrayList<NgiiDataDomain> cList = null;
		try {
			cList = new ArrayList<NgiiDataDomain>();
			this.proc.subDirList(cList, this.pro.getProperty("ngii.one.file.dir"));
		} catch (Exception e) {
			throw this.error(
					new sdiException(enException.ERROR, "개별수치지형도 변동자료 적재 디렉토리 조회 중 오류가 발생하였습니다.", e)
					);
		}
		
		/*
		 * [2] 갱신데이터 처리
		 */
		try {
			NgiiDataDomain temp = null;
			for(int cnt=0; cnt<cList.size(); cnt++)
			{
				this.logging.info("*******************************************************************************");
				this.logging.info("*******************************************************************************");
				this.logging.info("***** 개별수치지형도 갱신 데이터 처리를 수행합니다.");
				// metdataSn Setting (NGI/DXF)
				this.logging.info("***** 개별수치지형도 갱신데이터 메타데이터 일련번호를 확인합니다. (TS_MATDATA)");
				if(cList.get(cnt).getSpatialFileFormat().equals("NGI") || cList.get(cnt).getSpatialFileFormat().equals("ngi"))
					cList.get(cnt).setMetdataSn(this.pro.getProperty("ngii.one.metdata.metdatasn.ngi"));
				else if(cList.get(cnt).getSpatialFileFormat().equals("DXF") || cList.get(cnt).getSpatialFileFormat().equals("dxf"))
					cList.get(cnt).setMetdataSn(this.pro.getProperty("ngii.one.metdata.metdatasn.dxf"));
					
				// get dataSn (metdataSn)
				this.logging.info("***** 개별수치지형도 갱신데이터 메타데이터 일련번호에 해당하는 데이터 일련번호를 채번합니다 (TS_DATA)");
				temp = new NgiiDataDomain();
				temp = this.dao.getTsData(cList.get(cnt));
				cList.get(cnt).setDataSn(temp.getDataSn());
				cList.get(cnt).setDataNm(temp.getDataNm());
				
				int retCode;
				FileInputStream fis = null;		
				byte[] buf = null;				
				
				// TS_DATA Update (고시일, 갱신일) 통과해야 압축 후 TS_DATA_FILE 업데이트 
				if((retCode = this.dao.uptTsData(cList.get(cnt))) != 0)
				{
					this.logging.info("***** 개별수치지형도 갱신데이터 압축파일 생성 및 파일정보를 생성합니다...");
					// target File Zip
					this.zipProc.zip(cList.get(cnt));
					
					// Zip File Info Setting
					String targetZipFile = cList.get(cnt).getSpatialFileDir() + File.separator + cList.get(cnt).getSpatialFileName() + ".zip";
					cList.get(cnt).setDataZipFileNm(cList.get(cnt).getSpatialFileName() + ".zip");
					
					fis = new FileInputStream(new File(targetZipFile));
					buf = new byte[(int) new File(targetZipFile).length()];
					fis.read(buf, 0, buf.length);
					cList.get(cnt).setDataZipFileByte(buf);				// Zip File byte Setting
					cList.get(cnt).setDataZipFileSize(new File(targetZipFile).length());	// Zip File Size Setting (Long)
					
					this.logging.info("***** 압축 파일명			: " + cList.get(cnt).getDataZipFileNm());
					this.logging.info("***** 압축 사이즈			: " + cList.get(cnt).getDataZipFileSize());
					//this.logging.info("***** 압축 바이트			: " + cList.get(cnt).getDataZipFileByte());
					this.logging.info("***** 압축파일정보 생성 완료");
					
					// TS_DATA_FILE Update (압축파일명, 압축파일바이트, 압축파일사이즈)
					int result;
					if((result = this.dao.uptTsDataFile(cList.get(cnt))) != 0)
					{
						this.logging.info("***** 개별수치지형도 1건에 대한 공간정보 압축 파일 등록 성공 !!!");
						NgiiLogDomain logVo = new NgiiLogDomain();
						logVo.setDataSn(cList.get(cnt).getDataSn());
						logVo.setNgiiUptnDataNm(cList.get(cnt).getDataNm());
						
						// 데이터 코드 부여
						if(cList.get(cnt).getSpatialFileFormat().equals("NGI") || cList.get(cnt).getSpatialFileFormat().equals("ngi"))
							logVo.setNgiiUptnSecd("02");
						else
							logVo.setNgiiUptnSecd("01");
						
						// 갱신 로그 등록
						this.dao.insNgiiLog(logVo);
						this.logging.info("***** 개별수치지형도 1건에 대한 갱신 로그 등록 성공 !!!");
					}
					else
						this.error(new sdiException(enException.ERROR, "***** 개별수치지형도 공간정보 압축 파일 업데이트중 오류가 발생하였습니다. !!!"));
					
				}
				else
					this.error(new sdiException(enException.ERROR, "***** TS_DATA Update Error !!!"));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
			this.logging.info("***** [ " + sdf.format(new Date()) + " ] 현재 " + cList.size() + " 건의 갱신 데이터를 처리 성공하였습니다.");
		} catch (Exception e) {
			throw this.error(
					new sdiException(enException.ERROR, "***** 개별수치지형도 갱신중 오류가 발생하였습니다.", e)
					);
		}
		
		/*
		 * [3] 갱신 처리된 데이터 이동 적재
		 */
		try {
			this.logging.info("***** 개별수치지형도 갱신 처리 이후 원본 데이터 이전 적재를 수행합니다");
			proc.MoveFolder();
			this.logging.info("***** 개별수치지형도 갱신 처리 이후 원본 데이터 이전 적재를 수행완료");
		} catch (Exception e) {
			e.printStackTrace();
			throw this.error(
					new sdiException(enException.ERROR, "***** 개별수치지형도 갱신 후 원본데이터 이동적재 중 오류가 발생하였습니다.", e)
					);
		}
		
		/*
		 * [4] 이동적재 이후 원본 데이터 경로 내 파일 삭제 (재 갱신 방지)
		 */
		try {
			this.logging.info("***** 개별수치지형도 재 갱신 방지를 위한 원본데이터 삭제를 진행합니다.");
			proc.folderDelete(new File(pro.getProperty("ngii.one.file.dir")));
			this.logging.info("***** 개별수치지형도 재 갱신 방지를 위한 원본데이터 삭제를 진행완료.");
		} catch (Exception e) {
			throw this.error(
					new sdiException(enException.ERROR, "***** 개별수치지형도 갱신 후 원본데이터 삭제 중 오류가 발생하였습니다.", e)
					);
		}
		
		/*
		 * [5] 적재폴더(YYmmdd) 내 zip 파일을 제외한 원본(dxf,ngi,nda) 파일 삭제
		 */
		try {
			this.logging.info("***** 개별수치지형도 적재이동폴더내에서 ZIP 파일 외 공간정보파일(DXF, NGI, NDA)을 삭제합니다.");
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
			this.proc.folderDeletePath(this.pro.getProperty("ngii.one.file.movedir") + File.separator + sdf.format(new Date()));
		} catch (Exception e) {
			throw this.error(
					new sdiException(enException.ERROR, "***** 개별수치지형도 갱신 후 적재폴더(YYmmdd)내 원본데이터 삭제 중 오류가 발생하였습니다.", e)
					);
		}
	}
	
	/**
	 * @Method Name  : init
	 * @작성일   : 2015. 9. 30. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 초기화 수행 (세션연결)
	 * @throws sdiException
	 */
	public void init() throws sdiException
	{
		this.logging.info("===== <PROCESS> 초기화 =====");
		
		this.logging.info("===== <PROCESS> DB Session을 연결합니다");
		try {
			// DB 세션 연결
			this.db.openSession("com/mapper/Configuration.xml");
			
			// 쿼리 핸들러 설정
			this.dao = new daoQuery(this.db.getSession());
		} catch (Exception e) {
			// 예외가 발생할 경우 error 메서드를 통해 처리 후 돌려줌
			throw this.error(
				new sdiException(enException.ABORT, "DB Session 연결 중에 오류가 발생하였습니다.", e)
			);
		}
	}
	
	/**
	 * @Method Name  : start
	 * @작성일   : 2015. 9. 30. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 시작처리 (트랜잭션)
	 * @throws sdiException
	 */
	public void start()
			throws sdiException
		{
			this.logging.info("=== <JOB> 처리 시작 ===");
			
			// 트랜잭션 시작
			this.logging.info("Transaction을 시작합니다.");
			try
			{
				this.db.openTransaction();
			}
			catch (Exception e)
			{
				throw error(new sdiException(
					enException.ABORT, "Transaction 시작 중에 오류가 발생하였습니다.", e)); 
			}		
		}
	
	/**
	 * @Method Name  : error
	 * @작성일   : 2015. 9. 30. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 에러 처리 (롤백포함)
	 * @param err
	 * @return
	 */
	public sdiException error(sdiException err)
	{
		this.logging.info("## <오류 발생> ##");
		
		// 해당 예외에 대한 로그를 기록
		this.logging.error(err.toString());
		// 해당 예외에 대한 스택을 기록
		//this.logging.debug(e);
		
		// 트랜잭션 Rollback
		this.logging.info("Transaction을 Rollback합니다.");
		try
		{
			this.db.closeTransaction(enTransaction.ROLLBACK);
		}
		catch (Exception e)
		{
			// 이미 예외 처리 중이므로 로그만 기록
			this.logging.error(
				"Transaction Rollback 중에 오류가 발생하였습니다. => " + e.toString()); 
		}
		
		return err;
	}
	
	/**
	 * @Method Name  : clear
	 * @작성일   : 2015. 9. 30. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : DB Session 종료
	 */
	public void clear()
	{
		this.logging.info("## <PROCESS> 종료 ##");
		
		// DB 세션 종료
		this.logging.info("***** DB Session을 해제합니다.");
		try
		{
			// DB 연결 세션을 종료
			this.db.closeSession();
		}
		catch (Exception e) 
		{
			// 전체 종료 처리이기 때문에 로그만 기록
			this.logging.error(
				"***** DB Session 해제 중에 오류가 발생하였습니다. => " + e.toString()); 
		}
		
		// 로그 종료 (파일 기록)
		try
		{
			this.logging.finishLog(true);
		}
		catch (Exception e) 
		{
			// 로그 종료 처리이기 때문에 콘솔에만 출력
			System.out.println(
				"***** Logging 종료 (파일 기록) 중에 오류가 발생하였습니다. => " + e.toString()); 
		}
	}
	

}
