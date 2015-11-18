package com.common;

@SuppressWarnings("serial")
public class sdiException extends Exception{

	private enException type = enException.NONE;
	private String name = "ERROR";
	private String brief;
	private String message;
	
	public sdiException() {}
	public sdiException(String log)
	{
		this.brief = log;
		this.message =log;
	}
	public sdiException(enException type, String log)
	{
		this.type = type;
		this.brief = log;
		this.message = log;
	}
	public sdiException(enException type, String log, Exception e)
	{
		this.type = type;
		this.name = e.getClass().getSimpleName();
		this.brief = log + " (" + this.name + ")";
		this.message = brief + " => " + e.toString();
	}
	public enException getType()
	{
		return this.type;
	}
	public void setType(enException type)
	{
		this.type = type;
	}
	
	public String getBrief()
	{
		return this.brief;
	}
	public void setBrief(String brief)
	{
		this.brief = brief;
	}
	
	public String getMessage()
	{
		return this.message + ((this.type != enException.NONE) ? " (" + this.type.toString() + ")" : "");
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	
}
