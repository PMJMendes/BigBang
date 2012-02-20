package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;

public class VoidSubPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public Timestamp mdtEffectDate;
	public String mstrMotive;
	public String mstrNotes;
	private UUID midSubPolicy;
	private UUID midPrevStatus;
	private Timestamp mdtPrevEndDate;

	public VoidSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_VoidSubPolicy;
	}

	public String ShortDesc()
	{
		return "Anulação da Apólice Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();
		lstrBuilder.append("A apólice adesão foi anulada com data efeito a ").append(mdtEffectDate.toString().substring(0, 10))
				.append(pstrLineBreak).append("Motivo da anulação: ").append(mstrMotive).append(pstrLineBreak);

		if ( mstrNotes != null )
			lstrBuilder.append("Observações:").append(pstrLineBreak).append(mstrNotes).append(pstrLineBreak);

		return lstrBuilder.toString();
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
			midSubPolicy = lobjSubPolicy.getKey();
			midPrevStatus = (UUID)lobjSubPolicy.getAt(7);
			mdtPrevEndDate = (Timestamp)lobjSubPolicy.getAt(4);

			lobjSubPolicy.setAt(7, Constants.StatusID_Voided);
			lobjSubPolicy.setAt(4, mdtEffectDate);
			lobjSubPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();
		lstrBuilder.append("A apólice adesão será reposta");
		if ( mdtPrevEndDate != null )
			lstrBuilder.append(" com a data fim anterior de ").append(mdtPrevEndDate.toString().substring(0, 10));
		lstrBuilder.append(".");

		return lstrBuilder.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A apólice adesão foi reposta em vigor.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		SubPolicy lobjSubPolicy;

		try
		{
			lobjSubPolicy = (SubPolicy)GetProcess().GetData();

			lobjSubPolicy.setAt(7, midPrevStatus);
			lobjSubPolicy.setAt(4, mdtPrevEndDate);
			lobjSubPolicy.SaveToDb(pdb);
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
		lobjSet.marrChanged = new UUID[]{midSubPolicy};

		return new UndoSet[]{lobjSet}; 
	}
}
