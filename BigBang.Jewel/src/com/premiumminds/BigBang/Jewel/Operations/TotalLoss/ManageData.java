package com.premiumminds.BigBang.Jewel.Operations.TotalLoss;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.TotalLossData;
import com.premiumminds.BigBang.Jewel.Objects.TotalLoss;

public class ManageData
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public TotalLossData mobjData;

	public ManageData(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_TotalLoss_ManageData;
	}

	public String ShortDesc()
	{
		return "Alteração de Dados";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Novos dados do processo:");
			lstrResult.append(pstrLineBreak);
			mobjData.Describe(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		TotalLoss lobjAux;

		try
		{
			if ( mobjData != null )
			{
				lobjAux = TotalLoss.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

				mobjData.mobjPrevValues = new TotalLossData();
				mobjData.mobjPrevValues.FromObject(lobjAux);

				mobjData.midManager = GetProcess().GetManagerID();
				mobjData.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Os dados anteriores serão repostos:");
			lstrResult.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		if ( mobjData != null )
		{
			lstrResult.append("Os dados anteriores foram repostos:");
			lstrResult.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		TotalLoss lobjAux;

		try
		{
			if ( mobjData != null )
			{
				lobjAux = TotalLoss.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

				mobjData.mobjPrevValues.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
