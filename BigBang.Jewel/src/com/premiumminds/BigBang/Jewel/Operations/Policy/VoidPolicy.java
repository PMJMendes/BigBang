package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;

public class VoidPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public Timestamp mdtEffectDate;
	public String mstrMotive;
	public String mstrNotes;
	private UUID midPolicy;
	private UUID midPrevStatus;
	private Timestamp mdtPrevEndDate;

	public VoidPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_VoidPolicy;
	}

	public String ShortDesc()
	{
		return "Anulação da Apólice";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();
		lstrBuilder.append("A apólice foi anulada com data efeito a ").append(mdtEffectDate.toString().substring(0, 10))
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
		Policy lobjPolicy;

		try
		{
			lobjPolicy = (Policy)GetProcess().GetData();
			midPolicy = lobjPolicy.getKey();
			midPrevStatus = (UUID)lobjPolicy.getAt(13);
			mdtPrevEndDate = (Timestamp)lobjPolicy.getAt(9);

			lobjPolicy.setAt(13, Constants.StatusID_Voided);
			lobjPolicy.setAt(9, mdtEffectDate);
			lobjPolicy.SaveToDb(pdb);
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
		lstrBuilder.append("A apólice será reposta");
		if ( mdtPrevEndDate != null )
			lstrBuilder.append(" com a data fim anterior de ").append(mdtPrevEndDate.toString().substring(0, 10));
		lstrBuilder.append(".");

		return lstrBuilder.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A apólice foi reposta em vigor.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;

		try
		{
			lobjPolicy = (Policy)GetProcess().GetData();

			lobjPolicy.setAt(13, midPrevStatus);
			lobjPolicy.setAt(9, mdtPrevEndDate);
			lobjPolicy.SaveToDb(pdb);
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
		lobjSet.midType = Constants.ObjID_Policy;
		lobjSet.marrChanged = new UUID[]{midPolicy};

		return new UndoSet[]{lobjSet}; 
	}
}
