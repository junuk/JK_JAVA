package com.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class sdiLog {

	//--
	private static final String NAME_POLICY = "yyyyMMdd_HHmmss";
	private static final String LINE_SEPERATOR = System.getProperty("line.separator");
	//--
	private String path = "";
	private String owner = "", key = "", service = "", module = "";
	private boolean debug = false;
	private String title = "", description = "";
	
	private List<String> messages = null, errors = null;
	private List<File> files = new ArrayList<File>();	
	
	// 폴더생성 클래스
	private sdiFolder sdifolder;
	
	//--
	public sdiLog() {}
	
	//--
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public void setOwner(String owner)
	{
		this.owner = owner;
	}
	
	public void setKey(String key)
	{
		this.key = key;
	}
	
	public void setService(String service)
	{
		this.service = service;
	}
	
	public void setModule(String module)
	{
		this.module = module;
	}
	
	public void setDebug(boolean flag)
	{
		this.debug = flag;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public List<File> getFiles()
	{
		return this.files;
	}
	
	//--
	public String formatPath(String path, boolean format)
	{
		/*String logDir = this.path + ((this.owner == "") ? "Common" : this.owner) + "/" + this.service + "/" + path + "/";
		System.out.println("로그생성경로 => " + logDir);*/
		if (format)
			//return this.path + ((this.owner == "") ? "Common" : this.owner) + "/" + this.module + "/" + path + "/";
			return this.path + ((this.owner == "") ? "Common" : this.owner) + "/" + this.service + "/" + path + "/";
		else
			return this.path + "/" + path + "/";
	}
	public String formatName()
	{
		return ((this.key == "") ? "" : this.key + "_") + this.module + "_" + formatDate(NAME_POLICY);
	}
	public String formatDate(String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	//--
	public void startLog()
	{
		this.messages = new ArrayList<String>();
		this.errors = new ArrayList<String>();
		
		this.write("*====================================================================================", false);
		this.write(LINE_SEPERATOR, false);
		
		System.out.println("*----------------------------------------------------------------");
		if (this.module != "")
			System.out.println("* " + this.module + " *");
		if (this.title != "")
			System.out.println("* " + this.title + " *");
		if (this.description != "")
			System.out.println("* - Description  : " + this.description);
		System.out.println("* - Procssing Date : " + formatDate("yyyy-MM-dd HH:mm:ss"));
		System.out.println("*----------------------------------------------------------------");
	}
	public void startLog(String module)
	{
		this.setModule(module);
		this.startLog();
	}
	public void finishLog(boolean format)
		throws IOException
	{
		this.write("*====================================================================================", false);
		
		String path = this.formatPath("Log", format);
		String name = this.formatName();
		
		// 폴더 경로
		sdiMethod proc = new sdiMethod();
		Properties prop = new Properties();
		// 로그폴더 경로 (Path를 변경함 프로퍼티로)
		prop = proc.readProperties("property/ngii.properties");
		path = prop.getProperty("ngii.one.file.log.dir");
				
		// 폴더생성
		this.sdifolder = new sdiFolder();
		sdifolder.createFolder(path);
		
		if (this.messages.size() > 0)
		{
			FileWriter logger = new FileWriter(path + name + ".log", false);
			logger.write("*----------------------------------------------------------------");
			logger.write(LINE_SEPERATOR);
			logger.write("* " + this.title + " *");
			logger.write(LINE_SEPERATOR);
			logger.write(LINE_SEPERATOR);
			logger.write("* - Description  : " + this.description);
			logger.write(LINE_SEPERATOR);
			logger.write("* - Procssing Date : " + formatDate("yyyy-MM-dd HH:mm:ss"));
			logger.write(LINE_SEPERATOR);
			logger.write("*----------------------------------------------------------------");
			logger.write(LINE_SEPERATOR);
			logger.flush();
			for (int i = 0; i < this.messages.size(); i++)
			{
				logger.write(this.messages.get(i));
			}
			logger.close();
			//this.files.add(new File("Log", this.formatName() + ".log"));
			this.files.add(new File(path, name + ".log"));
		}
		if (this.errors.size() > 0)
		{
			FileWriter logger = new FileWriter(path + name + ".err", false);
			logger.write("*----------------------------------------------------------------");
			logger.write(LINE_SEPERATOR);	
			logger.write("* " + this.title + " *");
			logger.write(LINE_SEPERATOR);
			logger.write(" - Error (Exception) is occured in " + formatDate("yyyy-MM-dd HH:mm:ss"));
			logger.write(LINE_SEPERATOR);
			logger.write("*----------------------------------------------------------------");
			logger.write(LINE_SEPERATOR);
			logger.flush();
			for (int i = 0; i < this.errors.size(); i++)
			{
				logger.write(this.errors.get(i));
			}
			logger.close();
			//this.files.add(new File("Log", this.formatName() + ".err"));
			this.files.add(new File(path, name + ".err"));
		}	
	}
	
	//--
	public void info(String msg)
	{
		this.write("(LOG) " + msg, true);
		this.write(LINE_SEPERATOR, false);
	}
	public void error(String msg)
	{
		String err = "*ERR* " + msg; 
		this.write(err, true);
		this.write(LINE_SEPERATOR, false);
		this.except(err + LINE_SEPERATOR);
		
	}
	public void error(String msg, Exception e)
	{
		String err = "*ERR* " + msg;
		this.write(err, true);
		this.write(LINE_SEPERATOR, false);
		this.except(err + LINE_SEPERATOR);
	}
	public void fatal(String msg)
	{
		String err = "*FATAL* " + msg; 
		this.write(err, true);
		this.write(LINE_SEPERATOR, false);
		this.except(err + LINE_SEPERATOR);
	}
	public void fatal(String msg, Exception e)
	{
		String err = "*ERR* " + msg + ((e.getMessage() != null) ? e.getMessage() : ""); 
		this.write(err, true);
		this.write(LINE_SEPERATOR, false);
		this.except(err + LINE_SEPERATOR);
	}	
	public void debug(String msg)
	{
		if (this.debug)
		{
			this.write("!DEBUG! " + msg, true);
			this.write(LINE_SEPERATOR, false);
		}
	}
	public void debug(Exception e)
	{
		if (this.debug)
		{
			e.printStackTrace();	
		}
	}
	public void debug(String msg, Exception e)
	{
		if (this.debug)
		{
			this.write("!DEBUG! " + msg + ((e.getMessage() != null) ? e.getMessage() : ""), false);
			this.write(LINE_SEPERATOR, false);
		}
	}
	public void console(String msg)
	{
		System.out.println("[" + formatDate("yyyy-MM-dd HH:mm:ss") + "] " + msg);
	}
	public void console(String msg, Exception e)
	{
		System.out.println("[" + formatDate("yyyy-MM-dd HH:mm:ss") + "] " + msg + " => " + e.toString());
	}

	//--
	public void write(String msg, boolean date)
	{
		if (msg != LINE_SEPERATOR)
			System.out.println(((date) ? "[" + formatDate("yyyy-MM-dd HH:mm:ss") + "] " : "") + msg);
		if (this.messages != null)
		{
			if (msg != LINE_SEPERATOR)
				msg = ((date) ? "[" + formatDate("yyyy-MM-dd HH:mm:ss") + "] " : "") + msg;
			this.messages.add(msg);
		}
	}
	public void except(String msg)
	{
		if (this.errors != null)
		{
			if (msg != LINE_SEPERATOR)
				msg = formatDate("yyyy-MM-dd HH:mm:ss") + "] " + msg;
			this.errors.add(msg);
		}
	}
	
	//--
	public void clear()
		throws IOException
	{
		//this.finishLog();
	}
	
}
