/**
 * 
 */
package com.common;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vo.NgiiDataDomain;

/**
 * @FileName  : sdiMethod.java
 * @Project     : ngii
 * @Date         : 2015. 9. 17. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 : 공통 기능 메서드
 */
public class sdiMethod {
	
	private sdiLog logging = new sdiLog();
	private static final int ZIP_LEVEL = 8;
	private static final int BUFFER_SIZE = 1024 * 2;
	
	private Properties prop = null;
	
	public sdiMethod() throws IOException {
		this.prop = new Properties();
		prop = this.readProperties("property/ngii.properties");
	}
	
	
	/**
	 * @Method Name  : MoveFolder
	 * @작성일   : 2015. 11. 3. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 :
	 */
	public void MoveFolder() {
		File targetDir = new File(this.prop.getProperty("ngii.one.file.dir"));
		File[] targetFileList = targetDir.listFiles();

		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		File dayDir = new File(this.prop.getProperty("ngii.one.file.movedir") + File.separator + sdf.format(new Date()));

		for (File targetFile : targetFileList) {
			if (!dayDir.exists())
				dayDir.mkdirs();

			if (targetFile.getName().equals(dayDir.getName()))
				continue;

			else if (targetFile.isDirectory()) {
				folderCopy(targetFile,new File(this.prop.getProperty("ngii.one.file.movedir") + File.separator + dayDir.getName() + File.separator + targetFile.getName()));
			}
			
			else if(targetFile.isFile())
			{
				folderCopy(targetFile, new File(this.prop.getProperty("ngii.one.file.movedir") + File.separator + dayDir.getName() + File.separator + targetFile.getName()));
			}

			else
				continue;
		}
	}
	
	/**
	 * @Method Name  : folderCopy
	 * @작성일   : 2015. 11. 3. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 폴더내 파일(디렉토리) 이동 복사
	 * @param startDir	: 복사타겟위치
	 * @param endDir	: 복사위치
	 */
	public void folderCopy(File startDir, File endDir) 
	{
		if(startDir.isDirectory())
		{
			File[] ff = startDir.listFiles();
			for (File file : ff) {
				if (!endDir.exists())
					endDir.mkdirs();
				File temp = new File(endDir.getAbsolutePath() + File.separator + file.getName());
				if (file.isDirectory()) {
					temp.mkdir();
					folderCopy(file, temp);
				} else {
					FileInputStream fis = null;
					FileOutputStream fos = null;
					try {
						int pos = file.getName().lastIndexOf(".");
						fis = new FileInputStream(file);
						fos = new FileOutputStream(temp);
						//if(file.getName().substring(pos+1).equals("ngi") || file.getName().substring(pos+1).equals("nda") || file.getName().substring(pos+1).equals("dxf"))
						//{
							byte[] b = new byte[4096];
							int cnt = 0;
							while ((cnt = fis.read(b)) != -1) {
								fos.write(b, 0, cnt);
							}
							//fos.close();
						//}
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
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
		else if(startDir.isFile())
		{
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				int pos = endDir.getName().lastIndexOf("\\");
				File temp = new File(endDir.getAbsolutePath());
				fis = new FileInputStream(startDir);
				fos = new FileOutputStream(temp);
					byte[] b = new byte[4096];
					int cnt = 0;
					while ((cnt = fis.read(b)) != -1) {
						fos.write(b, 0, cnt);
					}
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		
	}
	
	public void folderDelete(File path) {
		try {
			if (!path.exists()) {
			}
			File[] files = path.listFiles();
			for (File file : files) 
			{
				//int pos = file.getName().lastIndexOf(".");
				if (file.isFile()) 
				{
					// 원본 데이터는 남겨두고 압축만 삭제
					/*if(!file.getName().substring(pos + 1).equals("ngi") 
							&& !file.getName().substring(pos + 1).equals("dxf") 
							&& !file.getName().substring(pos + 1).equals("nda") 
							&& !file.getName().substring(pos + 1).equals("log"))*/
					/*if(file.getName().substring(pos + 1).equals("ngi") 
							|| file.getName().substring(pos + 1).equals("dxf") 
							|| file.getName().substring(pos + 1).equals("nda") 
							|| file.getName().substring(pos + 1).equals("log")
							|| file.getName().substring(pos + 1).equals("zip"))
						file.delete();
					else*/
						file.delete();
				}
				else if(file.isDirectory())
					folderDelete(file);
				else
					file.delete();		// Path 하위 디렉토리까지 전체 제거
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Method Name  : readProperties
	 * @작성일   : 2015. 9. 21. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 :	프로퍼티 파일 읽기
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public Properties readProperties(String fileName) throws IOException
	{
		Properties pro = new Properties();
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(fileName);
			pro.load(new InputStreamReader(fis));
		} catch (Exception e) {
			logging.error("===== (readProperties) 프로퍼티를 읽을 수 없습니다.");
		}
		finally
		{
			if(fis != null)
				fis.close();
		}
		
		return pro;
	}
	
	/**
	 * @Method Name  : subDirList
	 * @작성일   : 2015. 10. 5. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 선택 디렉토리 내 NGI / DXF 파일 검색
	 * @param arrList
	 * @param dirName
	 * @throws Exception
	 */
	public void subDirList(ArrayList<NgiiDataDomain> arrList, String dirName) throws Exception
	{
		File dir = new File(dirName);
		File[] fList = dir.listFiles();
		
		try {
			
			for(int cnt=0; cnt<fList.length; cnt++)
			{
				File target = fList[cnt];
				if(target.isFile())
				{
					int pos = target.getName().lastIndexOf(".");
					int slashPos = target.getAbsolutePath().lastIndexOf("\\");
					String targetFormat = target.getName().substring(pos + 1); 
					NgiiDataDomain vo = new NgiiDataDomain();
					
					if(targetFormat.equals("NGI") || targetFormat.equals("ngi"))
					{
						int targetPos = target.getName().indexOf("_");							// 도엽번호 POS 검출
						//vo.setDataSn(target.getName().substring(0, targetPos));			
						vo.setDataMapdmcNo(target.getName().substring(0, targetPos));			// 대상 파일 도엽번호 추출
						vo.setSpatialFile(target);												// 대상 파일
						vo.setSpatialFileName(target.getName().substring(0, pos));				// 대상 파일명 (확장자 미포함)
						vo.setSpatialFileFormat(targetFormat);									// 대상 파일포맷
						vo.setSpatialFileDir(target.getAbsolutePath().substring(0, slashPos));	// 대상 파일 경로 (파일명포함)
						vo.setSpatialFileSize(target.length());									// 대상 파일 사이즈
						// NGI의 경우 대상 파일의 NDA 파일도 포함 시킨다.
						vo.setSubSpatialFile(new File(vo.getSpatialFileDir() + File.separator + vo.getSpatialFileName() + ".nda"));
						
						arrList.add(vo);
					}
					else if(targetFormat.equals("DXF") || targetFormat.equals("dxf"))
					{
						int targetPos = target.getName().indexOf("_");					// 도엽번호 POS 검출
						//vo.setDataSn(target.getName().substring(0, targetPos));			
						vo.setDataMapdmcNo(target.getName().substring(0, targetPos));	// 대상 파일 도엽번호 추출
						vo.setSpatialFile(target);										// 대상 파일
						vo.setSpatialFileName(target.getName().substring(0, pos));		// 대상 파일명 (확장자 미포함)
						vo.setSpatialFileFormat(targetFormat);							// 대상 파일포맷
						vo.setSpatialFileDir(target.getAbsolutePath().substring(0, slashPos));	// 대상 파일 경로 (파일명포함)
						vo.setSpatialFileSize(target.length());							// 대상 파일 사이즈
						
						arrList.add(vo);
					}
					else
						continue;
				}
				else if(target.isDirectory())
				{
					subDirList(arrList, target.getCanonicalPath().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/*
	 * 개별수치지형도 메타데이터 엑셀 파일 처리
	 */
	public Vector<String> getParsingExcel(String filePath, String fileName) throws Exception
	{
		XSSFWorkbook wb = null;				// Excel WorkBook
		File excelTarget = null;				// Target Excel File
		Vector<String> content = null;
		
		// 테스트 위치 C:/DEV_JK_TEST   NGII_151012_C.xlsx
		
		try {
			// 엑셀파일 읽기
			excelTarget = new File(filePath + File.separator + fileName);
			wb = new XSSFWorkbook(new FileInputStream(excelTarget));
			
			// 라인저장 임시 벡터
			Vector<String> tmpContent = new Vector<String>();
			boolean isNull = false;
			
			for(Row row : wb.getSheetAt(0))	// 행 구분
			{
				String str = new String();
				for(Cell cell : row)		// 열 구분
				{
					isNull = false;
					
					// 셀의 타입에 따라서 받아서 구분짓는데 한 행을 하나의 스트링으로 저장
					switch (cell.getCellType()) {
					// String Type
					case Cell.CELL_TYPE_STRING :
						str = str + cell.getRichStringCellValue().getString();
						break;
						
					// Numetic Type
					case Cell.CELL_TYPE_NUMERIC : 
						if(DateUtil.isCellDateFormatted(cell))
							str = str + cell.getDateCellValue().toString();
						else
							str = str + Integer.toString((int)cell.getNumericCellValue());
						break;
						
					// Boolean Type
					case Cell.CELL_TYPE_BOOLEAN : 
						str = str + cell.getBooleanCellValue();
						break;
					
						// Formula Type
					case Cell.CELL_TYPE_FORMULA : 
						str = str + cell.getCellFormula();
						break;
						
					default:		// NULL Type
						isNull = true;
					}
					if(isNull != true)
						str = str + ",";	// 컬럼별 데이터간 , 로 구분ㅐ
				}
				
				// 읽어낸 한 행 값을 벡터에 추가
				tmpContent.add(str);
			}
			
			content = new Vector<String>();
			
			// 한 행씩 추가하면서 마지막 배열에 space가 추가되는것을 제거한다
			// Space가 있을 때에만 사용한다 (문자에 Space)
			for(int i=0; i<tmpContent.size(); i++)
			{
				String str = tmpContent.get(i);
				str = str.substring(0, str.length()-1);
				content.add(str);
			}
			
			// 출력 테스트
			/*for(int j=0; j<content.size(); j++)
			{
				System.out.println("content => " + content.get(j).toString());
			}*/
			
		} catch (Exception e) {
			throw e;
		}
		return content;
	}
	
	
	/*
	 * 수치지도 / 주제도 등록
	 */
	public int addSpatialRegistData() throws Exception
	{
		// TODO : 수치지도 등록 유통 supplymng 패키지 참조하여 등록 (썸네일 생성 필요) 
		try {
			
		} catch (Exception e) {
			throw e;
		}
		
		return 0;
	}
	
	/*
	 * 수치지도 / 주제도 갱신
	 */
	
	
	/**
	 * @Method Name  : createThumnailImg
	 * @작성일   : 2015. 10. 5. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 썸네일을 생성한다
	 * @param target : 썸네일 생성 파일
	 * @throws Exception 
	 */
	public byte[] createThumnailImg(File target) throws Exception 
	{
		byte[] imageByte = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			String thmSrc = "C:/DEV_JK_TEST";
			
			// 확장자 제거 후 파일명 추출
			int pos = target.getName().indexOf(".");
			
			File thumnailFileNm = new File(thmSrc + File.separator + target.getName().substring(0, pos)+"_Thumnail.jpg");
			int width = 165;
			int height = 230;
			
			// 썸네일 이미지 생성
			BufferedImage orgImg = ImageIO.read(target);
			BufferedImage thmImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			
			// 썸네일 그리기
			Graphics2D g2d = thmImg.createGraphics();
			g2d.drawImage(orgImg, 0, 0, width, height, null);
			
			// 파일 생성
			ImageIO.write(thmImg, "png", thumnailFileNm);
			
			// byte 생성
			ImageIO.write(thmImg, "png", baos);
			baos.flush();
			imageByte = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			throw e;
		}
		return imageByte;
	}
	
	public byte[] makeNgiThumb(String sourcePath, String fileNm, String fileExt)
	{  
		
		
		byte[] byteOut = null;
		
				
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			BufferedImage bufferedImage = getImageFromNgi(sourcePath, fileNm + "." +  fileExt, "all", 550, 500, 0, 0);;
			ImageIO.write(bufferedImage, "gif", bos);
			//파일 생성 (임시)
			File file = new File(sourcePath + fileNm + ".gif");
			
			ImageIO.write(bufferedImage, "gif", file);
			
			byteOut = bos.toByteArray();
			
			bos.close();
			
			//생성 이미지 파일 삭제
			file.delete();

		}
		catch(Exception e)
		{	
			byteOut = null;				
		}
		
		return byteOut;
		
	}
	
	
	
	// TODO
	//NGI drawing(image)
	public static BufferedImage getImageFromNgi(String folderpath,String reqfilename, String layername, int sizeX, int sizeY,int moveX,int moveY){
		
		Double[] minMax=new Double[5]; //5번째값은 plinegen
		
		//헤더 bound가 부정확한 파일이 많아 전체 좌표 검색
		minMax = getExtentNgiByValue(folderpath,reqfilename);
		
		Double xdif = minMax[2] - minMax[0];
		Double ydif = minMax[3] - minMax[1];
		Double xmin = minMax[0];
		Double ymin = minMax[1];
		// sizeY 는 size X 에딸 minMax를 통해 구해야함(미구)
		// bufferedimage 생성용 임시 크기
		sizeY=Integer.parseInt(String.valueOf(Math.round(sizeX * (ydif/xdif))));
		
		int leftMargin = (800 - sizeX) / 2;
		int topMargin = (700 - sizeY) / 2;
		
		BufferedImage image = new BufferedImage(800, 700, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 800, 700);
		//createCompositeWatermark(image,g);
		
		String seplyrname="";
		
		String lineStr="";
		try {
			BufferedReader in = new BufferedReader(new FileReader(folderpath+File.separator + reqfilename));
			HashMap<String, String> headerResultMap=new HashMap<String, String>();
			
			do {
				lineStr=in.readLine();	// 한줄씩 읽기
				
				if(lineStr==null) break;
				if(lineStr.equals("EOF")||lineStr==null) break;
				
				//layer 시작
				if(lineStr.equals("<LAYER_START>")) {
					do {						
						lineStr=in.readLine();	// 한줄씩 읽기
						if(lineStr.equals("<LAYER_END>")) break;
						
						if(lineStr.equals("$LAYER_NAME")) {
							lineStr=in.readLine();
							lineStr=lineStr.replace("\"", "");
							seplyrname=lineStr;
							if(!(layername.equals("all"))) {
								if(layername.indexOf(lineStr)<0) break;
							}
						}
					
						if(lineStr.equals("<HEADER>")) headerResultMap = procHeader(in,layername);
						
						if(lineStr.equals("<DATA>")) //data부분 처리
						{
							 //data부분 routine시작
						
							do{
								lineStr = in.readLine();
								if(lineStr.equals("<END>")) break; // data 섹션 종료
									
								if(lineStr.equals("POLYGON")) //  POLYGON 처리 시작
								{ 
									String numpart = (in.readLine()).replace("NUMPARTS", "").trim();
									
									int numpartInt=Integer.parseInt(numpart);
									
									ArrayList<int[]> xpt_parts=new ArrayList<int[]>();
									ArrayList<int[]> ypt_parts=new ArrayList<int[]>();
									
									for(int parts=0;parts<numpartInt;parts++) 
									{
										int ptCnt=Integer.parseInt((in.readLine()).trim());
										//ngi 분석결과 polygon 일시 끝점이 시작점이 아니라서 첫번째 포인트를 저장해서 나중에 연결해야하는거 같음.
										
										double[] pointOri = new double[2];
										
										int[] drawPointInt=new int[2];
										int[] prevDrawPointInt=new int[2];
										int[] firstDrawPointInt=new int[2];
										
										//폴리곤으로 그리기 위한 변수
										StringBuffer xpt=new StringBuffer();
										StringBuffer ypt=new StringBuffer();
										
										for(int i=0;i<ptCnt;i++)
										{
											// 폴리곤시작
											lineStr=in.readLine();
											String[] ptStr=(lineStr.trim()).split(" ");
											
											pointOri[0]=Double.parseDouble(ptStr[0].trim());
											pointOri[1]=Double.parseDouble(ptStr[1].trim());
											
											drawPointInt[0]=Integer.parseInt(String.valueOf(Math.round(((pointOri[0]-xmin)*(sizeX/xdif)))));
											drawPointInt[1]=(sizeY-Integer.parseInt(String.valueOf(Math.round(((pointOri[1]-ymin)*(sizeY/ydif))))));
																					
											if(i==0) { // 최초 포인트 저장
												firstDrawPointInt[0]=drawPointInt[0];
												firstDrawPointInt[1]=drawPointInt[1];
												
												xpt.append(drawPointInt[0]+",");
												ypt.append(drawPointInt[1]+",");
											}
											
											if(i>0){
												if(!(drawPointInt[0]==prevDrawPointInt[0]&&drawPointInt[1]==prevDrawPointInt[1])){
													//라인으로그리기
													xpt.append(drawPointInt[0]+",");
													ypt.append(drawPointInt[1]+",");
												}
											}
											prevDrawPointInt[0]=drawPointInt[0];
											prevDrawPointInt[1]=drawPointInt[1];
										}
										
										//라인으로그리기
										xpt.append(firstDrawPointInt[0]+"");
										ypt.append(firstDrawPointInt[1]+"");
										String[] xptarray=xpt.toString().split(",");
										String[] yptarray=ypt.toString().split(",");
										int[] xptInt=new int[xptarray.length];
										int[] yptInt=new int[xptarray.length];
										for(int z=0;z<xptarray.length;z++){
											xptInt[z]=Integer.parseInt(xptarray[z])+leftMargin+moveX;
											yptInt[z]=Integer.parseInt(yptarray[z])+topMargin+moveY;
										}
										xpt_parts.add(xptInt);
										ypt_parts.add(yptInt);
									}

									String regionAttrSeq=removeStr(in.readLine(),"REGIONGATTR,(,)");
								//	System.out.println("null finding---"+headerResultMap.get("REGION_REP_"+regionAttrSeq));
									String[] regionAttr=headerResultMap.get("REGION_REP_"+regionAttrSeq).split(",");
									
									//sample: 1 REGIONATTR(SOLID, 1, 0, SOLID100, 9830399, 9830399)
									g.setColor(getColorFromNgiCde(seplyrname,regionAttr[4]));
								    
									AlphaComposite alpha;
									alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
									g.setComposite(alpha);
																		
									Area outer=new Area();
									Area inner=new Area();
									// multi inner 링은 가능하나 multi outer링은 처리불가능함... 분석필요....
									for(int parts=0;parts<xpt_parts.size();parts++){
										if(seplyrname.startsWith("G001")){ //행정경계는 그냥 그린다. 라인으로(덮히는 상황때문)
											g.setColor(Color.black);
											g.drawPolyline(xpt_parts.get(parts), ypt_parts.get(parts),xpt_parts.get(parts).length);
										}
										if(parts==0) outer=new Area(new Polygon(xpt_parts.get(parts), ypt_parts.get(parts),xpt_parts.get(parts).length));
										if(parts>0){
											inner=new Area(new Polygon(xpt_parts.get(parts), ypt_parts.get(parts),xpt_parts.get(parts).length));
											outer.subtract(inner);
										}
									}
									if(seplyrname.startsWith("G001")){
									}else{
										g.fill(outer);
									}	
								}
								else if(lineStr.equals("LINESTRING")){ //LINESTRING 처리 시작
									int ptCnt=Integer.parseInt((in.readLine()).trim());
									//ngi 분석결과 polygon 일시 끝점이 시작점이 아니라서 첫번째 포인트를 저장해서 나중에 연결해야하는거 같음.									
									
									double[] pointOri = new double[2];
									
									int[] drawPointInt=new int[2];
									int[] prevDrawPointInt=new int[2];
									int[] firstDrawPointInt=new int[2];
									
									StringBuffer xpt=new StringBuffer();
									StringBuffer ypt=new StringBuffer();
									
									for(int i=0;i<ptCnt;i++)
									{
										// 라인스트링시작
										lineStr=in.readLine();
										String[] ptStr=(lineStr.trim()).split(" ");
										
										pointOri[0]=Double.parseDouble(ptStr[0].trim());
										pointOri[1]=Double.parseDouble(ptStr[1].trim());
										drawPointInt[0]=Integer.parseInt(String.valueOf(Math.round(((pointOri[0]-xmin)*(sizeX/xdif)))));
										drawPointInt[1]=(sizeY-Integer.parseInt(String.valueOf(Math.round(((pointOri[1]-ymin)*(sizeY/ydif))))));																				
										
										if(i==0) { // 최초 포인트 저장
											firstDrawPointInt[0]=drawPointInt[0];
											firstDrawPointInt[1]=drawPointInt[1];
											xpt.append(drawPointInt[0]+",");
											ypt.append(drawPointInt[1]+",");
										}
										
										if(i>0)
										{
											if(!(drawPointInt[0]==prevDrawPointInt[0]&&drawPointInt[1]==prevDrawPointInt[1]))
											{
												xpt.append(drawPointInt[0]+",");
												ypt.append(drawPointInt[1]+",");
											}
										}
										prevDrawPointInt[0]=drawPointInt[0];
										prevDrawPointInt[1]=drawPointInt[1];
									}
									
									String[] xptarray=(xpt.substring(0,xpt.length()-1)).split(",");
									String[] yptarray=(ypt.substring(0,ypt.length()-1)).split(",");
									
									int[] xptInt=new int[xptarray.length];
									int[] yptInt=new int[xptarray.length];
									
									for(int z=0;z<xptarray.length;z++)
									{
										xptInt[z]=Integer.parseInt(xptarray[z])+leftMargin+moveX;
										yptInt[z]=Integer.parseInt(yptarray[z])+topMargin+moveY;
									}
									
									String lineAttrSeq=removeStr(in.readLine(),"LINEGATTR,(,)");
									String[] lineAttr=headerResultMap.get("LINE_REP_"+lineAttrSeq).split(",");

									g.setColor(getColorFromNgiCde(seplyrname,lineAttr[2]));
									
									AlphaComposite alpha;
									alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
									g.setComposite(alpha);
									g.drawPolyline(xptInt,yptInt,xptInt.length);
								}
								else if(lineStr.equals("POINT")){ //POINT 처리 시작
									
									double[] pointOri = new double[2];
									
									int[] drawPointInt=new int[2];
									
									lineStr=in.readLine();
									String[] ptStr=(lineStr.trim()).split(" ");
										
									pointOri[0]=Double.parseDouble(ptStr[0].trim());
									pointOri[1]=Double.parseDouble(ptStr[1].trim());
									
									drawPointInt[0]=Integer.parseInt(String.valueOf(Math.round(((pointOri[0]-xmin)*(sizeX/xdif)))))+leftMargin+moveX;
									drawPointInt[1]=(sizeY-Integer.parseInt(String.valueOf(Math.round(((pointOri[1]-ymin)*(sizeY/ydif))))))+topMargin+moveY;
																			
										
	
									//point 그리기
									String pointAttrSeq=removeStr(in.readLine(),"SYMBOLGATTR,(,)");
									//symbolgattr이 0 으로 박혀있는경우가 있음... 거의 대부분...
									if(headerResultMap.get("POINT_REP_"+pointAttrSeq)==null){
										g.setColor(Color.red);
									}else{
										String[] pointAttr=headerResultMap.get("POINT_REP_"+pointAttrSeq).split(",");
										g.setColor(getColorFromNgiCde(seplyrname,pointAttr[2]));
									}		
								//	System.out.println(drawPointInt[0]+"-"+drawPointInt[1]);
									AlphaComposite alpha;
									alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
									g.setComposite(alpha);
									//g.drawString("점", drawPointInt[0],drawPointInt[1]);
									
									//BasicStroke stroke=new BasicStroke(2.0f);
									//g.setStroke(stroke);
									//임시.. 십자가모양으로 점찍기.
									g.drawLine(drawPointInt[0]-2,drawPointInt[1],drawPointInt[0]+2,drawPointInt[1]);	
									g.drawLine(drawPointInt[0],drawPointInt[1]-2,drawPointInt[0],drawPointInt[1]+2);
									
								}
								else if(lineStr.equals("TEXT")){ //   linestring 처리 시작
									String textStr=removeStr(lineStr,"TEXT,(\",\")");
									double[] pointOri = new double[2];
									
									int[] drawPointInt=new int[2];
									
									lineStr=in.readLine();
									String[] ptStr=(lineStr.trim()).split(" ");
										
									pointOri[0]=Double.parseDouble(ptStr[0].trim());
									pointOri[1]=Double.parseDouble(ptStr[1].trim());
									
									drawPointInt[0]=Integer.parseInt(String.valueOf(Math.round(((pointOri[0]-xmin)*(sizeX/xdif)))))+leftMargin+moveX;
									drawPointInt[1]=(sizeY-Integer.parseInt(String.valueOf(Math.round(((pointOri[1]-ymin)*(sizeY/ydif))))))+topMargin+moveY;
																			
										
	
									//point 그리기
									String textAttrSeq=removeStr(in.readLine(),"TEXTGATTR,(,)");
									//symbolgattr이 0 으로 박혀있는경우가 있음... 거의 대부분...
									double rotatevalue=0.0; 
									if(headerResultMap.get("TEXT_REP_"+textAttrSeq)==null){
										g.setColor(Color.red);
									}else{
										String[] textAttr=headerResultMap.get("TEXT_REP_"+textAttrSeq).split(",");
										g.setColor(getColorFromNgiCde(seplyrname,textAttr[2]));
										//1 FONT("굴림", 32.880000, 0, 0.00, 6)
										//1 FONT("굴림", 16.440000, 431160, 0.00, 6)
										// font(폰트형식,크기,색상코드,각도,수직/수평좌표값)
									//	System.out.println("폰트사이즈ori--"+Double.parseDouble(textAttr[1]));
										int fontSize=Integer.parseInt(String.valueOf(Math.round(((Double.parseDouble(textAttr[1]))*(sizeX/xdif)))));
										//Font f=new Font(String name, String stile, int size);
										//g.setFont(f);
									//	System.out.println(textAttr[0].replace("\"","")+fontSize);
										g.setFont(new Font(textAttr[0].replace("\"",""),Font.BOLD,fontSize));
										rotatevalue=Double.parseDouble(textAttr[3]);
									}
									
									AlphaComposite alpha;
									alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
									g.setComposite(alpha);
									//g.drawString("점", drawPointInt[0],drawPointInt[1]);
									//반대로 돌아감
									g.rotate (Math.toRadians(rotatevalue)*(-1),drawPointInt[0],drawPointInt[1]);									
								//	System.out.println(textStr+"---"+rotatevalue);
									g.setColor(Color.red);
									
									g.drawLine(drawPointInt[0]-2,drawPointInt[1],drawPointInt[0]+2,drawPointInt[1]);	
									g.drawLine(drawPointInt[0],drawPointInt[1]-2,drawPointInt[0],drawPointInt[1]+2);
									g.setColor(Color.black);
									g.drawString(textStr,drawPointInt[0],drawPointInt[1]);
									//원상태로 다시 돌리기
									g.rotate (Math.toRadians(rotatevalue),drawPointInt[0],drawPointInt[1]);
									//g.rotate(-rotatevalue,drawPointInt[0],drawPointInt[1]);
								}
							}while(true);
						} //data 끝
						
						
					}while(true);
					
					
				}
			}while(true);
			in.close();
		}catch(Exception e){
			System.out.println("오류발생:"+e.toString());
		}
		//System.out.println(resultStr.toString());
		g.dispose();
		image.flush();
		return image;		
	}
	
	// TODO
	public static Color getColorFromNgiCde(String seplyrname,String codeValue){
		Color resultColor;
		
		Long intDec = Long.parseLong(codeValue.trim()); // 코드를 long으로 변환
		String hexVal=Long.toHexString(intDec).toUpperCase(); // 16진수로 변환
		int[] rgbValue={0,0,0};
		//rgp 5 5 5 혹은 5 5일경우는????
		for(int i=0;i<hexVal.length();i=i+2){
			String tmpVal="";
			if(hexVal.length()==1){
				//0 2 4
				//0 2
			   tmpVal=hexVal.substring(i, i+1);
		   }else{
			 //0 2 4 length 6
 			//0 2    length 4
			   int startIdx= hexVal.length()-(i+2);
			   int endidx=hexVal.length()-i;
			   if(startIdx<0) startIdx=0;
			   tmpVal=hexVal.substring(startIdx, endidx);
		   }
		   rgbValue[i/2]=Integer.parseInt(tmpVal,16);
		}
		 
		 
	//	System.out.println(seplyrname+":"+codeValue+"--"+rgbValue[0]+" "+rgbValue[1]+" "+rgbValue[2]);
		resultColor=new Color(rgbValue[0],rgbValue[1],rgbValue[2]);
		return resultColor;
	}
	
	// TODO
	public static String removeStr(String str,String oldChars){
		String[] oldChar=oldChars.split(",");
		for(int i=0;i<oldChar.length;i++){
			str=str.replace(oldChar[i], "");
		}
		return str.trim();
	}
	
	
	// TODO
	public static HashMap<String,String> procHeader(BufferedReader in,String layername){
		HashMap<String,String> headerResultMap=new HashMap<String, String>();
		String lineStr="";
		try{
		do{
			lineStr=(in.readLine()).trim();
			if(lineStr.equals("<END>")) break;
			
			//헤더부를 읽어 각각의 맵에 입력한다.
			if(lineStr.equals("$VERSION")){
				//$VERSION
				do{
					lineStr=(in.readLine()).trim();
					if(lineStr.equals("$END")) break;
					headerResultMap.put("VERSION", lineStr);
				}while(true);
				
			}else if(lineStr.equals("$GEOMETRIC_METADATA")){
				//$GEOMETRIC_METADATA
				do{
					lineStr=(in.readLine()).trim();
					if(lineStr.equals("$END")) break;
					if(lineStr.startsWith("MASK")) headerResultMap.put("MASK", removeStr(lineStr,"MASK,(,)"));
					else if(lineStr.startsWith("DIM")) headerResultMap.put("DIM", removeStr(lineStr,"DIM,(,)"));
					else if(lineStr.startsWith("BOUND")) headerResultMap.put("BOUND", removeStr(lineStr,"BOUND,(,)"));
				}while(true);
				
			}else if(lineStr.equals("$POINT_REPRESENT")){
				//$POINT_REPRESENT
				do{
					lineStr=(in.readLine()).trim();
					if(lineStr.equals("$END")) break;
					String seq=lineStr.substring(0,lineStr.indexOf("SYMBOL")-1);
					headerResultMap.put("POINT_REP_"+seq, removeStr(lineStr.substring(lineStr.indexOf("SYMBOL"),lineStr.length()),"SYMBOL,(,)"));
					//System.out.println("POINT_REP_"+seq+"----"+removeStr(lineStr.substring(lineStr.indexOf("SYMBOL"),lineStr.length()),"SYMBOL,(,)"));
				}while(true);
			
				
			}else if(lineStr.equals("$REGION_REPRESENT")){ 
				//$REGION_REPRESENT
				do{
					lineStr=(in.readLine()).trim();
					if(lineStr.equals("$END")) break;
					String seq=lineStr.substring(0,lineStr.indexOf("REGIONATTR")-1);
					headerResultMap.put("REGION_REP_"+seq, removeStr(lineStr.substring(lineStr.indexOf("REGIONATTR"),lineStr.length()),"REGIONATTR,(,)"));
				//	System.out.println("REGION_REP_"+seq+"-----"+ removeStr(lineStr.substring(lineStr.indexOf("REGIONATTR"),lineStr.length()),"REGIONATTR,(,)"));	
					
				}while(true);
			
			}else if(lineStr.equals("$LINE_REPRESENT")){
				//$LINE_REPRESENT
				do{
					lineStr=(in.readLine()).trim();
					if(lineStr.equals("$END")) break;
					String seq=lineStr.substring(0,lineStr.indexOf("LINEATTR")-1);
					headerResultMap.put("LINE_REP_"+seq, removeStr(lineStr.substring(lineStr.indexOf("LINEATTR"),lineStr.length()),"LINEATTR,(,)"));
					
				}while(true);
			
				headerResultMap.put("MASK", "");
			}else if(lineStr.equals("$TEXT_REPRESENT")){
				//$TEXT_REPRESENT
				do{
					lineStr=(in.readLine()).trim();
					if(lineStr.equals("$END")) break;
					String seq=lineStr.substring(0,lineStr.indexOf("FONT")-1);
					headerResultMap.put("TEXT_REP_"+seq, removeStr(lineStr.substring(lineStr.indexOf("FONT"),lineStr.length()),"FONT,(,)"));
					
				}while(true);
			
				headerResultMap.put("MASK", "");
			}
			
		}while(true);
		}catch(Exception e){
			System.out.println("header 처리오류="+e.toString());
		}
	//	System.out.println("********************"+headerResultMap.size());
		return headerResultMap;
	}
	
	
	//전체 Extent 반환함수 bound값 오류시 전체 좌표에서
		public static Double[] getExtentNgiByValue(String folderpath, String reqfilename) {
			
			Double[] minMax = { 99999999.0, 9999999.0, 0.0, 0.0};
			
			String lineStr="";
			int bndcnt=0;
			try{
				BufferedReader in = new BufferedReader(new FileReader(folderpath + File.separator + reqfilename));
				do {				
					lineStr=in.readLine();	// 한줄씩 읽기
					
					if(lineStr==null) break;
					if(lineStr.equals("EOF")||lineStr==null) break;
									
					if(lineStr.equals("<DATA>")) //data부분 routine시작 
					{					
						do {
							lineStr=in.readLine();
							if(lineStr.equals("<END>")) break; // data 섹션 종료
														
							if(lineStr.equals("POLYGON")) //  POLYGON 처리 시작
							{ 
								String numpart=(in.readLine()).replace("NUMPARTS", "").trim();
								int numpartInt=Integer.parseInt(numpart);
								
								for(int parts = 0; parts < numpartInt; parts++) 
								{
									int ptCnt=Integer.parseInt((in.readLine()).trim());
									//ngi 분석결과 polygon 일시 끝점이 시작점이 아니라서 첫번째 포인트를 저장해서 나중에 연결해야하는거 같음.
									double[] pointOri = new double[2];
									
									for(int i = 0; i < ptCnt; i++) // 폴리곤시작
									{ 
										lineStr = in.readLine();
										String[] ptStr = (lineStr.trim()).split(" ");
										
										pointOri[0] = Double.parseDouble(ptStr[0].trim());
										pointOri[1] = Double.parseDouble(ptStr[1].trim());
										
										if(minMax[0] > pointOri[0]) minMax[0] = pointOri[0]; //xmin									
										if(minMax[1] > pointOri[1]) minMax[1] = pointOri[1]; //ymin									
										if(minMax[2] < pointOri[0]) minMax[2] = pointOri[0]; //xmax									
										if(minMax[3] < pointOri[1]) minMax[3] = pointOri[1]; //ymax
									}	
								}							
							}
							else if(lineStr.equals("LINESTRING"))//LINESTRING 처리 시작
							{ 
								int ptCnt = Integer.parseInt((in.readLine()).trim());
								
								double[] pointOri = new double[2];
								
								for(int i = 0; i < ptCnt; i++) {
									// 라인스트링시작
									lineStr = in.readLine();
									String[] ptStr = (lineStr.trim()).split(" ");
									
									pointOri[0] = Double.parseDouble(ptStr[0].trim());
									pointOri[1] = Double.parseDouble(ptStr[1].trim());
									
									
									if(minMax[0] > pointOri[0]) minMax[0] = pointOri[0];//xmin								
									if(minMax[1] > pointOri[1]) minMax[1] = pointOri[1];//ymin								
									if(minMax[2] < pointOri[0]) minMax[2] = pointOri[0];//xmax								
									if(minMax[3] < pointOri[1]) minMax[3] = pointOri[1];//ymax
								}
							}
							else if(lineStr.equals("POINT")) //POINT 처리 시작
							{
								double[] pointOri = new double[2];
								lineStr = in.readLine();
								String[] ptStr = (lineStr.trim()).split(" ");
									
								pointOri[0] = Double.parseDouble(ptStr[0].trim());
								pointOri[1] = Double.parseDouble(ptStr[1].trim());
								
								if(minMax[0] > pointOri[0]) minMax[0] = pointOri[0];//xmin							
								if(minMax[1] > pointOri[1]) minMax[1] = pointOri[1];//ymin							
								if(minMax[2] < pointOri[0]) minMax[2] = pointOri[0];//xmax							
								if(minMax[3] < pointOri[1]) minMax[3] = pointOri[1];//ymax
							}
							else if(lineStr.equals("TEXT")) //TEXT 처리 시작
							{ 
								double[] pointOri = new double[2];
								lineStr = in.readLine();
								String[] ptStr = (lineStr.trim()).split(" ");
								
								pointOri[0]=Double.parseDouble(ptStr[0].trim());
								pointOri[1]=Double.parseDouble(ptStr[1].trim());							
								
								if(minMax[0] > pointOri[0]) minMax[0] = pointOri[0];//xmin							
								if(minMax[1] > pointOri[1]) minMax[1] = pointOri[1];//ymin							
								if(minMax[2] < pointOri[0]) minMax[2] = pointOri[0];//xmax							
								if(minMax[3] < pointOri[1]) minMax[3] = pointOri[1];//ymax							
							}
						} while(true);
					} 
					
					bndcnt++;
					
				} while(true);
				
				in.close();
				
			} catch(IOException ie){			
				ie.printStackTrace();
			} catch(NumberFormatException nfe) { 
				nfe.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			return minMax;
		}
		
}
