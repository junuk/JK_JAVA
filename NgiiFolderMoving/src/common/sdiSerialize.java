package com.common;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class sdiSerialize {

	//--
	private static final String PARSER = ",";
	private static final String REGEX = "#";
	
	public sdiSerialize() {}
	
	public String get(String field)
		throws IllegalAccessException
	{
		Field [] fields = this.getClass().getFields();
		for (Field obj : fields)
		{
			if (obj.getName().equalsIgnoreCase(field) && obj.get(this) != null)
				return obj.get(this).toString();
		}
		return null;
	}
	
	public void mapping(String name, String value)
		throws IllegalAccessException
	{
		Field [] arrField = this.getClass().getFields();
		for (int i=0; i < arrField.length; i++)
		{
			if (!arrField[i].getName().equalsIgnoreCase(name))
				continue;
			if (arrField[i].getType().toString().equals("int"))
				arrField[i].setInt(this, Integer.parseInt(value));
			else if (arrField[i].getType().toString().equals("long"))
				arrField[i].setLong(this, Long.parseLong(value));
			else if (arrField[i].getType().toString().equals("boolean"))
				arrField[i].setBoolean(this, Boolean.parseBoolean(value));
			else
				arrField[i].set(this, value);
		}
	}
	
	public String parsing(String pattern)
		throws IllegalAccessException
	{
		String convert = "";
		String columns[] = pattern.split(PARSER);
		for (String column : columns)
		{
			convert += (this.get(column) != null) ? this.get(column) : column;
		}
		return convert;
	}
	
	public String parsing(String text, String pattern)
		throws IllegalAccessException
	{
		if (pattern == "" || pattern == null)
			return text;
		
		String convert = "";
		String columns[] = pattern.split(PARSER);
		for (String column : columns)
		{
			if (column.startsWith(REGEX))
			{
				Pattern p = Pattern.compile(column.replace(REGEX, ""));
				Matcher m = p.matcher(text);
				convert += m.find() ? m.group() : column;
			}
			else
				convert += (this.get(column) != null) ? this.get(column) : column;
		}
		return convert;
	}
	
	public String serialize()
	{
		return "";
	}
	
	public String serialize(enDocType type) 
		throws IllegalAccessException
	{
		String result = "";
		Field [] arrField = this.getClass().getFields();
		for (int i=0; i<arrField.length; i++)
		{
			switch (type)
			{
			case CSV:
				result += ((i == 0) ? "" : ";");
				result += arrField[i].get(this);
				//result += ((i == arrField.length-1) ? "\n" : "");
				break;
			case JSON:
				result += ((i == 0) ? "{" : ",");
				result += "\"" + arrField[i].getName() + "\":\"" + arrField[i].get(this) + "\"";
				result += ((i == arrField.length-1) ? "}" : "");
				break;
			case XML:
				result +=  "<" + arrField[i].getName() + ">";
				result += (arrField[i].get(this) == null) ? "" : arrField[i].get(this);
				result += "</" + arrField[i].getName() + ">";
				break;
			}
		}
		/* later refer to below - by ProfessorX
		for (i=0; i<arrMethod.length; i++)
		{
			String result = arrMethod[i].invoke(this).toString();
		}
		*/		
		return result;
	}
	
	public String serialize(enDocTag tag, enDocType type) 
		throws IllegalAccessException
	{
		String result = "";
		Field [] arrField = this.getClass().getFields();
		for (int i=0; i<arrField.length; i++)
		{
			switch (type)
			{
			case CSV:
				result += ((i == 0) ? "" : ";");
				result += arrField[i].get(this);
				//result += ((i == arrField.length-1) ? "\n" : "");
				break;
			case JSON:
				result += ((i == 0) ? "{" : ",");
				result += "\"" + arrField[i].getName() + "\":\"" + arrField[i].get(this) + "\"";
				result += ((i == arrField.length-1) ? "}" : "");
				break;
			case XML:
				result += ((i == 0) ? "<" + tag + ">" : "");
				result +=  "<" + arrField[i].getName() + ">";
				result += (arrField[i].get(this) == null) ? "" : arrField[i].get(this);
				result += "</" + arrField[i].getName() + ">";
				result += ((i == arrField.length-1) ? "</" + tag + ">" : "");
				break;
			}
		}
		return result;
	}
	
	public void deserialize(enDocTag tag, enDocType type, String queue) 
		throws UnsupportedEncodingException, ParserConfigurationException, IOException, SAXException, IllegalAccessException, Exception
	{
		Field [] arrField = this.getClass().getFields();		
		InputStream input = new ByteArrayInputStream(queue.getBytes("UTF-8"));
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(input);
		NodeList nodes = doc.getDocumentElement().getElementsByTagName(tag.toString()).item(0).getChildNodes();
		
		if (arrField.length != nodes.getLength())
			throw new Exception("Document Node(XML)�� Class Field�� ��ġ���� �ʽ��ϴ�.");
		
		for (int i=0; i < nodes.getLength(); i++)
		{
			if (arrField[i].getType().toString().equals("int"))
				arrField[i].setInt(this, Integer.parseInt(nodes.item(i).getTextContent()));
			else if (arrField[i].getType().toString().equals("long"))
				arrField[i].setLong(this, Long.parseLong(nodes.item(i).getTextContent()));
			else if (arrField[i].getType().toString().equals("boolean"))
				arrField[i].setBoolean(this, Boolean.parseBoolean(nodes.item(i).getTextContent()));
			else
				arrField[i].set(this, nodes.item(i).getTextContent());
		}
	}
	
	public void deserialize(String tag, enDocType type, InputStream input) 
		throws UnsupportedEncodingException, ParserConfigurationException, IOException, SAXException, IllegalAccessException, Exception
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(input);
		NodeList nodes = doc.getDocumentElement().getElementsByTagName(tag).item(0).getChildNodes();
		
		//if (arrField.length != nodes.getLength())
		//	throw new Exception("Document Node(XML)�� Class Field�� ��ġ���� �ʽ��ϴ�.");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			this.mapping(nodes.item(i).getNodeName(), nodes.item(i).getTextContent());
		}
		
		input.close();
	}
	
	public void deserialize(enDocTag tag, enDocType type, InputStream input) 
		throws UnsupportedEncodingException, ParserConfigurationException, IOException, SAXException, IllegalAccessException, Exception
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(input);
		NodeList nodes = doc.getDocumentElement().getElementsByTagName(tag.toString()).item(0).getChildNodes();
		
		//if (arrField.length != nodes.getLength())
		//	throw new Exception("Document Node(XML)�� Class Field�� ��ġ���� �ʽ��ϴ�.");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			this.mapping(nodes.item(i).getNodeName(), nodes.item(i).getTextContent());
		}
		
		input.close();
	}
	
	public void deserialize(enDocTag tag, String element, enDocType type, InputStream input) 
		throws UnsupportedEncodingException, ParserConfigurationException, IOException, SAXException, IllegalAccessException, Exception
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(input);
		NodeList nodes = ((Element) doc.getDocumentElement().getElementsByTagName(tag.toString()).item(0))
							.getElementsByTagName(element).item(0).getChildNodes();
		
		//if (arrField.length != nodes.getLength())
		//	throw new Exception("Document Node(XML)�� Class Field�� ��ġ���� �ʽ��ϴ�.");
		for (int i = 0; i < nodes.getLength(); i++)
		{
			if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
				
			this.mapping(nodes.item(i).getNodeName(), nodes.item(i).getTextContent());
		}
		
		input.close();
	}
	
	public void deserialize(enDocType type, Node obj) 
		throws IllegalAccessException, Exception
	{
		Field [] arrField = this.getClass().getFields();
		NodeList nodes = obj.getChildNodes();
		
		if (arrField.length != nodes.getLength())
			throw new Exception("Document Node(XML)�� Class Field�� ��ġ���� �ʽ��ϴ�.");
		for (int i=0; i < nodes.getLength(); i++)
		{
			if (arrField[i].getType().toString().equals("int"))
				arrField[i].setInt(this, Integer.parseInt(nodes.item(i).getTextContent()));
			else if (arrField[i].getType().toString().equals("long"))
				arrField[i].setLong(this, Long.parseLong(nodes.item(i).getTextContent()));
			else if (arrField[i].getType().toString().equals("boolean"))
				arrField[i].setBoolean(this, Boolean.parseBoolean(nodes.item(i).getTextContent()));
			else
				arrField[i].set(this, nodes.item(i).getTextContent());
		}
	}
	
}
