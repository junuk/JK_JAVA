/**
 * 
 */
package proc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vo.NgiiGDBHistDomain;
import vo.NgiiLogDomain;
import common.sdiDB;
import dao.daoQuery;

/**
 * @FileName  : ngiiProc.java
 * @Project     : NgiiFolderMoving
 * @Date         : 2015. 10. 25. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 :
 */
public class ngiiProc implements Job {
	
	// 로그 정보
	private Logger logger = null;
	// 프로퍼티 정보
	private Properties pro = null;
	
	// FTP Client
	private FTPClient conn = null;
	
	// DB Connector
	private sdiDB db = null;
	
	private daoQuery dao = null;
	
	/*
	 * [01] 시작 처리
	 */
	public void start()
	{
		// 로깅 활성화
		this.logger = Logger.getLogger(getClass());
		
		try {
			// start Log 처리
			logger.info("***** [연속수치지형도 GDB 폴더이동 / 갱신] 프로세스 동작 합니다. *****");
			logger.info("*******************************");
			//this.init();
		} catch (Exception e) {
			logger.error("***** 에러 발생 *****\n");
			e.printStackTrace();
			logger.error("*******************************");
			this.finish();
		}
	}
	
	/*
	 * [02] 초기화 처리
	 */
	public void init()
	{
		FileInputStream fis = null;
		try {
			this.db = new sdiDB();
			this.pro = new Properties();
			
			logger.info("***** 초기화 작업 [DB Open Session] 수행합니다 *****");
			logger.info("*******************************");
			this.db.openSession("com/mapper/Configuration.xml");
			
			logger.info("***** 초기화 작업 [DB Connect Session] 수행합니다 *****");
			logger.info("*******************************");
			this.dao = new daoQuery(this.db.getSession());
			
			logger.info("***** 초기화 작업 [Property Reading] 수행합니다 *****");
			logger.info("*******************************");
			fis = new FileInputStream("property/ngii.properties");
			this.pro.load(new InputStreamReader(fis));
		} catch (Exception e) {
			logger.error("***** 초기화 작업중 에러가 발생하였습니다. *****\n");
			e.printStackTrace();
			this.finish();
		}
	}
	
	/*
	 * [03] 종료 처리
	 */
	public void finish()
	{
		try {
			// End Log 처리
			logger.info("***** 종료 작업 수행합니다 *****");
			logger.info("*******************************");
		} catch (Exception e) {
			logger.error("***** 에러 발생 *****\n");
			e.printStackTrace();
			logger.error("*******************************");
			this.finish();
		}
	}
	
	/*
	 * [04] 프로세스 수행
	 */
	public void process()
	{
		try {
			logger.info("***** 프로세스 수행합니다 *****");
			logger.info("*******************************");
			// 날자 폴더 검색 YYMMDD_T
			// 정규식을 이용하여 YYMMDD_T 형태의 폴더만 찾도록 제어한다
			
			// 대상폴더를 검색하여 YYMMDD_T 형식의 폴더를 검색하고 리스트에 담는다 (한개가 아닐수도있으므로)
			File sDir = new File(pro.getProperty("ngii.folder.startDir"));
			File[] fileList = sDir.listFiles();
			System.out.println("sDir -> " + sDir.getAbsolutePath());
			for(File targetFile : fileList)
			{
				if(targetFile.isDirectory())
				{
					NgiiGDBHistDomain GDBLog = null;
					NgiiLogDomain Log = null;
					File endFolder = null;
					if(regexMatched(targetFile.getName()))
					{
						int filePos = targetFile.getName().lastIndexOf("_");
						
						// 이동적재 수행
						endFolder = new File(pro.getProperty("ngii.folder.endDir") + File.separator + targetFile.getName().substring(0, filePos));
						folderCopy(targetFile, endFolder);
						logger.info("적재완료 폴더 명 -> " + targetFile.getName().substring(0, filePos));
						
						// 이동적재 후 적재전 폴더명 변경 (적재 확인 처리)
						int pos = targetFile.getAbsolutePath().lastIndexOf("_");
						File reNameFile = new File(targetFile.getAbsolutePath().substring(0, pos) + "_F");
						logger.info("ReName 수행 합니다 -> " + targetFile.getName() + " 을 -> " + reNameFile.getName() + " 로 변경합니다.");
						targetFile.renameTo(reNameFile);
						logger.info("ReName 수행 완료");
						
						// 적재 완료 후 로그 처리
						GDBLog = new NgiiGDBHistDomain();
						Log = new NgiiLogDomain();
						
						GDBLog.setExstAt("1");		// 1(존재) 2(삭제) (삭제 로직 태울때 2로 체크한다)
						GDBLog.setNrmDir(pro.getProperty("ngii.folder.endDir"));
						GDBLog.setNrmFolderName(endFolder.getName());
						
						Log.setNgiiUptnDataNm("연속수치지형도 1:5000");
						Log.setNgiiUptnSecd("03");
						int retCount = 0;
						if((retCount = dao.insNgiiLog(Log)) != 1);
						{
							logger.error("***** 지리원 갱신 이력 입력중 오류가 발생하였습니다.");
							retCount = 0;
						}
						if((retCount = dao.addThNrmCstdyHist(GDBLog)) != 1)
						{
							logger.error("***** 연속수치지형도 보관 이력 입력중 오류가 발생하였습니다.");
							retCount = 0;
						}
					}
				}
				else
					continue;
			}
			
		} catch (Exception e) {
			logger.error("***** 에러 발생 *****\n");
			e.printStackTrace();
			logger.error("*******************************");
			this.finish();
		}
	}
	
	/*
	 * [05] 프로세스 컨트롤러
	 */
	public void procMain()
	{
		try {
			// 시작 처리
			this.start();
			
			// 초기화 처리
			this.init();
			
			// 업무처리
			this.process();
			
			// 종료 처리
			this.finish();
		} catch (Exception e) {
			logger.error("*******************************");
			logger.error("***** 에러 발생 *****\n");
			e.printStackTrace();
			logger.error("*******************************");
			this.finish();
		}
	}
	
	/**
	 * @Method Name  : ftpConnection
	 * @작성일   : 2015. 11. 8. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 :	FTP 연결
	 * @return
	 */
	public boolean ftpConnection()
	{
		boolean isConnection = false;
		
		String SERVER = pro.getProperty("ngii.folder.ftp.ip");
		String PORT = pro.getProperty("ngii.folder.ftp.port");
		String USER = pro.getProperty("ngii.folder.ftp.user");
		String PASSWORD = pro.getProperty("ngii.folder.ftp.password");
		
		
		try {
			conn = new FTPClient();
			conn.setControlEncoding("UTF-8");
			conn.connect(SERVER, Integer.parseInt(PORT));
			conn.login(USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isConnection;
	}
	
	/**
	 * @Method Name  : ftpDisconnection
	 * @작성일   : 2015. 11. 8. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 :	FTP 연결해제
	 * @return
	 */
	public boolean ftpDisconnection()
	{
		boolean isDisconnection = false;
		try {
			conn.logout();
			if(conn.isConnected())
				conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDisconnection;
	}
	
	/*
	 * FTP Get
	 */
	public boolean getFTP()
	{
		boolean retCode = false;
		FileOutputStream fos = null;
		
		try {
			File file = new File("");
			fos = new FileOutputStream(file);
			boolean isSuccess = conn.retrieveFile("내리는 파일 명", fos);
			
			if(isSuccess)
			{
				// 다운로드 성공
			}
			else
			{
				// 다운로드 실패
				System.out.println("***** 다운로드 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			this.ftpDisconnection();
		}
		return retCode;
	}
	
	/*
	 * FTP Put
	 */
	public boolean putFTP()
	{
		boolean retCode = false;
		FTPClient ftp = null;
		FileInputStream fis = null;
		
		try {
			ftp = new FTPClient();
			ftp.setControlEncoding("UTF-8");
			
			ftp.connect("host", 1111);
			ftp.login("id", "pw");
			ftp.changeWorkingDirectory(pro.getProperty("ngii.folder.startDir"));
			
			File targetFolder = new File(pro.getProperty("ngii.folder.startDir"));
			File[] uploadFiles = targetFolder.listFiles();
			
			for(File target : uploadFiles)
			{
				fis = new FileInputStream(target);
				
				if(target.isDirectory())
				{
					target.mkdirs();
				}
				else
				{
					
				}
				
				boolean isSuccess = ftp.storeFile(target.getName(), fis);
				
				if(isSuccess)
				{
					// 업로드 성공
				}
				else
				{
					// 업로드 실패
					// 다운로드 실패
					System.out.println("***** 업로드 실패");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(ftp != null && ftp.isConnected())
			{
				try {ftp.disconnect();} catch (Exception e2) {e2.printStackTrace();}
			}
		}
		
		
		return retCode;
	}
	
	/*
	 * [06] 폴더 복사 메서드 (로컬to로컬)
	 * startDir	:	복사 시작 경로 
	 * endDir	: 	복사 위치 경로
	 */
	public void folderCopy(File startDir, File endDir)
	{
		File[] ff = startDir.listFiles();
		for (File file : ff) {
			if(!endDir.exists())
				endDir.mkdirs();
			File temp = new File(endDir.getAbsolutePath() + File.separator + file.getName());
			if(file.isDirectory()){
				temp.mkdir();
				folderCopy(file, temp);
			} else {
				FileInputStream fis = null;
				FileOutputStream fos = null;
				try {
					fis = new FileInputStream(file);
					fos = new FileOutputStream(temp) ;
					byte[] b = new byte[4096];
					int cnt = 0;
					while((cnt=fis.read(b)) != -1){
						fos.write(b, 0, cnt);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					try {
						fis.close();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
			file = null;
		}
		ff = null;
	}
	
	/**
	 * 정규식 매칭 검사
	 */
	public boolean regexMatched(String inputText)
	{
		return Pattern.matches(this.pro.getProperty("ngii.folder.search.regex"), inputText);
	}
	
	/*
	 * 스케줄러 Execute
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//수행 시간 측정
		long startTime = System.currentTimeMillis();
		this.procMain();
		long finishTime = System.currentTimeMillis();
		logger.info("***** 프로그램 수행 시간 -> " + (finishTime - startTime)/1000.0 + " 초");
	}
	
}
