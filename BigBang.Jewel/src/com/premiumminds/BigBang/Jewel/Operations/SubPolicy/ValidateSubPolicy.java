package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class ValidateSubPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midSubPolicy;

	public ValidateSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_ValidateSubPolicy;
	}

	public String ShortDesc()
	{
		return "Validação da Apólice Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice adesão foi validada.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		SubPolicy lobjSubPolicy;
		DetailedBase lobjValidation;
		Hashtable<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;

		try
		{
			lobjSubPolicy = (SubPolicy)GetProcess().GetData();
			midSubPolicy = lobjSubPolicy.getKey();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			lobjValidation = lobjSubPolicy.GetDetailedObject();

			if ( lobjValidation == null )
				DetailedBase.DefaultSubValidate(lobjSubPolicy, pdb);
			else
				lobjValidation.SubValidate(pdb);
		}
		catch (PolicyValidationException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			lobjSubPolicy.setAt(7, Constants.StatusID_Valid);
			lobjSubPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		larrItems = new Hashtable<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
			while ( lrs.next() )
			{
				lobjAgendaProc = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
				larrItems.put((UUID)lobjAgendaProc.getAt(0),
						AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgendaProc.getAt(0)));
			}
			lrs.close();
			lrs = null;

			for ( AgendaItem lobjItem: larrItems.values() )
			{
				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
			}
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A validação será retirada e passará a ser possível fazer alterações em modo de trabalho.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A validação da apólice adesão foi retirada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		SubPolicy lobjSubPolicy;
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	try
    	{
			lobjSubPolicy = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), midSubPolicy);
			lobjSubPolicy.setAt(13, Constants.StatusID_InProgress);
			lobjSubPolicy.SaveToDb(pdb);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Validação de Apólice Adesão");
			lobjItem.setAt(1, GetProcess().GetManagerID());
			lobjItem.setAt(2, Constants.ProcID_SubPolicy);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_SubPolicy_ValidateSubPolicy}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_SubPolicy;
		lobjSet.marrChanged = new UUID[] {midSubPolicy};
		lobjSet.marrCreated = new UUID[] {};
		lobjSet.marrDeleted = new UUID[] {};

		return new UndoSet[] {lobjSet};
	}
}
