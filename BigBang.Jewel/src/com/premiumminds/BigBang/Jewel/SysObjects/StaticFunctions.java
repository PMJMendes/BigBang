package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

public class StaticFunctions
{
	public static void DoLogin(UUID pidNameSpace, UUID pidUser, boolean pbNested)
		throws BigBangJewelException
	{
		String lstrPrinter;
		UserDecoration lobjDeco;
		HashMap<String, String> larrParams;
		UUID lidParams;
        MasterDB ldb;
        ResultSet lrs;
		ObjectBase lobjParam;

		if ( pbNested )
			return;

    	larrParams = new HashMap<String, String>();
    	lobjDeco = null;

    	ldb = null;
    	lrs = null;
		try
		{
			ldb = new MasterDB();

			lidParams = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AppParams);
			lrs = Entity.GetInstance(lidParams).SelectAll(ldb);
			while (lrs.next())
			{
				lobjParam = Engine.GetWorkInstance(lidParams, lrs);
				larrParams.put((String)lobjParam.getAt(2), (String)lobjParam.getAt(1));
			}
			lrs.close();
	    	lrs = null;

	        ldb.Disconnect();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			if ( ldb != null ) try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		Engine.getUserData().put("MailServer", larrParams.get("SERVER"));

		Engine.getUserData().put("Printer", larrParams.get("PRINTER"));

    	lobjDeco = UserDecoration.GetByUserID(pidNameSpace, pidUser);
		if ( lobjDeco == null )
			return;

		lstrPrinter = (String)lobjDeco.getAt(UserDecoration.I.PRINTERNAME);
		if(lstrPrinter != null)
		{
			Engine.getUserData().put("Printer", lstrPrinter);
		}
	}

	public static void DoStartup(UUID pidNameSpace)
		throws BigBangJewelException
	{
		throw new BigBangJewelException("Esta funcionalidade está desactivada.");

//		try
//		{
//			PetriEngine.StartupByScript(pidNameSpace, Constants.ProcID_SubPolicy);
//			PetriEngine.StartupAllProcesses(pidNameSpace);
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
	}

	public static void AcctMovGen(UUID pidNameSpace)
		throws BigBangJewelException
	{
		if ( !Constants.NSID_BigBang.equals(pidNameSpace) )
			return;

		try
		{
			Engine.pushNameSpace(Constants.NSID_CredEGS, null);
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		AccountingExporter.Export();

		try
		{
			Engine.popNameSpace();
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		/*try
		{
			Engine.pushNameSpace(Constants.NSID_AMartins, null);
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		AccountingExporter.Export();

		try
		{
			Engine.popNameSpace();
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}*/

		return;
	}

	public static void AutoCreateExercises(UUID pidNameSpace)
		throws BigBangJewelException
	{
		if ( !Constants.NSID_BigBang.equals(pidNameSpace) )
			return;

		try
		{
			Engine.pushNameSpace(Constants.NSID_CredEGS, Constants.UID_Root);
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		Exerciser.CreateExercises();

		try
		{
			Engine.popNameSpace();
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		/*try
		{
			Engine.pushNameSpace(Constants.NSID_AMartins, Constants.UID_Root);
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		Exerciser.CreateExercises();

		try
		{
			Engine.popNameSpace();
		}
		catch (JewelEngineException e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}*/

		return;
	}

	public static void DoSpecial(UUID pidNameSpace)
		throws BigBangJewelException
	{
		try
		{
			Engine.pushNameSpace(Constants.NSID_CredEGS, null);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
//			InnerDoSpecial();
		}
		catch (Throwable e)
		{
			try { Engine.popNameSpace(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			Engine.popNameSpace();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

//	private static int InnerDoSpecial()
//		throws BigBangJewelException
//	{
//		ArrayList<UndoInsurerAccounting> larrOps;
//		InsurerAccountingSet lobjSet;
//		UndoInsurerAccounting lopIA;
//		MasterDB ldb;
//		int i;
//
//		larrOps = new ArrayList<UndoInsurerAccounting>();
//		try
//		{
//			lobjSet = InsurerAccountingSet.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString("A4F75C91-CF6F-4A02-982B-A3E200C345FB"));
//			for (TransactionMapBase lobjMap : lobjSet.getCurrentMaps())
//			{
//				if ( lobjMap.isSettled() )
//					continue;
//
//				for (TransactionDetailBase lobjDetail : lobjMap.getCurrentDetails())
//				{
//					lopIA = new UndoInsurerAccounting(lobjDetail.getReceipt().GetProcessID());
//					lopIA.midNameSpace = Engine.getCurrentNameSpace();
//					lopIA.midSourceLog = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjDetail.getReceipt().GetProcessID()).GetLiveLog(Constants.OPID_Receipt_InsurerAccounting).getKey();
//					larrOps.add(lopIA);
//				}
//			}
//			ldb = new MasterDB();
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		try
//		{
//			ldb.BeginTrans();
//		}
//		catch (Throwable e)
//		{
//			try { ldb.Disconnect(); } catch (Throwable e1) {}
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		i = 0;
//		try
//		{
//			for (UndoInsurerAccounting lop : larrOps)
//			{
//				lop.Execute(ldb);
//				i++;
//			}
//		}
//		catch (Throwable e)
//		{
//			try { ldb.Rollback(); } catch (Throwable e1) {}
//			try { ldb.Disconnect(); } catch (Throwable e1) {}
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		try
//		{
//			ldb.Commit();
//		}
//		catch (Throwable e)
//		{
//			try { ldb.Disconnect(); } catch (Throwable e1) {}
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		try
//		{
//			ldb.Disconnect();
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		return i;
//	}
}
