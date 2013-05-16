package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;

public class CloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrNotes;
	private UUID midRequest;
	private String mstrPrevNotes;

	public CloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_QuoteRequest_CloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O processo de consulta de mercado foi fechado." +
				( mstrNotes == null ? "" : pstrLineBreak + pstrLineBreak + "Observações finais:" + pstrLineBreak + mstrNotes );
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;
		QuoteRequest lobjRequest;

		lobjProc = GetProcess();
		midRequest = lobjProc.GetDataKey();

		if ( mstrNotes != null )
		{
			lobjRequest = (QuoteRequest)lobjProc.GetData();
			mstrPrevNotes = (String)lobjRequest.getAt(3);

			try
			{
				lobjRequest.setAt(3, mstrNotes);
				lobjRequest.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		lobjProc.Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo será reaberto.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O processo de consulta foi reaberto." +
				(mstrNotes == null ? "" : " O valor do campo de observações foi reposto:" + pstrLineBreak + mstrPrevNotes );
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProc;
		QuoteRequest lobjRequest;

		lobjProc = GetProcess();

		if ( mstrNotes != null )
		{
			lobjRequest = (QuoteRequest)lobjProc.GetData();

			try
			{
				lobjRequest.setAt(3, mstrPrevNotes);
				lobjRequest.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		lobjProc.Restart(pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjResult;

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_QuoteRequest;
		lobjResult.marrDeleted = new UUID[0];
		lobjResult.marrChanged = new UUID[] { midRequest };
		lobjResult.marrCreated = new UUID[0];
	
		return new UndoSet[] { lobjResult };
	}
}
