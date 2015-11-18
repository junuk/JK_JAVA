package com.common;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class sdiDB {

	private static SqlSessionFactory dbMapper = null;
	private SqlSession dbSession = null;
	private boolean transact = false;
	
	
	
	public void openSession(String path)
		throws IOException, SQLException
	{
		if (dbMapper == null)
		{
			Reader reader = Resources.getResourceAsReader(path);
			dbMapper = new SqlSessionFactoryBuilder().build(reader);
			reader.close();
		}
		dbSession = dbMapper.openSession();
		dbSession.getConnection().setAutoCommit(true);		// setAutoCommit
	}
	public void closeSession()
		throws IOException, Exception
	{
		if (this.dbSession != null)
		{
			closeTransaction(enTransaction.COMMIT);
			dbSession.close();
		}
	}
	
	public SqlSession getSession()
	{
		return this.dbSession;
	}
	
	public void openTransaction()
		throws Exception
	{
		if (transact)
			throw new Exception("이전 Transaction이 종료되지 않았습니다.");		
		this.transact = true;
	}
	
	public void closeTransaction(enTransaction action)
	//	throws Exception
	{
		//if (!transact)
		//	throw new Exception("Transaction이 시작되지 않았습니다.");
		if (transact)
		{
			if (action == enTransaction.COMMIT)
				this.dbSession.commit();
			else if (action == enTransaction.ROLLBACK)
				this.dbSession.rollback();
			this.transact = false;
		}
	}
	
	public void commit()
	{
		this.dbSession.commit();
	}
	public void rollback()
	{
		this.dbSession.rollback();
	}
	
}