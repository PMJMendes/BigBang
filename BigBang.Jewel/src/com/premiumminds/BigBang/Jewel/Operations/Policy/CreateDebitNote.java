package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DebitNoteData;
import com.premiumminds.BigBang.Jewel.Objects.DebitNote;

public class CreateDebitNote
	extends Operation
{
	private static final long serialVersionUID = 1L;

	DebitNoteData mobjData;

	public CreateDebitNote(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_CreateDebitNote;
	}

	public String ShortDesc()
	{
		return "Criação de Nota de Débito";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();
		lstrBuilder.append("Foi criada a seguinte nota de débito:").append(pstrLineBreak);
		mobjData.Describe(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		DebitNote lobjNote;

		try
		{
			if ( mobjData.mstrNumber == null )
				mobjData.mstrNumber = GetDebitNoteNumber(pdb);

			lobjNote = DebitNote.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjNote);
			lobjNote.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private String GetDebitNoteNumber(SQLServer pdb)
		throws BigBangJewelException
	{
		Timestamp ldtAux;
		String lstrFilter;
		IEntity lrefNotes;
        ResultSet lrsNotes;
        int llngResult;
        String lstrAux;
        int llngAux;

		ldtAux = new Timestamp(new java.util.Date().getTime());

		try
		{
	        lstrFilter = ldtAux.toString().substring(0, 4) + "/%";
			lrefNotes = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DebitNote)); 
			lrsNotes = lrefNotes.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {lstrFilter},
					new int[] {Integer.MIN_VALUE});
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngResult = 1;
		try
		{
			while ( lrsNotes.next() )
			{
				lstrAux = lrsNotes.getString(2).substring(lstrFilter.length() - 1);
				llngAux = Integer.parseInt(lstrAux);
				if ( llngAux >= llngResult )
					llngResult = llngAux + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsNotes.close(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsNotes.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lstrFilter.substring(0, lstrFilter.length() - 1) + llngResult;
	}
}
