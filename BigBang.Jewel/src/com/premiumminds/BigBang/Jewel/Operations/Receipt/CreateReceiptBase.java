package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public abstract class CreateReceiptBase
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ReceiptData mobjData;
	public transient DSBridgeData mobjImage;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;
	public boolean mbForceDebitNote;

	public abstract Timestamp DateCheck() throws BigBangJewelException;

	public abstract UUID GetMediatorID() throws BigBangJewelException;

	public CreateReceiptBase(UUID pidProcess)
	{
		super(pidProcess);
		mbForceDebitNote = false;
	}

	public String ShortDesc()
	{
		return "Criação de Recibo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criado o seguinte recibo:");
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
		IProcess lobjMe;
		Receipt lobjAux;
		IScript lobjScript;
		IProcess lobjProc; 
		TriggerImageOnCreate lopTIOC;
		UUID lidProfile;
		int i;

		try
		{
			lobjMe = GetProcess();

			if ( mobjData.midManager == null )
				mobjData.midManager = lobjMe.GetManagerID();
			if ( mobjData.midMediator == null )
				mobjData.midMediator = GetMediatorID();

			if ( mbForceDebitNote && (mobjData.mstrNumber==null) )
				mobjData.mstrNumber = GetDebitNoteNumber();

			lobjAux = Receipt.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			if ( lobjAux.isForCasualties() )
			{
				lobjAux.setAt(4, null);
				lobjAux.setAt(5, null);
				lobjAux.setAt(6, null);
			}
			for ( i = 3; i < 8; i++ )
				if ( lobjAux.getAt(i) != null )
					lobjAux.setAt(i, ((BigDecimal)lobjAux.getAt(i)).abs());
			if ( lobjAux.getAt(17) != null )
				lobjAux.setAt(17, ((BigDecimal)lobjAux.getAt(17)).abs());
			lobjAux.SaveToDb(pdb);

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjAux.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_Receipt);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjAux.getKey(), lobjMe.getKey(),
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			mobjData.mid = lobjAux.getKey();
			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;

			if ( Constants.MCPID_None.equals(lobjAux.getMediator().getProfile()) )
				TriggerOp(new ExternBlockDirectRetrocession(lobjProc.getKey()), pdb);
			else if ( lobjAux.doCalcRetrocession() )
				lobjAux.SaveToDb(pdb);

			lidProfile = lobjAux.getProfile();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mbForceDebitNote )
		{
			TriggerOp(new ExternForceInternalDebitNote(lobjProc.getKey()), pdb);
		}
		else
		{
			if ( mobjImage != null )
			{
				lopTIOC = new TriggerImageOnCreate(lobjProc.getKey());
				lopTIOC.mobjImage = mobjImage;
				TriggerOp(lopTIOC, pdb);
			}

			if ( Constants.ProfID_Simple.equals(lidProfile) )
			{
				TriggerOp(new ExternForceShortCircuit(lobjProc.getKey()), pdb);
			}

			if ( Constants.ProfID_External.equals(lidProfile) )
			{
				TriggerOp(new ExternForceShortCircuit(lobjProc.getKey()), pdb);
				TriggerOp(new ExternBlockEndProcessSend(lobjProc.getKey()), pdb);
			}

			if ( lobjAux.isReverseCircuit() )
			{
				TriggerOp(new ExternForceReverse(lobjProc.getKey()), pdb);
				TriggerOp(new ExternBlockEndProcessSend(lobjProc.getKey()), pdb);
			}
		}
	}

	private String GetDebitNoteNumber()
		throws BigBangJewelException
	{
		String lstrFilter;
		IEntity lrefReceipts;
        MasterDB ldb;
        ResultSet lrsReceipts;
        int llngResult;
        String lstrAux;
        int llngAux;

		try
		{
	        lstrFilter = GetProcess().GetData().getLabel() + ".%";
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsReceipts = lrefReceipts.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {lstrFilter},
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
			while ( lrsReceipts.next() )
			{
				lstrAux = lrsReceipts.getString(2).substring(lstrFilter.length() - 1);
				llngAux = Integer.parseInt(lstrAux);
				if ( llngAux >= llngResult )
					llngResult = llngAux + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsReceipts.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsReceipts.close();
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
