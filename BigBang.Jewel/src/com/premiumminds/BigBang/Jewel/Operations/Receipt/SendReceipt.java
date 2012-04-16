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
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.ReceiptCoverLetterReport;

public class SendReceipt
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrReceiptIDs;
	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetDocument;
	public UUID midSetDetail;
	public DocOps mobjDocOps;
	private UUID midClient;
//	private OutgoingMessageData mobjMessage;

	public SendReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_SendReceipt;
	}

	public String ShortDesc()
	{
		return "Envio de Recibo ao Segurado";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi gerada uma carta de rosto para enviar este recibo.");
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
		PrintSet lobjSet;
		PrintSetDocument lobjSetClient;
		PrintSetDetail lobjSetReceipt;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			midClient = GetProcess().GetParent().GetParent().GetDataKey();
		else
			midClient = (UUID)GetProcess().GetParent().GetData().getAt(2);

		try
		{
			if ( mobjDocOps == null )
				generateDocOp();

			if ( mbUseSets )
			{
				if ( midSet == null )
				{
					lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSet.setAt(0, Constants.TID_ReceiptCoverLetter);
					lobjSet.setAt(1, new Timestamp(new java.util.Date().getTime()));
					lobjSet.setAt(2, Engine.getCurrentUser());
					lobjSet.setAt(3, (Timestamp)null);
					lobjSet.SaveToDb(pdb);
					midSet = lobjSet.getKey();
				}

				if ( midSetDocument == null )
				{
					lobjSetClient = PrintSetDocument.GetInstance(Engine.getCurrentNameSpace(), null);
					lobjSetClient.setAt(0, midSet);
					lobjSetClient.setAt(1, mobjDocOps.marrCreate[0].mobjFile);
					lobjSetClient.setAt(2, false);
					lobjSetClient.SaveToDb(pdb);
					midSetDocument = lobjSetClient.getKey();
				}

				lobjSetReceipt = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), null);
				lobjSetReceipt.setAt(0, midSetDocument);
				lobjSetReceipt.setAt(1, null);
				lobjSetReceipt.SaveToDb(pdb);
				midSetDetail = lobjSetReceipt.getKey();
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());
	}

	private void generateDocOp()
		throws BigBangJewelException
	{
		ReceiptCoverLetterReport lrepRCL;
		FileXfer lobjFile;
		DocumentData lobjDoc;

		lrepRCL = new ReceiptCoverLetterReport();
		lrepRCL.midClient = midClient;
		lrepRCL.marrReceiptIDs = marrReceiptIDs;
		lobjFile = lrepRCL.Generate();

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Envio de Recibo";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_ReceiptCoverLetter;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Recibos Enviados";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(lrepRCL.mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Total de Prémios";
		lobjDoc.marrInfo[1].mstrValue = lrepRCL.mdblTotal.toPlainString();

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
	}
}
