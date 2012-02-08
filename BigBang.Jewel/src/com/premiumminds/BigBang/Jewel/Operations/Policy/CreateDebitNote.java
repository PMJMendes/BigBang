package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DebitNoteData;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.DebitNote;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.DebitNoteReport;

public class CreateDebitNote
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public DebitNoteData mobjData;
	public DocOps mobjDocOps;

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
		DebitNoteReport lrepDN;
		DocumentData lobjDoc;

		try
		{
			if ( mobjData.mstrNumber == null )
				mobjData.mstrNumber = GetDebitNoteNumber(pdb);
			if ( mobjData.mdtIssue == null )
				mobjData.mdtIssue = new Timestamp(new java.util.Date().getTime());

			lobjNote = DebitNote.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjNote);
			lobjNote.SaveToDb(pdb);
			mobjData.mid = lobjNote.getKey();

			lrepDN = new DebitNoteReport();
			lrepDN.mstrNumber = mobjData.mstrNumber;
			lrepDN.midPolicy = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjData.midProcess).GetDataKey();
			lrepDN.mstrReceipt = null;
			lrepDN.mdtMaturity = mobjData.mdtMaturity;
			lrepDN.mdblValue = mobjData.mdblValue;
			lrepDN.mdtDate = mobjData.mdtIssue;

			lobjDoc = new DocumentData();
			lobjDoc.mstrName = "Nota de Débito";
			lobjDoc.midOwnerType = Constants.ObjID_Policy;
			lobjDoc.midOwnerId = lrepDN.midPolicy;
			lobjDoc.midDocType = Constants.DocID_DebitNote;
			lobjDoc.mstrText = null;
			lobjDoc.mobjFile = lrepDN.Generate().GetVarData();
			lobjDoc.marrInfo = new DocInfoData[1];
			lobjDoc.marrInfo[0] = new DocInfoData();
			lobjDoc.marrInfo[0].mstrType = "Número";
			lobjDoc.marrInfo[0].mstrValue = mobjData.mstrNumber;

			mobjDocOps = new DocOps();
			mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
			mobjDocOps.RunSubOp(pdb, lrepDN.midPolicy);
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

	public String UndoDesc(String pstrLineBreak)
	{
		return "A nota de débito será eliminada.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A nota de débito criada foi eliminada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		DebitNote lobjNote;

		mobjDocOps.UndoSubOp(pdb, null);

		try
		{
			lobjNote = DebitNote.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);
			lobjNote.getDefinition().Delete(pdb, lobjNote.getKey());
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
