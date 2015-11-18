/**
 * 
 */
package com.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Stack;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.vo.NgiiDataDomain;

/**
 * @FileName  : sdiZip.java
 * @Project     : ngii
 * @Date         : 2015. 10. 29. 
 * @작성자      : BAAAM
 * @변경이력 :
 * @프로그램 설명 :	Apache Commons Compress 를 이용한 NGI / DXF 압축
 */
public class sdiZip {
	
	private sdiLog logging = new sdiLog();
	
	public void zip(NgiiDataDomain target) throws IOException
	{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipArchiveOutputStream zos = null;
		ZipArchiveEntry ze = null;
		String name = null;
		
		try {
			fos = new FileOutputStream(new File(target.getSpatialFileDir() + File.separator + target.getSpatialFileName() + ".zip"));
			zos = new ZipArchiveOutputStream(fos);
			zos.setEncoding("UTF-8");
			
			int length;
			byte[] buf = new byte[8*1024];
			File root = new File(target.getSpatialFileDir());
			
			Stack<File> stack = new Stack<File>();
			
			// NGI 타입 압축 처리
			if(target.getSpatialFileFormat().equals("NGI") || target.getSpatialFileFormat().equals("ngi"))
			{
				stack.push(target.getSpatialFile());
				stack.push(target.getSubSpatialFile());
			}
			// DXF 타입 압축 처리
			else if(target.getSpatialFileFormat().equals("DXF") || target.getSpatialFileFormat().equals("dxf"))
			{
				stack.push(target.getSpatialFile());
			}
			
			while (!stack.isEmpty()) 
			{
				File f = stack.pop();
				name = toPath(root, f);
				System.out.println("name -> " + name);
				ze = new ZipArchiveEntry(name);
				zos.putArchiveEntry(ze);
				fis = new FileInputStream(f);
				
				while((length = fis.read(buf, 0, buf.length)) >= 0)
				{
					zos.write(buf, 0, length);
				}
				fis.close();
				zos.closeArchiveEntry();
			}
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String toPath(File root, File dir){
		String path = dir.getAbsolutePath();
		path = path.substring(root.getAbsolutePath().length()).replace(File.separatorChar, '/');
		if ( path.startsWith("/")) path = path.substring(1);
		if ( dir.isDirectory() && !path.endsWith("/")) path += "/" ;
		return path ;
	}

}
