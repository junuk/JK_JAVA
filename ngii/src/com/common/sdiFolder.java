package com.common;

import java.io.File;


public class sdiFolder {
	
	private sdiLog logging;
	
	public sdiFolder() {}
	

	/**
	 * @Method Name  : createFolder
	 * @작성일   : 2015. 10. 13. 
	 * @작성자   : BAAAM
	 * @변경이력  :
	 * @Method 설명 : 임의의 경로를 넣으면 해당 경로로 디렉토리를 생성해준다
	 * @param path
	 */
	public void createFolder(String path)
	{
		File folder = new File(path);
		
		boolean result = false;
		logging = new sdiLog();
		
		try {
			if(!folder.exists())
			{
				result = folder.mkdirs();
				if(result == false)
					logging.info("디렉토리를 생성 할 수 없습니다.");
				else
					logging.info("디렉토리를 생성 합니다.");
			}
			else
				logging.info("디렉토리가 존재 합니다.");
					
		} catch (Exception e) {
			// TODO: handle exception
			logging.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	

}
