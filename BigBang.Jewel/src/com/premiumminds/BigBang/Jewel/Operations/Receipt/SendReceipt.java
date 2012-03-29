package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.ReceiptCoverLetterReport;

public class SendReceipt
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrReceiptIDs;
	public boolean mbUseGroups;
	public DocOps mobjDocOps;
	private UUID midClient;
//	private OutgoingMessageData mobjMessage;
	private transient BigDecimal mdblTotal;
	private transient int mlngCount;

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
		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			midClient = GetProcess().GetParent().GetParent().GetDataKey();
		else
			midClient = (UUID)GetProcess().GetParent().GetData().getAt(2);

		try
		{
			if ( mobjDocOps == null )
				generateDocOp();
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
		DocumentData lobjDoc;

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Envio de Recibo";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_ReceiptCoverLetter;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = generateReport();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Recibos Enviados";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Total de Prémios";
		lobjDoc.marrInfo[1].mstrValue = mdblTotal.toPlainString();

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
	}

	private byte[] generateReport()
		throws BigBangJewelException
	{
		ReceiptCoverLetterReport lrepRCL;
		FileXfer lobjResult;

		lrepRCL = new ReceiptCoverLetterReport();
		lrepRCL.midClient = midClient;
		lrepRCL.marrReceiptIDs = marrReceiptIDs;

		lobjResult = lrepRCL.Generate();
		mdblTotal = lrepRCL.mdblTotal;
		mlngCount = lrepRCL.mlngCount;

		return lobjResult.GetVarData();
	}
}
