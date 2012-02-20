package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;

public class ForceValidateSubPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midSubPolicy;

	public ForceValidateSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_ForceValidateSubPolicy;
	}

	public String ShortDesc()
	{
		return "Validação em Migração";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Esta apólice foi migrada do Gescar e a sua validação foi forçada.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb) 
		throws JewelPetriException
	{
		SubPolicy lobjSubPolicy;

		try
		{
			lobjSubPolicy = (SubPolicy)GetProcess().GetData();
			lobjSubPolicy.setAt(7, Constants.StatusID_Valid);
			lobjSubPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		midSubPolicy = lobjSubPolicy.getKey();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A validação será retirada e passará a ser possível fazer alterações em modo de trabalho.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A validação forçada foi retirada.";
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
			lobjSubPolicy = (SubPolicy)GetProcess().GetData();
			lobjSubPolicy.setAt(7, Constants.StatusID_InProgress);
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
