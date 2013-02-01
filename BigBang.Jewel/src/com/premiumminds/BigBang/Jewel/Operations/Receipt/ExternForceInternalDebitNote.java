package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.HealthDebitNoteReport;

public class ExternForceInternalDebitNote
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midPrintSet;
	public DocOps mobjDocOps;

	public ExternForceInternalDebitNote(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternForceInternalDebitNote;
	}

	public String ShortDesc()
	{
		return "Criação do Documento de Cobrança";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi gerado um documento de cobrança e respectiva carta de rosto " +
				"para esta nota de débito.");
		lstrBuilder.append(pstrLineBreak).append(pstrLineBreak);

		mobjDocOps.LongDesc(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;
		PrintSetDocument lobjSetClient;
		PrintSetDetail lobjSetReceipt;

		try
		{
			lobjReceipt = (Receipt)GetProcess().GetData();
			lobjReceipt.setAt(Receipt.I.ISINTERNAL, true);
			lobjReceipt.SaveToDb(pdb);

			if ( mobjDocOps == null )
				generateDocOp();

			if ( midPrintSet != null )
			{
				lobjSetClient = PrintSetDocument.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjSetClient.setAt(0, midPrintSet);
				lobjSetClient.setAt(1, mobjDocOps.marrCreate[0].mobjFile);
				lobjSetClient.setAt(2, false);
				lobjSetClient.SaveToDb(pdb);

				lobjSetReceipt = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjSetReceipt.setAt(0, lobjSetClient.getKey());
				lobjSetReceipt.setAt(1, null);
				lobjSetReceipt.SaveToDb(pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());

		TriggerOp(new ExternForceShortCircuit(GetProcess().getKey()), pdb);
	}

	public UUID generatePrintSet(SQLServer pdb)
		throws BigBangJewelException
	{
		PrintSet lobjSet;

		try
		{
			lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjSet.setAt(0, Constants.TID_HealthDebitNote);
			lobjSet.setAt(1, new Timestamp(new java.util.Date().getTime()));
			lobjSet.setAt(2, Engine.getCurrentUser());
			lobjSet.setAt(3, (Timestamp)null);
			lobjSet.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjSet.getKey();
	}

	private void generateDocOp()
		throws BigBangJewelException
	{
		HealthDebitNoteReport lrepHDN;
		FileXfer lobjFile;
		DocumentData lobjDoc;

		lrepHDN = new HealthDebitNoteReport();
		try
		{
			lrepHDN.midReceipt = GetProcess().GetDataKey();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
		lobjFile = lrepHDN.Generate();

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Nota de Débito de Saúde";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_DebitNote;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número";
		lobjDoc.marrInfo[0].mstrValue = lrepHDN.mstrNumber;
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Valor";
		lobjDoc.marrInfo[1].mstrValue = lrepHDN.mstrValue;

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
	}
}
