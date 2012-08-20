package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class PrintSetDocument
	extends ObjectBase
{
	public static class I
	{
		public static int SET     =  0;
		public static int FILE    =  1;
		public static int EXCLUDE =  2;
	}

    public static PrintSetDocument GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (PrintSetDocument)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PrintSetDocument), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static PrintSetDocument GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (PrintSetDocument)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PrintSetDocument), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
	}

	public FileXfer getFile()
	{
		java.lang.Object lobjFile;

		lobjFile = getAt(I.FILE);

		if ( lobjFile == null )
			return null;

		if ( lobjFile instanceof FileXfer )
			return (FileXfer)lobjFile;

		return new FileXfer((byte[])lobjFile);
	}

    public PrintSetDetail[] getCurrentDetails()
    	throws BigBangJewelException
    {
		ArrayList<PrintSetDetail> larrAux;
		IEntity lrefDetails;
        MasterDB ldb;
        ResultSet lrsDetails;

		larrAux = new ArrayList<PrintSetDetail>();

		try
		{
			lrefDetails = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PrintSetDetail)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDetails = lrefDetails.SelectByMembers(ldb, new int[] {PrintSetDetail.I.DOCUMENT},
					new java.lang.Object[] {getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsDetails.next() )
				larrAux.add(PrintSetDetail.GetInstance(getNameSpace(), lrsDetails));
		}
		catch (BigBangJewelException e)
		{
			try { lrsDetails.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsDetails.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDetails.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new PrintSetDetail[larrAux.size()]);
    }
}
