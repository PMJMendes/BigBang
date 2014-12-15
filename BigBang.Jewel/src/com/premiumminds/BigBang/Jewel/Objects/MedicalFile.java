package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class MedicalFile
	extends ProcessData
{
	public static class I
	{
		public static int REFERENCE   = 0;
		public static int SUBCASUALTY = 1;
		public static int PROCESS     = 2;
		public static int NEXTDATE    = 3;
		public static int NOTES       = 4;
	}

    public static MedicalFile GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MedicalFile)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MedicalFile), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static MedicalFile GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MedicalFile)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MedicalFile), prsObject);
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

	public UUID GetProcessID()
	{
		return (UUID)getAt(I.PROCESS);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(I.PROCESS, pidProcess);
	}

    public MedicalDetail[] GetCurrentDetails()
    	throws BigBangJewelException
    {
		ArrayList<MedicalDetail> larrAux;
		IEntity lrefDetails;
        MasterDB ldb;
        ResultSet lrsDetails;

		larrAux = new ArrayList<MedicalDetail>();

		try
		{
			lrefDetails = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MedicalDetail)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDetails = lrefDetails.SelectByMembers(ldb, new int[] {MedicalDetail.I.FILE},
					new java.lang.Object[] {getKey()}, new int[] {MedicalDetail.I.STARTDATE});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsDetails.next() )
				larrAux.add(MedicalDetail.GetInstance(getNameSpace(), lrsDetails));
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

		return larrAux.toArray(new MedicalDetail[larrAux.size()]);
    }

    public MedicalAppointment[] GetCurrentAppts()
    	throws BigBangJewelException
    {
		ArrayList<MedicalAppointment> larrAux;
		IEntity lrefDetails;
        MasterDB ldb;
        ResultSet lrsDetails;

		larrAux = new ArrayList<MedicalAppointment>();

		try
		{
			lrefDetails = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MedicalAppointment)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsDetails = lrefDetails.SelectByMembers(ldb, new int[] {MedicalAppointment.I.FILE},
					new java.lang.Object[] {getKey()}, new int[] {MedicalAppointment.I.DATE});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsDetails.next() )
				larrAux.add(MedicalAppointment.GetInstance(getNameSpace(), lrsDetails));
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

		return larrAux.toArray(new MedicalAppointment[larrAux.size()]);
    }
}
