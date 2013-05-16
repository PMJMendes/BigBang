package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.ExpenseData;
import com.premiumminds.BigBang.Jewel.Objects.Expense;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class CreateExpense
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExpenseData mobjData;
	public transient DSBridgeData mobjImage;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateExpense(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_CreateHealthExpense;
	}

	public String ShortDesc()
	{
		return "Criação de Despesa de Saúde";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criada a seguinte despesa de saúde:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Expense lobjAux;
		IScript lobjScript;
		IProcess lobjProc;
		DocumentData lobjDoc;

		try
		{
			if ( mobjData.midManager == null )
				mobjData.midManager = GetProcess().GetManagerID();
			if ( mobjData.mstrNumber == null )
				mobjData.mstrNumber = GetNewExpenseNumber();

			lobjAux = Expense.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			if ( mobjImage != null )
			{
				lobjDoc = new DocumentData();
				lobjDoc.mstrName = "Original";
				lobjDoc.midOwnerType = Constants.ObjID_Receipt;
				lobjDoc.midOwnerId = null;
				lobjDoc.midDocType = Constants.DocID_ExpenseScan;
				lobjDoc.mstrText = null;
				lobjDoc.mobjDSBridge = new DSBridgeData();
				lobjDoc.mobjDSBridge.mstrDSHandle = mobjImage.mstrDSHandle;
				lobjDoc.mobjDSBridge.mstrDSLoc = mobjImage.mstrDSLoc;
				lobjDoc.mobjDSBridge.mstrDSTitle = null;
				lobjDoc.mobjDSBridge.mbDelete = true;

				mobjDocOps = new DocOps();
				mobjDocOps.marrCreate = new DocumentData[] {lobjDoc};
			}

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjAux.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_Expense);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjAux.getKey(), GetProcess().getKey(),
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			mobjData.mid = lobjAux.getKey();
			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private String GetNewExpenseNumber()
		throws BigBangJewelException
	{
		String lstrFilter;
		IEntity lrefExpenses;
        MasterDB ldb;
        ResultSet lrsCasualties;
        int llngResult;
        String lstrAux;
        int llngAux;

		try
		{
	        lstrFilter = "%";
			lrefExpenses = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Expense)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCasualties = lrefExpenses.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {lstrFilter},
					new int[] {Integer.MIN_VALUE});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngResult = 1;
		try
		{
			while ( lrsCasualties.next() )
			{
				lstrAux = lrsCasualties.getString(2).substring(lstrFilter.length() - 1);
				llngAux = Integer.parseInt(lstrAux);
				if ( llngAux >= llngResult )
					llngResult = llngAux + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsCasualties.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCasualties.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
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

		return lstrFilter.substring(0, lstrFilter.length() - 1) + llngResult;
	}
}
